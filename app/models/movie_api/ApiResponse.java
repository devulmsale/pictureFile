package models.movie_api;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 基本响应内容.
 */
@XStreamAlias("response")
public class ApiResponse {

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
