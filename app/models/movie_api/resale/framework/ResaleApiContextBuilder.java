package models.movie_api.resale.framework;

import models.movie_api.ApiResultCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ResaleApiContext Builder.
 */
public class ResaleApiContextBuilder {

    private ResaleApiContext apiContext;

    public ResaleApiContextBuilder() {
        apiContext = new ResaleApiContext();
    }

    public ResaleApiContextBuilder appCode(String appCode) {
        apiContext.appCode = appCode;
        return this;
    }

    public ResaleApiContextBuilder method(String method) {
        apiContext.method = method;
        return this;
    }

    public ResaleApiContextBuilder time(Long time) {
        apiContext.time = time;
        return this;
    }


    public ResaleApiContextBuilder setCurrentTime() {
        apiContext.time = new Date().getTime() / 1000l;
        return this;
    }

    public ResaleApiContextBuilder checkValue(String checkValue) {
        apiContext.checkValue = checkValue;
        return this;
    }

    /**
     * 按指定Action的方式生成checkValue.
     *
     * @param action
     * @return
     */
    public ResaleApiContextBuilder checkValue(ResaleApiBaseAction action) {
        apiContext.checkValue = action.getCheckValue(apiContext);
        return this;
    }

    public ResaleApiContextBuilder params(Map<String, String[]> params) {
        apiContext.params = params;
        return this;
    }

    public ResaleApiContextBuilder addParam(String key, String[] value) {
        if (apiContext.params == null) {
            apiContext.params = new HashMap<>();
        }
        apiContext.params.put(key, value);
        return this;
    }

    public ResaleApiContext build() {
        apiContext.result = ApiResultCode.SUCCESS; //默认成功
        return apiContext;
    }
}
