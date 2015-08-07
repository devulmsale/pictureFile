package controllers.pic_api.v1.json;

import controllers.pic_api.framework.ApiContext;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * JSON请求基类
 */
public class JsonRequest {

    /**
     * 请求应用ID，由系统统一分配.
     */
    public String appCode;

    /**
     * 机器码，客户端使用机器的一个唯一特征码生成，保证所有客户端这个值都不一样
     */
    public String machineCode;

    /**
     * 调用方法.
     */
    public String method;

    /**
     * 请求时间戳.
     */
    public Long time;

    /**
     * 校验和.
     * 以appCode+machineCode+method+按文档中params顺序，依次连接，以@@分隔，用MD5计算的校验和
     */
    public String checkValue;

    public Map<String, Object> params;

    /**
     * 这个用于对象转化.
     */
    public Map<String, String[]> paramsMap;


    public ApiContext toContext() {
        ApiContext context = new ApiContext();
        context.appCode = this.appCode;
        context.machineCode = this.machineCode;
        context.method = this.method;
        context.time = this.time;
        context.checkValue = this.checkValue;
        context.params = new HashMap<>();
        context.params.putAll(this.params);
        context.paramsMap = this.paramsMap;
        return context;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("appCode", appCode)
                .append("machineCode", machineCode)
                .append("method", method)
                .append("checkValue", checkValue)
                .append("params", params)
                .toString();
    }
}
