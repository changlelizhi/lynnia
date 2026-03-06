package space.changle.lynnia.service.enums;

import lombok.Getter;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.LynniaException;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/4 17:03
 * @description
 */
@Getter
public enum UserStatus {

    NORMAL(1, "正常"),

    FROZEN(2, "冻结"),

    BANNED(3, "封禁");

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
}
