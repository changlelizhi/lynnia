package space.changle.lynnia.api.enums;

import lombok.Getter;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.LynniaException;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/4 17:03
 * @description 用户状态枚举
 */
@Getter
public enum UserStatus {

    NOT_EXIST(0, "不存在"),

    NORMAL(1, "正常"),

    BANNED(2, "封禁");

    private final int code;

    private final String desc;

    UserStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getByCode(int code) {
        for (UserStatus value : values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
       throw new LynniaException(Result.USER_STATUS_UNKNOWN);
    }

    public static UserStatus fromCode(int code) {
        for (UserStatus value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new LynniaException(Result.USER_STATUS_UNKNOWN);
    }
}
