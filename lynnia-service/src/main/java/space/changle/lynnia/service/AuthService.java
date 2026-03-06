package space.changle.lynnia.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import space.changle.lynnia.api.AuthApi;
import space.changle.lynnia.api.UserApi;
import space.changle.lynnia.common.enums.LoginType;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.LynniaException;
import space.changle.lynnia.dto.outdto.WebTokenOutDto;
import space.changle.lynnia.security.token.JwtTokenProvider;
import space.changle.lynnia.service.constant.RedisKey;
import space.changle.lynnia.security.auth.TelegramAuth;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/2 21:25
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements AuthApi {

    private final JwtTokenProvider jwtTokenProvider;

    private final TelegramAuth telegramAuth;

    private final UserApi userApi;

    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public String tmaLogin(String initData) {
        if (StringUtils.isBlank(initData)) {
            throw new LynniaException(Result.INIT_DATA_FAIL);
        }
        boolean valid = telegramAuth.isValid(initData);
        if (!valid) {
            throw new LynniaException(Result.AUTH_TELEGRAM_INVALID);
        }
        String userId = telegramAuth.getUser(initData).getId();
        boolean userExist = userApi.isUserExist(userId);
        if (userExist) {
            return jwtTokenProvider.generateAccessToken(userId, LoginType.TMA);
        }
        throw new LynniaException(Result.USER_NOT_EXIST);
    }

    @Override
    public WebTokenOutDto webLogin(String userId, String code) {
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(code)) {
            throw new LynniaException(Result.USER_ID_OR_CODE_EMPTY);
        }
        if (!userApi.isUserExist(userId)) {
            throw new LynniaException(Result.USER_NOT_EXIST);
        }
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisKey.loginCode(userId));
        if (!code.equals(cacheCode)) {
            throw new LynniaException(Result.CODE_INVALID);
        }
        String accessToken = jwtTokenProvider.generateAccessToken(userId, LoginType.WEB);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, LoginType.WEB);
        return WebTokenOutDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
