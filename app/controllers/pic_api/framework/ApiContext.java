package controllers.pic_api.framework;

import models.movie_api.ApiResultCode;
import org.apache.commons.lang.builder.ToStringBuilder;
import util.extension.InvocationContext;

import java.math.BigDecimal;
import java.util.Map;

/**
 * JSON接口处理Context.
 */
public class ApiContext implements InvocationContext {

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
     * 这个用于对象转化，如果有这个值，就试图作为对象进行处理.
     */
    public Map<String, String[]> paramsMap;

    public ApiResultCode result = ApiResultCode.SUCCESS;

    public String resultMessage;

    public Object data;
    public Long   lastVersion;

    public Long getLongParam(String key) {
        Object o = this.params.get(key);
        if (o == null) return null;
        if (o instanceof Double) {
            return ((Double) o).longValue();
        }
        if (o instanceof String) {
            return Long.valueOf(o.toString());
        }
        if (o instanceof Long) {
            return (Long) o;
        }
        throw new RuntimeException("the " + key + " value=(" + o + ") is not support, type=" + o.getClass().getName());
    }


    public Integer getIntegerParam(String key, Integer defaultValue) {
        Integer value = getIntegerParam(key);
        return (value == null ? defaultValue : value);
    }

    public Integer getIntegerParam(String key) {
        Object o = this.params.get(key);
        if (o == null) return null;
        if (o instanceof Double) {
            return ((Double) o).intValue();
        }
        if (o instanceof String) {
            return Integer.valueOf(o.toString());
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        throw new RuntimeException("the " + key + " value=(" + o + ") is not support, type=" + o.getClass().getName());
    }

    public BigDecimal getBigDecimalParam(String key) {
        Object o = this.params.get(key);
        if (o == null) return null;
        if (o instanceof Double) {
            return BigDecimal.valueOf((Double) o);
        }
        if (o instanceof String) {
            return new BigDecimal(o.toString());
        }
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        }
        throw new RuntimeException("the " + key + " value=(" + o + ") is not support, type=" + o.getClass().getName());
    }

    public Boolean getBooleanParam(String key) {
        Object o = this.params.get(key);
        if (o == null) return null;
        if (o instanceof String) {
            return Boolean.parseBoolean(o.toString());
        }
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        throw new RuntimeException("the " + key + " value=(" + o + ") is not support, type=" + o.getClass().getName());
    }

    public String getStringParam(String key) {
        Object o = this.params.get(key);
        if (o == null) return null;
        if (o instanceof String) {
            return (String) o;
        }
        throw new RuntimeException("the " + key + " value=(" + o + ") is not support, type=" + o.getClass().getName());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("appCode", appCode)
                .append("machineCode", machineCode)
                .append("method", method)
                .append("time", time)
                .append("checkValue", checkValue)
                .append("params", params)
                .append("result", result)
                .append("resultMessage", resultMessage)
                .append("data", data)
                .append("lastVersion", lastVersion)
                .toString();
    }

}
