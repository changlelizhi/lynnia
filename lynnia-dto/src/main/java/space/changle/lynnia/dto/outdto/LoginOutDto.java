package space.changle.lynnia.dto.outdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/11 16:55
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LoginOutDto {

    private String token;

}
