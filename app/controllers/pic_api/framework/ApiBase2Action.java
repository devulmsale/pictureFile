package controllers.pic_api.framework;

import com.google.gson.Gson;
import models.mer.Merchant;
import models.movie_api.ApiResultCode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.data.binding.Binder;
import play.data.binding.ParamNode;
import play.data.binding.RootParamNode;
import util.extension.ExtensionInvocation;
import util.extension.ExtensionResult;
import util.extension.annotation.ExtensionPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * API接口扩展类v2，各业务对接API，直接继承这个类即可。
 */
@ExtensionPoint("JsonApi2Action")
public abstract class ApiBase2Action<T, V> implements ExtensionInvocation<ApiContext> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtensionResult execute(ApiContext context) {
        // 检查必填参数.
        String[] notPresentParams = checkRequiredParams(context);
        if (notPresentParams != null && notPresentParams.length > 0) {
            // 缺少必填参数，返回错误400
            context.result = ApiResultCode.G400;
            context.resultMessage = "必填参数不完整，缺少以下参数：" + StringUtils.join(notPresentParams, ", ");
            return getReturnResult(context);
        }

        // 如果参数中没有checkValue，就不做参数检查(开放给web应用调用.) 这个时候如果应用需要user参数，就需要在context中加入这个参数
        if (context.checkValue != null) {
            // 检查校验和.
            String checkValue = getCheckValue(context);

            if (checkValue == null) {
                // 找不到对应的appCode，请求非法.
                context.result = ApiResultCode.G401;
                context.resultMessage = "不存在的AppCode";
                return getReturnResult(context);
            }
        }

//        if (!checkValue.equals(context.checkValue)) {
//            Logger.info("校验失败，context.checkValue=" + context.checkValue + ", checkValue=" + checkValue);
//            context.result = ApiResultCode.G400;
//            context.resultMessage = "参数校验失败";
//            return getReturnResult(context);
//        }


        T requestObject = createEmptyRequestObject();

        // 填充request对象
        RootParamNode paramNode = ParamNode.convert(context.paramsMap);
        Binder.bindBean(paramNode, "", requestObject);

        Logger.info("============= 参数检查正常，开始执行." + context.paramsMap);
        Logger.info("     requestObject=" + new Gson().toJson(requestObject));
        // 调用正常业务逻辑.
        context.result = ApiResultCode.SUCCESS;
        Logger.info("============= 参数检查正常，开始执行>>" + this.getClass().getName());
        try {
            context.data = doExecute(context, requestObject);
        } catch (Throwable t) {
            Logger.info(t, "Api出错");
            context.result = ApiResultCode.G500;
            context.resultMessage = t.getMessage();
        }

        return getReturnResult(context);
    }

    public String getCheckValue(ApiContext context) {
        String keyValueString = getKeyValuesString(context);
        if (keyValueString == null) {
            return null;
        }
        return getMd5Hex(keyValueString);
    }

    public String getKeyValuesString(ApiContext context) {
        List<String> keyValueList = new ArrayList<>();

        Merchant merchant = Merchant.findByCode(context.appCode);
        if (merchant == null) {
            Logger.info("请求传入的appCode(" + context.appCode + ")没有找到对应的Resaler,可能是没有审批通过。");
            return null;
        }

        keyValueList.add("appSecret=" + merchant.code);
        keyValueList.add("machineCode=" + context.machineCode);
        keyValueList.add("time=" + context.time);
        keyValueList.add("method=" + context.method);

        for (String key : getParamNames()) {
            Object value = context.params.get(key);
            if (value != null) {
                Logger.info(key + ".valueType=" + value.getClass().getName() + ", value=" + value);
                if (value instanceof Long) {
                    keyValueList.add(key + "=" + new Double((Long) value)); // 解决json中总是出现Double的问题
                } else {
                    keyValueList.add(key + "=" + value);
                }
            }
        }

        String keyValueString = StringUtils.join(keyValueList, "@@");
        Logger.info("keyValueString=" + keyValueString);
        return keyValueString;
    }

    public String getMd5Hex(String value) {
        String md5Hex = DigestUtils.md5Hex(value).toUpperCase();
        Logger.debug("   md5KeyValues=" + md5Hex);
        return md5Hex;
    }

    private ExtensionResult getReturnResult(ApiContext context) {
        if (context.resultMessage == null) {
            context.resultMessage = context.result.getMessage(); //使用枚举变量中定义的消息.
        }
        return ExtensionResult.code(context.result.getCode()).message(context.resultMessage);
    }

    /**
     * 返回缺少的字段
     *
     * @param context
     * @return
     */
    private String[] checkRequiredParams(ApiContext context) {
        if (context.params == null) {
            return getRequiredParamNames(); //所有必填参数都没有出现.
        }
        List<String> notPresentParams = new ArrayList<>();
        for (String paramName : getRequiredParamNames()) {
            if (!context.params.containsKey(paramName)) {
                notPresentParams.add(paramName);
            }
        }
        return notPresentParams.toArray(new String[notPresentParams.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean match(ApiContext context) {
        return getMethod().equals(context.method);
    }

    /**
     * 声明需要的参数，请注意按文档要求的顺序返回。
     * 以进行checkValue检查。
     *
     * @return 参数值对.
     */
    public abstract String[] getParamNames();

    /**
     * 返回响应的方法名，注意是大小写敏感的。
     *
     * @return 方法名
     */
    public abstract String getMethod();

    /**
     * 声明必填的字段，请注意按文档要求返回。
     * 如果这里存在的字段，没有在params存在，则返回错误400 Bad Request
     *
     * @return 声明必填的字段数组
     */
    public String[] getRequiredParamNames() {
        return new String[0];
    }

    /**
     * 建立空白的请求参数对象。
     * 框架会填充参数值.
     *
     * @return
     */
    public abstract T createEmptyRequestObject();

    /**
     * 业务处理逻辑，返回一个需要包装的值对象.
     *
     * @param context
     * @param requestObject
     * @return
     */
    public abstract V doExecute(ApiContext context, T requestObject);
}
