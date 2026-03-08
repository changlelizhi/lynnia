package space.changle.lynnia.service.constant;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/6 14:46
 * @description
 */
public final class RedisKey {

    private RedisKey() {}

    // 正常用户 ID 集合（已注册且未封禁）
    public static final String USER_NORMAL_SET = "user:normal:set";

    // 封禁用户集合
    public static final String USER_BAN_SET = "user:ban:set";


    // 登录验证码
    public static final String USER_LOGIN_CODE_PREFIX = "user:login:code:";

    public static String loginCode(String userId) {
        return USER_LOGIN_CODE_PREFIX + userId;
    }
}
