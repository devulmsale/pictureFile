package controllers.pic_api.v1.xml;

import models.movie_api.ApiResponse;
import models.movie_api.ApiResultCode;

/**
 * XmlResponse的Builder类.
 */
public class ApiResponseBuilder {

    private ApiResponse apiResponse;

    public ApiResponseBuilder() {
        this(ApiResultCode.SUCCESS);
    }

    public ApiResponseBuilder(ApiResultCode apiResultCode) {
        apiResponse = new ApiResponse();
        apiResponse.code = apiResultCode.getCode();
        apiResponse.message = apiResultCode.getMessage();
    }

//    public ApiResponseBuilder(ResaleApiContext context) {
//        if (context.apiResponse != null) {
//            apiResponse = context.apiResponse;
//        } else {
//            apiResponse = new ApiResponse();
//        }
//        apiResponse.code = context.result.getCode();
//        if (context.resultMessage != null) {
//            apiResponse.message = context.resultMessage;
//        } else {
//            apiResponse.message = context.result.getMessage();
//        }
//        apiResponse.lastVersion = context.lastVersion;
//    }

    public ApiResponseBuilder code(Integer code) {
        apiResponse.code = code;
        return this;
    }

    public ApiResponseBuilder message(String message) {
        apiResponse.message = message;
        return this;
    }

    public ApiResponseBuilder lastVersion(Long lastVersion) {
        apiResponse.lastVersion = lastVersion;
        return this;
    }

    public ApiResponse build() {
        return apiResponse;
    }

}
