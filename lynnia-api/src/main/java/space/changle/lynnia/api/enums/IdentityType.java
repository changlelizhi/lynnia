package space.changle.lynnia.api.enums;

import lombok.Getter;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.LynniaException;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/4 18:35
 * @description 用户身份枚举
 */
@Getter
public enum IdentityType {

    UNSELECTED(0, "未选择"),
    DOM(1, "Dom"),
    SUB(2, "Sub"),
    SWITCH(3, "Switch");

    private final int code;

    private final String desc;

    IdentityType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static String getById(int code) {
        for (IdentityType value : values()) {
            if (value.code == code) {
                return value.desc;
            }
        }
        throw new LynniaException(Result.USER_IDENTITY_UNKNOWN);
    }
}
