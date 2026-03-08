package space.changle.lynnia.dto.outdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/7 11:04
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserCheckOutDto {

    private String status;

    private boolean exist;
}
