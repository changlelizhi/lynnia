package space.changle.lynnia.service.constant;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/6 14:46
 * @description
 */
public final class RedisKey {

    private RedisKey() {}

    public static final String USER_ID_SET = "user:ids";

    public static final String USER_LOGIN_CODE_PREFIX = "user:login:code:";

    public static String loginCode(String userId) {
        return USER_LOGIN_CODE_PREFIX + userId;
    }
}
