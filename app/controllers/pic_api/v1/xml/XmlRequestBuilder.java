package controllers.pic_api.v1.xml;

import java.util.Date;
import java.util.Map;

/**
 * XmlRequestBuilder.
 */
public class XmlRequestBuilder {

    private XmlRequest xmlRequest;

    public XmlRequestBuilder() {
        xmlRequest = new XmlRequest();
    }

    public XmlRequestBuilder appCode(String appCode) {
        xmlRequest.appCode = appCode;
        return this;
    }

    public XmlRequestBuilder method(String method) {
        xmlRequest.method = method;
        return this;
    }

    public XmlRequestBuilder time(Long time) {
        xmlRequest.time = time;
        return this;
    }


    public XmlRequestBuilder setCurrentTime() {
        xmlRequest.time = new Date().getTime()/1000l;
        return this;
    }

    public XmlRequestBuilder checkValue(String checkValue) {
        xmlRequest.checkValue = checkValue;
        return this;
    }

    public XmlRequestBuilder params(Map<String, String[]> params) {
        xmlRequest.params = params;
        return this;
    }

    public XmlRequest build() {
        // 计算checkValue
        return xmlRequest;
    }
}
