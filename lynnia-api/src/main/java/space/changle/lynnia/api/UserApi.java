package space.changle.lynnia.api;

import space.changle.lynnia.dto.outdto.*;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/2/28 21:48
 * @description 用户接口
 */
public interface UserApi {

    /**
     * 是否是正常用户
     *
     * @param userId 用户id
     * @return 是否是正常用户
     */
    boolean isNormalUser(String userId);

    /**
     * 是否是封禁用户
     *
     * @param userId 用户id
     * @return 是否是封禁用户
     */
    boolean isBanUser(String userId);

    /**
     * 断言是否是正常用户
     *
     * @param userId 用户id
     */
    void  assertNormalUser(String userId);

    /**
     * 注册用户
     *
     * @param initData 初始化数据
     */
    SignupOutDto registerUser(String initData);

    /**
     * 查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    UserInfoOutDto queryUserInfo(String userId);

    /**
     * 保存用户信息
     *
     * @param userId   用户id
     * @param initData 初始化数据
     */
    void saveProfile(String userId, String initData);

    /**
     * 保存用户简介
     *
     * @param userId 用户id
     * @param bio    简介
     */
    void saveUserBio(String userId, String bio);

    /**
     * 发送验证码
     *
     * @param userId 用户id
     * @return 验证码
     */
    CodeOutDto sendVerificationCode(String userId);

    /**
     * 验证用户
     *
     * @param initData 初始化数据
     */
    UserCheckOutDto checkUser(String initData);

    /**
     * 签到状态和历史
     *
     * @param userId 用户id
     * @return 签到状态和历史
     */
    CheckinOutDto checkinStatusAndHistory(String userId);

    /**
     * 用户签到
     *
     * @param userId 用户id
     */
    void userCheckin(String userId);
}
