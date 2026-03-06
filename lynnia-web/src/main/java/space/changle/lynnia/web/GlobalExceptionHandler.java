package space.changle.lynnia.web;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.LynniaException;
import space.changle.lynnia.common.result.ApiResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/3 19:40
 * @description
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LynniaException.class)
    public ApiResult<Result> handleLingBotException(LynniaException ex) {
        return ApiResult.error(ex.getResult());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ApiResult.error(Result.FAIL, errors);
    }
}
