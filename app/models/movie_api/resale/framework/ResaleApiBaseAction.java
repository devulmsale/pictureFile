package models.movie_api.resale.framework;

import models.mer.Merchant;
import models.movie_api.ApiResponse;
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

import java.util.*;

/**
 * 分销接口API基础类.
 *
 * @param <T> 请求参数值类型
 * @param <V> 返回的结果类型
 */
@ExtensionPoint("JsonApiAction")
public abstract class ResaleApiBaseAction<T, V extends ApiResponse> implements ExtensionInvocation<ResaleApiContext> {

    /**
     * {@inheritDoc}
     */
    @Override
    public final ExtensionResult execute(ResaleApiContext context) {
        long start = System.currentTimeMillis();

        // 检查必填参数.
        String[] notPresentParams = checkRequiredParams(context);
        if (notPresentParams != null && notPresentParams.length > 0) {
            // 缺少必填参数，返回错误400
            context.result = ApiResultCode.G400;
            context.resultMessage = "必填参数不完整，缺少以下参数：" + StringUtils.join(notPresentParams, ", ");
            return getReturnResult(context);
        }

        // 检查校验和.
        String checkValue = getCheckValue(context);

        if (checkValue == null) {
            // 找不到对应的appCode，请求非法.
            context.result = ApiResultCode.G401;
            context.resultMessage = "不存在的AppCode";
            return getReturnResult(context);
        }

        if (!checkValue.equals(context.checkValue)) {
            Logger.info("校验失败，context.checkValue=" + context.checkValue + ", checkValue=" + checkValue);
            context.result = ApiResultCode.G400;
            context.resultMessage = "参数校验失败";
            return getReturnResult(context);
        }


        T requestObject = createEmptyRequestObject();

        // 填充request对象
        RootParamNode paramNode = ParamNode.convert(context.params);
        Binder.bindBean(paramNode, "", requestObject);

        Logger.info("============= 参数检查正常，开始执行.");
        // 调用正常业务逻辑.
        context.result = ApiResultCode.SUCCESS;
        Logger.info("============= 参数检查正常，开始执行>>" + this.getClass().getName());
        try {
            context.apiResponse = doExecute(context, requestObject);
        } catch (Throwable t) {
            Logger.error(t, "服务器出错");
            context.result = ApiResultCode.G500;
            context.resultMessage = t.getMessage();
        }
        long end = System.currentTimeMillis();
        Logger.error("RESALE_API_CALL_END: " + this.getClass().getName() + ", " + (end-start) + " ms");

        return getReturnResult(context);
    }

    public String getCheckValue(ResaleApiContext context) {
        String keyValueString = getKeyValuesString(context);
        if (keyValueString == null) {
            return null;
        }
        return keyValueString;
    }

    public String getKeyValuesString(ResaleApiContext context) {
        Map<String, Object> keyValueMap = new HashMap<>();

        Merchant resaler = Merchant.findByCode(context.appCode);
        if (resaler == null) {
            Logger.info("请求传入的appCode(" + context.appCode + ")没有找到对应的Resaler,可能是没有审批通过。");
            return null;
        }

        context.resaler = resaler;
        keyValueMap.put("appSecret", resaler.appSecret);

        for (String key : context.params.keySet()) {
            if (!"checkValue".equals(key) && !"body".equals(key) ) {
                String[] values = context.params.get(key);
                if (values != null && values.length > 0) {
                    keyValueMap.put(key, values[0]);
                }
            }
        }

        return signParams(keyValueMap);
    }

    private String signParams(Map<String, Object> paramsMap) {
        TreeSet<String> sortedKeySet = new TreeSet<>();
        sortedKeySet.addAll(paramsMap.keySet());
        List<String> valueList = new ArrayList<>();
        for (String key : sortedKeySet) {
            valueList.add(key + "=" + paramsMap.get(key));
        }
        String value = StringUtils.join(valueList, "@@");
        Logger.info("params=(" + value + ")");
        return DigestUtils.md5Hex(value).toUpperCase();
    }

    private ExtensionResult getReturnResult(ResaleApiContext context) {
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
    private String[] checkRequiredParams(ResaleApiContext context) {
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
    public final boolean match(ResaleApiContext context) {
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
    public abstract V doExecute(ResaleApiContext context, T requestObject);
}
