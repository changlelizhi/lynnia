package space.changle.lynnia.service.convert;

import space.changle.lynnia.dto.outdto.UserInfoOutDto;
import space.changle.lynnia.repo.entity.UserProfile;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/14 19:31
 * @description
 */
public class UserConvertToDTO {

    public static UserInfoOutDto  convertToDTO(UserProfile userProfile,int totalCheckinDays ,int userStatus){
        return UserInfoOutDto.builder()
                .nickname(userProfile.getNickname())
                .reputation(userProfile.getReputation())
                .totalCheckinDays(totalCheckinDays)
                .plusCount(userProfile.getPlusCount())
                .minusCount(userProfile.getMinusCount())
                .taskCount(userProfile.getTaskCount())
                .identityType(userProfile.getIdentityType())
                .timezone(userProfile.getTimezone())
                .bio(userProfile.getBio())
                .photoUrl(userProfile.getPhotoUrl())
                .status(userStatus)
                .build();

    }
}
