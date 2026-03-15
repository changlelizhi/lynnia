package space.changle.lynnia.web.controller;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.changle.lynnia.common.result.ApiResult;

import java.lang.management.LockInfo;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/15 12:19
 * @description
 */
@RestController
@RequestMapping("/api/tma/lock")
public class LockController {

    @GetMapping("/info")
    public ApiResult<LockInfo>
}
