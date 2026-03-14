package space.changle.lynnia.dto.outdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/10 00:27
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CheckinOutDto {

    //是否签到

    private boolean isCheckined;

    private List<LocalDate> history;

}
