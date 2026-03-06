package space.changle.lynnia.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import space.changle.lynnia.api.AuthApi;
import space.changle.lynnia.api.UserApi;
import space.changle.lynnia.common.result.ApiResult;
import space.changle.lynnia.dto.outdto.WebTokenOutDto;
import space.changle.lynnia.web.request.WebLoginRequest;


/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/2/28 22:00
 * @description
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthApi authApi;

    private final UserApi userApi;

    public AuthController(AuthApi authApi, UserApi userApi) {
        this.authApi = authApi;
        this.userApi = userApi;
    }

    /**
     * 用户注册
     *
     * @param initData
     * @return
     */
    @PostMapping("/register")
    public ApiResult<Void> register(@RequestHeader(value = "X-TMA-Init-Data", required = false) String initData) {
        userApi.registerUser(initData);
        return ApiResult.success();
    }

    @PostMapping("/tmalogin")
    public ApiResult<String> tgLogin(@RequestHeader(value = "X-TMA-Init-Data",required = false) String initData  ) {
        String tmaedLogin = authApi.tmaLogin(initData);
        return ApiResult.success(tmaedLogin);
    }

    @PostMapping("/weblogin")
    public ApiResult<String> webLogin(@Valid @RequestBody WebLoginRequest webLoginRequest , HttpServletResponse response) {
        String userId = webLoginRequest.userId();
        String code = webLoginRequest.code();
        WebTokenOutDto webTokenOutDto = authApi.webLogin(userId, code);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", webTokenOutDto.getRefreshToken())
                .httpOnly( true)
                .secure(true)
                .path("/")
                .maxAge(604800)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return ApiResult.success(webTokenOutDto.getAccessToken());
    }

}
