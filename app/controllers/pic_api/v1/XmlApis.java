package controllers.pic_api.v1;

import cache.CacheHelper;
import com.thoughtworks.xstream.XStream;
import controllers.pic_api.v1.xml.ApiResponseBuilder;
import controllers.pic_api.v1.xml.XmlRequest;
import controllers.pic_api.v1.xml.XmlRequestBuilder;
import models.movie_api.ApiResponse;
import models.movie_api.ApiResultCode;
import models.movie_api.resale.framework.ResaleApiBaseAction;
import models.movie_api.resale.framework.ResaleApiContext;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.mvc.After;
import play.mvc.Controller;
import util.extension.ExtensionInvoker;
import util.extension.ExtensionResult;
import util.transaction.RedisLock;

/**
 * 基于XML请求的服务接口，用于对接下游厂商.
 */
public class XmlApis extends Controller {

    public static void execute(String method, String appCode, Long time, String checkValue) {

        if (StringUtils.isBlank(method)) {
            ApiResponse apiResponse = new ApiResponseBuilder(ApiResultCode.G400).build();
            renderXmlResponse(apiResponse);
        }

        XmlRequestBuilder builder = new XmlRequestBuilder();
        builder.method(method).appCode(appCode).time(time).checkValue(checkValue).params(params.all());
        XmlRequest xmlRequest = builder.build();

        Logger.info("xmlRequest=" + xmlRequest);

        ResaleApiContext context = xmlRequest.toContext();
        Logger.info("context=" + context);

        /**
         * 调用ApiBaseAction的所有子类进行方法匹配调用。
         */
        ExtensionResult extensionResult = ExtensionInvoker.run(ResaleApiBaseAction.class, context);

        Logger.info("result.code=" + extensionResult.code + ", message=" + extensionResult.message);

        if (extensionResult.code < 0) {
            // 没有对应的方法
            ApiResponse apiResponse = new ApiResponseBuilder(ApiResultCode.G404).build();
            renderXmlResponse(apiResponse);
        }

        ApiResponse apiResponse = new ApiResponseBuilder(context).build();

        renderXmlResponse(apiResponse);
    }

    protected static void renderXmlResponse(ApiResponse apiResponse) {
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        try {
            String xml = xStream.toXML(apiResponse);
            //Logger.info("generate xml=" + xml);
            renderXml(xml);
        } catch (Exception e) {
            Logger.error(e, "XML解包失败.");
        }
    }

    @After
    public static void unlockAll() {
        RedisLock.unlockAll();
    }

    @After
    public static void cleanCacheHelper() {
        CacheHelper.cleanPreRead();
    }

    public static XmlRequest getXmlRequest(String method) {
        XmlRequestBuilder builder = new XmlRequestBuilder();
        builder.method(method);
        return builder.build();
    }
}
