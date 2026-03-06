package space.changle.lynnia.web.request;

import jakarta.validation.constraints.NotBlank;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/6 14:20
 * @description 登录请求
 */
public record WebLoginRequest (@NotBlank(message = "用户ID不能为空")  String userId, @NotBlank(message = "验证码不能为空") String code){


}
