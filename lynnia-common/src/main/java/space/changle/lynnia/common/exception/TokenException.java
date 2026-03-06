package space.changle.lynnia.common.exception;

import lombok.Getter;
import space.changle.lynnia.common.enums.Result;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/1 14:22
 * @description
 */
@Getter
public class TokenException extends RuntimeException{

    private final Result result;

    public TokenException(Result result) {
        super(result.getMessage());
        this.result = result;
    }

}
