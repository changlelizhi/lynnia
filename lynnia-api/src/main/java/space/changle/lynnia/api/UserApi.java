package space.changle.lynnia.api;

import space.changle.lynnia.dto.outdto.UserInfoOutDto;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/2/28 21:48
 * @description 用户接口
 */
public interface UserApi {

    /**
     * 判断用户是否存在
     * @param userId 用户id
     * @return true:存在 false:不存在
     */
    boolean isUserExist(String userId);

    /**
     * 注册用户
     * @param initData 初始化数据
     */
    void registerUser(String initData);

    /**
     * 查询用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    UserInfoOutDto queryUserInfo(String userId);

    /**
     * 保存用户信息
     * @param userId 用户id
     * @param initData  初始化数据
     */
    void saveProfile(String userId, String initData);

    /**
     * 保存用户简介
     * @param userId 用户id
     * @param bio 简介
     */
    void saveUserBio(String userId,  String bio);

    /**
     * 发送验证码
     * @param userId 用户id
     * @return 验证码
     */
    String sendVerificationCode(String userId);
}
