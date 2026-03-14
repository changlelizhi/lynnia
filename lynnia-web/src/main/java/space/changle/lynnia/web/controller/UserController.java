package space.changle.lynnia.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import space.changle.lynnia.api.UserApi;
import space.changle.lynnia.common.result.ApiResult;
import space.changle.lynnia.dto.outdto.CheckinOutDto;
import space.changle.lynnia.dto.outdto.CodeOutDto;
import space.changle.lynnia.dto.outdto.UserInfoOutDto;
import space.changle.lynnia.web.request.UserBioRequest;
import space.changle.lynnia.web.response.CodeResult;
import space.changle.lynnia.web.response.CheckinResult;
import space.changle.lynnia.web.response.UserInfoResult;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/2 12:45
 * @description
 */
@RestController
@RequestMapping("/api/tma/user")
@RequiredArgsConstructor
public class UserController {

    private final UserApi userApi;

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/info")
    public ApiResult<UserInfoResult> info(@AuthenticationPrincipal String userId) {
        UserInfoOutDto userInfoOutDto = userApi.queryUserInfo(userId);
        UserInfoResult userInfoResult = UserInfoResult.of(userInfoOutDto);
        return ApiResult.success(userInfoResult);
    }

    /**
     * 保存用户简介
     *
     * @param userId
     * @param userBioRequest
     * @return
     */
    @PostMapping("/savebio")
    public ApiResult<Void> saveBio(@AuthenticationPrincipal String userId, @Valid @RequestBody UserBioRequest userBioRequest) {
        userApi.saveUserBio(userId, userBioRequest.bio());
        return ApiResult.success();
    }

    /**
     * 更新用户信息
     *
     * @param userId
     * @param initData
     * @return
     */
    @PostMapping("/profile")
    public ApiResult<Void> updateProfile(@AuthenticationPrincipal String userId, @RequestHeader(value = "X-TMA-InitData", required = false) String initData) {
        userApi.saveProfile(userId, initData);
        return ApiResult.success();
    }

    /**
     * 发送登录验证码
     *
     * @param userId
     * @return
     */
    @GetMapping("/code")
    public ApiResult<CodeResult> sendLoginCode(@AuthenticationPrincipal String userId) {
        CodeOutDto codeOutDto = userApi.sendVerificationCode(userId);
        return ApiResult.success(CodeResult.of(codeOutDto));
    }

    //签到
    @PostMapping("/checkin")
    public ApiResult<Void> checkin(@AuthenticationPrincipal String userId) {
        userApi.userCheckin(userId);
        return ApiResult.success();
    }


    // 签到信息
    @GetMapping("/checkinInfo")
    public ApiResult<CheckinResult> checkinInfo(@AuthenticationPrincipal String userId) {
        CheckinOutDto checkinOutDto = userApi.checkinStatusAndHistory(userId);
        return ApiResult.success(  CheckinResult.of(checkinOutDto));
    }


}
