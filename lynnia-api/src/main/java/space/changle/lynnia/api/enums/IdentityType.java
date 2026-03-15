package space.changle.lynnia.api.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.LynniaException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/4 18:35
 * @description 用户身份枚举
 */
@Slf4j
@Getter
public enum IdentityType {

    UNSELECTED(0, "未选择"),
    DOM(1, "Dom"),
    SUB(2, "Sub"),
    SWITCH(3, "Switch");

    private final int code;

    private final String desc;

    // 静态缓存：desc -> code
    private static final Map<String, Integer> DESC_TO_CODE = new HashMap<>();

    static {
        for (IdentityType type : IdentityType.values()) {
            DESC_TO_CODE.put(type.desc, type.code);
        }
    }

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

    //根据desc获取code
    public static int getByDesc(String desc) {
        for (IdentityType value : values()) {
            if (value.desc.equals(desc)) {
                return value.code;
            }
        }
        throw new LynniaException(Result.USER_IDENTITY_UNKNOWN);
    }

    public static int getCodeByDesc(String desc) {
        if (desc == null) {
            throw new LynniaException(Result.USER_IDENTITY_UNKNOWN);
        }
        Integer code = DESC_TO_CODE.get(desc);
        if (code == null) {
            throw new LynniaException(Result.USER_IDENTITY_UNKNOWN);
        }
        return code;
    }
}
