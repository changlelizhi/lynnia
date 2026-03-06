package space.changle.lynnia.common.exception;

import lombok.Getter;
import space.changle.lynnia.common.enums.Result;


/**
 * @author 长乐
 * @version 1.0
 * @date 2026/2/15 15:42
 * @description
 */
@Getter
public class LynniaException extends RuntimeException{

    private final Result result;

    public LynniaException(Result result) {
        super(result.getMessage());
        this.result = result;
    }

}
