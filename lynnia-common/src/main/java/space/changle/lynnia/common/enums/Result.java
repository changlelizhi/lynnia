package space.changle.lynnia.common.enums;

import lombok.Getter;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/2/28 21:42
 * @description
 */
@Getter
public enum Result {

    SUCCESS(20000,"成功"),

    FAIL(1,"失败"),

    INIT_DATA_FAIL(10000,"初始化数据失败"),

    AUTH_TELEGRAM_INVALID(10001,"签名无效"),

    AUTH_INIT_MISSING_PARAM(10002,"缺少初始化数据参数"),

    USER_NOT_EXIST(30000,"用户不存在,请先注册"),

    USER_EXIST(30001,"用户已存在,请勿重新注册"),

    USER_BAN(30002,"用户被封禁"),

    USER_STATUS_UNKNOWN(30003,"未知的用户状态"),

    USER_IDENTITY_UNKNOWN(30004,"未知的身份"),

    USER_IDENTITY_UPDATE_FAIL(30005,"身份更新失败"),

    BIO_TOO_LONG(30006,"个人简介过长" ),

    USER_SAVE_BIO_FAIL(30007,"保存简介失败"),

    USER_CHECKIN_TODAY(30008,"今天已签到"),

    TIME_ZONE_INVALID(30009,"时区无效"),

    USER_NOT_SAME(30010,"不是同一个用户"),

    USER_ID_OR_CODE_EMPTY(30011,"用户id或验证码为空"),

    CODE_INVALID(30012,"验证码无效"),

    USER_TIME_ZONE_CHANGE_FAIL(30013,"切换时区失败"),

    UNAUTHORIZED(40100, "未授权"),

    TOKEN_EXPIRED(40101, "令牌已过期"),

    TOKEN_INVALID(40102, "令牌无效"),

    ACCESS_DENIED(40303, "拒绝访问"),

    REFRESH_TOKEN_INVALID(40103, "刷新令牌无效"),


    SYSTEM_ERROR(99999, "系统错误"),
   ;

    private final int code;

    private final String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
