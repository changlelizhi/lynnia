package space.changle.lynnia.repo.mapper;

import org.apache.ibatis.annotations.Param;
import space.changle.lynnia.repo.entity.UserProfile;

import java.time.OffsetDateTime;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/4 17:39
 * @description
 */
public interface UserProfileMapper {

    int insertProfile(UserProfile userProfile);

    UserProfile selectByUserId(String userId);

    int updateBio( @Param("userId") String userId, @Param("bio") String bio,@Param("updateTime") OffsetDateTime updateTime);

    int updateProfile(@Param("userId") String userId, @Param("nickname") String nickname, @Param("photoUrl") String photoUrl, @Param("updateTime") OffsetDateTime updateTime);

    int updateReputation (@Param("userId") String userId, @Param("reputation") int reputation, @Param("updateTime") OffsetDateTime updateTime);

    int updateTimeZone(@Param("userId")String userId, @Param("timezone") String timezone,@Param("updateTime")  OffsetDateTime updateTime);

    String selectUserTimeZone(@Param("userId") String userId);

    int updateUserIdentity(@Param("userId")String userId, @Param("identityType") int identityType, @Param("updateTime")  OffsetDateTime updateTime);
}
