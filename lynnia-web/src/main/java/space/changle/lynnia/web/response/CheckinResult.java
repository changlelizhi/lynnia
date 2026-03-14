package space.changle.lynnia.web.response;


import space.changle.lynnia.dto.outdto.CheckinOutDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/9 18:14
 * @description
 */
public record CheckinResult(boolean isCheckined, List<String> history) {


    public static CheckinResult of(CheckinOutDto checkinOutDto){
        List<String> history = Optional.ofNullable(checkinOutDto.getHistory())
                .orElse(List.of())
                .stream()
                .map(LocalDate::toString)
                .toList();
        return new CheckinResult(
                checkinOutDto.isCheckined(),
                history
        );
    }
}
