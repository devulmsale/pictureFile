package models.movie_api;

/**
 * 调用结果.
 */
public enum ApiResultCode {
    // 系统级异常
    G304(304, "Not Modified"),
    G400(400, "错误的请求参数或报文解析失败"),
    G401(401, "认证失败"),
    G404(404, "请求方法名不存在"),
    G500(500, "服务器内部错"),

    // 用户类接口 - 登录 LoginAction
    USER_PHONE_REGISTERED(10101, "手机号码已经注册"),
    USER_PHONE_INVALID(10102, "手机号码不合法"),
    USER_LOGIN_MACHINE_INVALID(10301, "此用户不是当前手机登录，无法注销"),
    USER_LOGOUT(10302, "此用户已经注销"),
    USER_NOT_EXISTS(10201, "您的帐号已在其他电脑登录，请重新登录系统"),
    USER_LOCK(10202, "用户已被锁定，不能登录，请联系客服"),
    USER_PASSWORD_ERROR(10203, "您输入的帐号或密码错误，不能登录，请联系客服"),

    ORDER_SEAT_SALED(50101, "锁定位置失败，选定座位已经出售，请重新选择"),
    ORDER_NOT_EXISTS(50201, "订单号不存在"),
    BAD_MOVIE_ORDER(50202, "无效的电影订单"),
    BAD_GEN_ORDER(50203, "生成订单失败"),
    INVALID_PLAY_PLAN(50204, "指定场次不存在"),
    INVALID_SALE_PRICE(50205, "分销单价低于成本价"),
    RESALER_ORDER_EXISTS(50206, "锁座订单号在一天内使用过，不能锁定位置"),

    LIKE_PROMOTION_REPEATEDLY(20701, "已经关注此活动，不能重复关注"),
    NEVER_LIKE_PROMOTION(20702, "没有关注此活动"),

    JOIN_PROMOTIION_REPEATEDLY(20801, "已经参加过此活动，不能重复参加"),
    NEVER_JOIN_PROMOTION(20802, "没有参加过此活动"),

    LIKE_CINEMA_REPEATEDLY(30801, "已经关注此影院，不能重复关注"),
    NEVER_LIKE_CINEMA(30802, "没有关注此影院"),

    LIKE_FILM_REPEATEDLY(31201, "已经关注此电影，不能重复关注"),
    NEVER_LIKE_FILM(31202, "没有关注此电影"),

    INVALID_CINEMA(20101, "无效的影院"),
    INVALID_PLAYPLAN(20102, "无效的排片"),
    INVALID_CARDCOUPON(20103, "无效的卡券"),
    USED_CARDCOUPON(20104, "卡券已使用"),
    UNACTIVED_CARDCOUPON(20105, "卡券未激活"),
    CANCLED_CARDCOUPON(20106, "卡券已被注销"),
    INVALID_TOKEN(20107, "无效的优惠券使用Token"),

    TICKET_UNAVAILABLE(80101, "取票券不可用"),
    TICKET_HAD_PRINTED(80102, "取票券已经打印"),
    TICKET_TOKEN_EXPIRED(80103, "取票操作已经过期"),

    KEEP_SEAT_CINEMA(90101 , "不能预留其他影院座位"),
    APPLIER_SEAT_CINEMA(90103 , "不能退其他影院售票"),
    KEEP_SEAT_COUNT(90102, "预留座位数量大于场次设定数量"),



    MERCHANT_CODE_ERROR(12001 , "code 不存在或已删除"),
    MERCHANT_USER_OR_PASSWORD_ERROR(12002 , "用户名或密码错误"),
    MERCHANTUSER_MERCHANT_DIFF(12003 , "登录账号所属商户跟code商户不一致"),


    SUCCESS(0, "成功");

    private Integer code;
    private String  message;

    ApiResultCode(Integer _code, String _message) {
        code = _code;
        message = _message;
    }

    public String toString() {
        return code + ":" + message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
