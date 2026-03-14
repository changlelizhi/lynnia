package space.changle.lynnia.web.response;

import space.changle.lynnia.dto.outdto.LoginOutDto;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/2/28 22:44
 * @description
 */
public record LoginResult(String accessToken) {

    public static LoginResult of(LoginOutDto loginOutDto) {
        return new LoginResult(loginOutDto.getToken());
    }

}
