package space.changle.lynnia.dto.outdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/10 20:49
 * @description 注册结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SignupOutDto {

    private boolean signUpResult;

    private String userId;

}
