package space.changle.lynnia.web.response;

import space.changle.lynnia.dto.outdto.CodeOutDto;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/7 20:07
 * @description
 */
public record CodeResult (String captcha, long expireTime) {

    public static CodeResult of(CodeOutDto codeOutDto) {
        return new CodeResult(codeOutDto.getCode(), codeOutDto.getExpireTime());
    }
}
