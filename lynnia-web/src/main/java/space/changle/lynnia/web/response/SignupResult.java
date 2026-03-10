package space.changle.lynnia.web.response;

import space.changle.lynnia.dto.outdto.SignupOutDto;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/10 20:59
 * @description
 */
public record SignupResult(boolean isSignup, String userId) {

    public static SignupResult of(SignupOutDto signupOutDto){
        return new SignupResult(signupOutDto.isSignUpResult(), signupOutDto.getUserId());
    }
}
