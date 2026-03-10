package space.changle.lynnia.web.response;

import space.changle.lynnia.dto.outdto.SignOutDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/9 18:14
 * @description
 */
public record SignResult(boolean isSigned, List<String> history) {


    public static SignResult of(SignOutDto signOutDto){
        List<String> history = Optional.ofNullable(signOutDto.getHistory())
                .orElse(List.of())
                .stream()
                .map(LocalDate::toString)
                .toList();
        return new SignResult(
                signOutDto.isSigned(),
                history
        );
    }
}
