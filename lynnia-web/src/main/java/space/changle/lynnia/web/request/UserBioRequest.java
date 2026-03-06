package space.changle.lynnia.web.request;

import jakarta.validation.constraints.Size;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/5 19:45
 * @description
 */
public record UserBioRequest(@Size(max = 150,message = "个人简介过长") String bio) {
}
