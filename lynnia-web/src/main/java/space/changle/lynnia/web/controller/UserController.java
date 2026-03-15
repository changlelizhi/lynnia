package space.changle.lynnia.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import space.changle.lynnia.api.UserApi;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.LynniaException;
import space.changle.lynnia.common.result.ApiResult;
import space.changle.lynnia.dto.outdto.CheckinOutDto;
import space.changle.lynnia.dto.outdto.CodeOutDto;
import space.changle.lynnia.dto.outdto.UserInfoOutDto;
import space.changle.lynnia.web.request.TimezoneRequest;
import space.changle.lynnia.web.request.UserBioRequest;
import space.changle.lynnia.web.request.UserIdentityRequest;
import space.changle.lynnia.web.response.CodeResult;
import space.changle.lynnia.web.response.CheckinResult;
import space.changle.lynnia.web.response.UserInfoResult;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/2 12:45
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/api/tma/user")
@RequiredArgsConstructor
public class UserController {

    private final UserApi userApi;

    //获取用户信息
    @GetMapping("/info")
    public ApiResult<UserInfoResult> info(@AuthenticationPrincipal String userId) {
        UserInfoOutDto userInfoOutDto = userApi.queryUserInfo(userId);
        UserInfoResult userInfoResult = UserInfoResult.of(userInfoOutDto);
        return ApiResult.success(userInfoResult);
    }

    //更新用户信息
    @PostMapping("/profile")
    public ApiResult<Void> updateProfile(@AuthenticationPrincipal String userId, @RequestHeader(value = "X-TMA-InitData", required = false) String initData) {
        userApi.saveProfile(userId, initData);
        return ApiResult.success();
    }

    //获取验证码
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
        return ApiResult.success(CheckinResult.of(checkinOutDto));
    }

    // 更新时区
    @PutMapping("/timezone")
    public ApiResult<Void> updateTimezone(@AuthenticationPrincipal String userId, @RequestBody TimezoneRequest timezoneRequest) {

         userApi.switchTimeZones(userId, timezoneRequest.tmaUserId(), timezoneRequest.timezone());

        return ApiResult.success();
    }

    // 保存用户简介
    @PutMapping("/saveBio")
    public ApiResult<Void> saveBio(@AuthenticationPrincipal String userId, @RequestBody UserBioRequest  userBioRequest) {
        log.info("bio: {}", userBioRequest);
        userApi.saveUserBio(userId, userBioRequest.bio());
        return ApiResult.success();
    }

    // 保存用户身份
    @PutMapping("/identity")
    public ApiResult<Void> saveIdentity(@AuthenticationPrincipal String userId, @RequestBody UserIdentityRequest userIdentityRequest) {
        userApi.renewIdentity(userId, userIdentityRequest.identity());
        return ApiResult.success();
    }


}
