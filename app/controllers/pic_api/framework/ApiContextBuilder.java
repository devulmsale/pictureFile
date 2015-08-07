package controllers.pic_api.framework;

import models.movie_api.ApiResultCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ApiContext的Builder类.
 */
public class ApiContextBuilder {

    private ApiContext apiContext;

    public ApiContextBuilder() {
        apiContext = new ApiContext();
    }

    public ApiContextBuilder appCode(String appCode) {
        apiContext.appCode = appCode;
        return this;
    }

    public ApiContextBuilder machineCode(String machineCode) {
        apiContext.machineCode = machineCode;
        return this;
    }

    public ApiContextBuilder method(String method) {
        apiContext.method = method;
        return this;
    }

    public ApiContextBuilder time(Long time) {
        apiContext.time = time;
        return this;
    }


    public ApiContextBuilder setCurrentTime() {
        apiContext.time = new Date().getTime() / 1000l;
        return this;
    }

    public ApiContextBuilder checkValue(String checkValue) {
        apiContext.checkValue = checkValue;
        return this;
    }

    /**
     * 按指定Action的方式生成checkValue.
     *
     * @param action
     * @return
     */
    public ApiContextBuilder checkValue(ApiBaseAction action) {
        apiContext.checkValue = action.getCheckValue(apiContext);
        return this;
    }

    public ApiContextBuilder params(Map<String, Object> params) {
        apiContext.params = params;
        return this;
    }

    public ApiContextBuilder addParam(String key, Object value) {
        if (apiContext.params == null) {
            apiContext.params = new HashMap<>();
        }
        apiContext.params.put(key, value);
        return this;
    }

    public ApiContext build() {
        apiContext.result = ApiResultCode.SUCCESS; //默认成功
        return apiContext;
    }
}
