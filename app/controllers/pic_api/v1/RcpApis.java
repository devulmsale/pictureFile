package controllers.pic_api.v1;

import cache.CacheHelper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.pic_api.framework.ApiBase2Action;
import controllers.pic_api.framework.ApiBaseAction;
import controllers.pic_api.framework.ApiContext;
import controllers.pic_api.v1.json.JsonRequest;
import controllers.pic_api.v1.json.JsonRequestBuilder;
import controllers.pic_api.v1.json.JsonResponse;
import controllers.pic_api.v1.json.JsonResponseBuilder;
import models.movie_api.ApiResultCode;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.libs.IO;
import play.mvc.After;
import play.mvc.Controller;
import util.extension.ExtensionInvoker;
import util.extension.ExtensionResult;
import util.transaction.RedisLock;

import java.text.DateFormat;

/**
 * RCP接口v1.0
 * 使用json提供服务，供富客户端使用.
 */
public class RcpApis extends Controller {

    public static void execute() {
        String body = IO.readContentAsString(request.body);

        Logger.info("body : %s  — " ,  body);
        if (StringUtils.isBlank(body)) {
            JsonResponse jsonResponse = new JsonResponseBuilder(ApiResultCode.G400).build();
            renderJsonResponse(jsonResponse);
        }

        Logger.info("requestBody 2=" + body);

        JsonRequest jsonRequest = new JsonRequestBuilder(body).build();

        Logger.info("jsonRequest=" + jsonRequest);

        ApiContext context = jsonRequest.toContext();
        Logger.info("context=" + context);

        /**
         * 调用ApiBaseAction的所有子类进行方法匹配调用。
         */
        ExtensionResult extensionResult = ExtensionInvoker.run(ApiBaseAction.class, context);

        if (extensionResult.code < 0) {
            Logger.info("=================== 没有找到ApiBaseAction实现，尝试查找ApiBase2Action实现 ====================");
            extensionResult = ExtensionInvoker.run(ApiBase2Action.class, context);
            if (extensionResult.code < 0) {
                // 没有对应的方法
                JsonResponse jsonResponse = new JsonResponseBuilder(ApiResultCode.G404).build();
                renderJsonResponse(jsonResponse);
            }
        }

        JsonResponse jsonResponse = new JsonResponseBuilder().fromContext(context).withData(context.data).build();

        renderJsonResponse(jsonResponse);
    }

    private static void renderJsonResponse(JsonResponse jsonResponse) {
        Gson gson = new GsonBuilder()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setPrettyPrinting()
                .create();

        String responseText = gson.toJson(jsonResponse);
        //Logger.info("responseText = " + responseText);
        //renderJSON(jsonResponse);
        renderJSON(responseText);
    }

    @After
    public static void unlockAll() {
        RedisLock.unlockAll();
    }

    @After
    public static void cleanCacheHelper() {
        CacheHelper.cleanPreRead();
    }
}
