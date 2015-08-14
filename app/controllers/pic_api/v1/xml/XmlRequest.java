package controllers.pic_api.v1.xml;

import models.movie_api.resale.framework.ResaleApiContext;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * XML请求类.
 */
public class XmlRequest {

    /**
     * 请求应用ID，由系统统一分配.
     */
    public String appCode;

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

    public Map<String, String[]> params;


    public ResaleApiContext toContext() {
        ResaleApiContext context = new ResaleApiContext();
        context.appCode = this.appCode;
        context.method = this.method;
        context.time = this.time;
        context.checkValue = this.checkValue;
        context.params = new HashMap<>();
        context.params.putAll(this.params);
        return context;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("appCode", appCode)
                .append("method", method)
                .append("checkValue", checkValue)
                .append("params", params)
                .toString();
    }
}
