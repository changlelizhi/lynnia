package space.changle.lynnia.api;

import space.changle.lynnia.dto.outdto.WebTokenOutDto;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/2/28 21:48
 * @description 认证接口
 */
public interface AuthApi {

    /**
     * tma登录
     * @param initData tma初始化数据
     * @return 登录结果
     */
    String tmaLogin(String initData);

    /**
     * web登录
     * @param userId 用户id
     * @param code 验证码
     * @return 登录结果
     */
    WebTokenOutDto webLogin(String userId, String code);
}
