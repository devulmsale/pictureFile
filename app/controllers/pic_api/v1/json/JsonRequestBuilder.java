package controllers.pic_api.v1.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controllers.pic_api.framework.ApiBase2Action;
import controllers.pic_api.framework.ApiBaseAction;
import controllers.pic_api.framework.ApiContext;
import play.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JsonRequest Builder类.
 * 目前这个类只用于测试.
 */
public class JsonRequestBuilder {
    private JsonRequest jsonRequest;

    public JsonRequestBuilder() {
        jsonRequest = new JsonRequest();
    }

    public JsonRequestBuilder(String json) {
        Logger.info("JSON : %s ----" , json);
        jsonRequest = new Gson().fromJson(json, JsonRequest.class);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement paramsElement = jsonObject.get("params");

        JsonObject paramsObject = paramsElement.getAsJsonObject();

        jsonRequest.paramsMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : paramsObject.entrySet()) {
            String key = entry.getKey();
            JsonElement element = entry.getValue();
            String value = element.getAsString();
            Logger.info("===========> %s : %s", key, value);
            String[] values = new String[]{value};
            jsonRequest.paramsMap.put(key, values);
        }
    }

    public JsonRequestBuilder appCode(String appCode) {
        jsonRequest.appCode = appCode;
        return this;
    }

    public JsonRequestBuilder machineCode(String machineCode) {
        jsonRequest.machineCode = machineCode;
        return this;
    }

    public JsonRequestBuilder method(String method) {
        jsonRequest.method = method;
        return this;
    }

    public JsonRequestBuilder time(Long time) {
        jsonRequest.time = time;
        return this;
    }


    public JsonRequestBuilder setCurrentTime() {
        jsonRequest.time = new Date().getTime()/1000l;
        return this;
    }

    public JsonRequestBuilder checkValue(String checkValue) {
        jsonRequest.checkValue = checkValue;
        return this;
    }

    /**
     * 按指定Action的方式生成checkValue.
     * @param action
     * @return
     */
    public JsonRequestBuilder checkValue(ApiBaseAction action) {
        ApiContext context = jsonRequest.toContext();
        jsonRequest.checkValue = action.getCheckValue(context);
        return this;
    }
    public JsonRequestBuilder checkValue(ApiBase2Action action) {
        ApiContext context = jsonRequest.toContext();
        jsonRequest.checkValue = action.getCheckValue(context);
        return this;
    }

    public JsonRequestBuilder params(Map<String, Object> params) {
        jsonRequest.params = params;
        return this;
    }

    public JsonRequestBuilder addParam(String key, Object value) {
        if (jsonRequest.params == null) {
            jsonRequest.params = new HashMap<>();
        }
        jsonRequest.params.put(key, value);
        return this;
    }

    public JsonRequest build() {
        // 计算checkValue
        return jsonRequest;
    }
}
