package controllers.pic_api.v1.xml;

/**
 * XML响应类.
 */
public class XmlResponse {

    /**
     * 结果数据对象.
     */
    public Object data;

    /**
     * 错误码。
     * 为0表示调用成功.
     */
    public Integer code;

    /**
     * 错误消息.
     */
    public String message;

    /**
     * 最后数值版本.
     * 如果是展示型结果集，可能会返回一个lastVersion，用于客户端缓存.
     */
    public Long lastVersion;

}
