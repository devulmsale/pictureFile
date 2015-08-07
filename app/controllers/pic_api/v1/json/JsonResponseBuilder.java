package controllers.pic_api.v1.json;


import controllers.pic_api.framework.ApiContext;
import models.movie_api.ApiResultCode;

/**
 * 请求类型.
 */
public class JsonResponseBuilder {
    private JsonResponse jsonResponse;

    public JsonResponseBuilder() {
        this(ApiResultCode.SUCCESS);
    }

    public JsonResponseBuilder(ApiResultCode apiResultCode) {
        jsonResponse = new JsonResponse();
        jsonResponse.code = apiResultCode.getCode();
        jsonResponse.message = apiResultCode.getMessage();
    }

    public JsonResponseBuilder withData(Object data) {
        jsonResponse.setData(data);
        return this;
    }

    public JsonResponseBuilder code(Integer code) {
        jsonResponse.code = code;
        return this;
    }

    public JsonResponseBuilder message(String message) {
        jsonResponse.message = message;
        return this;
    }

    public JsonResponseBuilder lastVersion(Long lastVersion) {
        jsonResponse.lastVersion = lastVersion;
        return this;
    }

    public JsonResponse build() {
        return jsonResponse;
    }

    public JsonResponseBuilder fromContext(ApiContext context) {
        jsonResponse.code = context.result.getCode();
        if (context.resultMessage != null) {
            jsonResponse.message = context.resultMessage;
        } else {
            jsonResponse.message = context.result.getMessage();
        }
        jsonResponse.lastVersion = context.lastVersion;
        return this;
    }
}
