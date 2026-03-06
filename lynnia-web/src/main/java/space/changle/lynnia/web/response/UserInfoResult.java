package space.changle.lynnia.web.response;


import space.changle.lynnia.dto.outdto.UserInfoOutDto;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/5 15:25
 * @description
 */
public record UserInfoResult(
        Profile profile,
        Growth growth
) {

    public record Profile(
            String bio,
            String photoUrl,
            String identityType,
            String timezone
    ) {}

    public record Growth(
            int reputation,
            int totalCheckinDays,
            int streakDays
    ) {}

    public static UserInfoResult of(UserInfoOutDto outDto) {
        return new UserInfoResult(
                new Profile(
                        outDto.getBio(),
                        outDto.getPhotoUrl(),
                        outDto.getIdentityType(),
                        outDto.getTimezone()
                ),
                new Growth(
                        outDto.getReputation(),
                        outDto.getTotalCheckinDays(),
                        outDto.getStreakDays()
                )
        );
    }


}
