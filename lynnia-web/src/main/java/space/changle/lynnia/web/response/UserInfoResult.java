package space.changle.lynnia.web.response;


import space.changle.lynnia.api.enums.IdentityType;
import space.changle.lynnia.api.enums.UserStatus;
import space.changle.lynnia.dto.outdto.UserInfoOutDto;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/5 15:25
 * @description
 */
public record UserInfoResult(
        String nickname,
        int reputation,
        int totalCheckinDays,
        int plusCount,
        int minusCount,
        int taskCount,
        String identityType,
        String timezone,
        String bio,
        String photoUrl,
        String status
) {

    public static UserInfoResult of(UserInfoOutDto userInfoOutDto){
        return new UserInfoResult(
                userInfoOutDto.getNickname(),
                userInfoOutDto.getReputation(),
                userInfoOutDto.getTotalCheckinDays(),
                userInfoOutDto.getPlusCount(),
                userInfoOutDto.getMinusCount(),
                userInfoOutDto.getTaskCount(),
                IdentityType.getById(userInfoOutDto.getIdentityType()),
                userInfoOutDto.getTimezone(),
                userInfoOutDto.getBio(),
                userInfoOutDto.getPhotoUrl(),
                UserStatus.getByCode(userInfoOutDto.getStatus())
        );
    }

}
