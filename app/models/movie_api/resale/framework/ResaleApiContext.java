package models.movie_api.resale.framework;

import models.mer.Merchant;
import models.movie_api.ApiResponse;
import models.movie_api.ApiResultCode;
import org.apache.commons.lang.builder.ToStringBuilder;
import util.extension.InvocationContext;

import java.util.Map;

/**
 * 分销接口上下文信息.
 */
public class ResaleApiContext implements InvocationContext {

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

    public ApiResultCode result = ApiResultCode.SUCCESS;

    public String resultMessage;

    public ApiResponse apiResponse;

    public Long lastVersion;

    public Merchant resaler;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("appCode", appCode)
                .append("method", method)
                .append("time", time)
                .append("checkValue", checkValue)
                .append("params", params)
                .append("result", result)
                .append("resultMessage", resultMessage)
                .append("apiResponse", apiResponse)
                .append("lastVersion", lastVersion)
                .toString();
    }

}
