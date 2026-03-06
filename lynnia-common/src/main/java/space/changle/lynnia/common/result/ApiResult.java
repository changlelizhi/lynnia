package space.changle.lynnia.common.result;

import lombok.Getter;
import space.changle.lynnia.common.enums.Result;

import java.time.Instant;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/2/28 22:32
 * @description
 */
@Getter
public class ApiResult<T> {

    private int code;

    private String message;

    private T data;

    private final long timestamp;

    private ApiResult() {
        this.timestamp = Instant.now().getEpochSecond();
    }


    private ApiResult(Result result, T data) {
        this();
        this.code = result.getCode();
        this.message = result.getMessage();
        this.data = data;
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<>(Result.SUCCESS, null);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(Result.SUCCESS, data);
    }

    public static <T> ApiResult<T> error(Result result) {
        return new ApiResult<>(result, null);
    }
    public static <T> ApiResult<T> error(Result result, T data) {
        return new ApiResult<>(result, data);
    }
}
