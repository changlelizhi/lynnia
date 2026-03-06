package space.changle.lynnia.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import space.changle.lynnia.api.UserApi;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.LynniaException;
import space.changle.lynnia.common.result.TmaUser;
import space.changle.lynnia.common.util.UserUtils;
import space.changle.lynnia.dto.outdto.UserInfoOutDto;
import space.changle.lynnia.repo.entity.UserAccount;
import space.changle.lynnia.repo.entity.UserGrowth;
import space.changle.lynnia.repo.entity.UserProfile;
import space.changle.lynnia.repo.mapper.UserAccountMapper;
import space.changle.lynnia.repo.mapper.UserGrowthMapper;
import space.changle.lynnia.repo.mapper.UserProfileMapper;
import space.changle.lynnia.service.constant.RedisKey;
import space.changle.lynnia.security.auth.TelegramAuth;
import space.changle.lynnia.service.constant.UserConstant;
import space.changle.lynnia.service.enums.IdentityType;
import space.changle.lynnia.service.enums.UserStatus;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/3 21:55
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserApi {

    private final TelegramAuth telegramAuth;

    private final UserAccountMapper userAccountMapper;

    private final UserProfileMapper userProfileMapper;

    private final UserGrowthMapper userGrowthMapper;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean isUserExist(String userId) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisKey.USER_ID_SET, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerUser(String initData) {
        if (StringUtils.isBlank(initData)) {
            throw new LynniaException(Result.INIT_DATA_FAIL);
        }
        if (!telegramAuth.isValid(initData)) {
            throw new LynniaException(Result.AUTH_TELEGRAM_INVALID);
        }
        TmaUser user = telegramAuth.getUser(initData);
        String userId = user.getId();
        if (isUserExist(userId)) {
            throw new LynniaException(Result.USER_EXIST);
        }
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        UserAccount userAccount = buildUserAccount(userId, now);
        UserProfile userProfile = buildUserProfile(user, now);
        UserGrowth userGrowth = buildUserGrowth(userId, now);
        try {
            userAccountMapper.insertAccount(userAccount);
            userProfileMapper.insertProfile(userProfile);
            userGrowthMapper.insertGrowth(userGrowth);
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            stringRedisTemplate.opsForSet()
                                    .add(RedisKey.USER_ID_SET, userId);
                        }
                    }
            );
        } catch (DuplicateKeyException e) {
            if (userAccountMapper.selectById(userId) != null) {
                // 已存在 → 幂等返回成功
                return;
            }
            throw new LynniaException(Result.SYSTEM_ERROR);
        }

    }

    @Override
    public UserInfoOutDto queryUserInfo(String userId) {
        if (!isUserExist(userId)) {
            throw new LynniaException(Result.USER_NOT_EXIST);
        }
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        UserGrowth userGrowth = userGrowthMapper.selectByUserId(userId);
        return UserInfoOutDto.builder()
                .bio(userProfile.getBio())
                .photoUrl(userProfile.getPhotoUrl())
                .identityType(IdentityType.getById(userProfile.getIdentityType()))
                .timezone(userProfile.getTimezone())
                .reputation(userGrowth.getReputation())
                .totalCheckinDays(userGrowth.getTotalCheckinDays())
                .streakDays(userGrowth.getStreakDays())
                .build();
    }

    @Override
    public void saveProfile(String userId, String initData) {
        if (!isUserExist(userId)) {
            throw new LynniaException(Result.USER_NOT_EXIST);
        }
        boolean valid = telegramAuth.isValid(initData);
        if (!valid) {
            throw new LynniaException(Result.AUTH_TELEGRAM_INVALID);
        }
        TmaUser user = telegramAuth.getUser(initData);
        if (!user.getId().equals(userId)) {
            throw new LynniaException(Result.AUTH_TELEGRAM_INVALID);
        }
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        String userName = UserUtils.getUserName(user.getFirstName(), user.getLastName());
        userProfileMapper.updateProfile(userId, userName, user.getPhotoUrl(), now);
    }

    @Override
    public void saveUserBio(String userId, String bio) {
        if (!isUserExist(userId)) {
            throw new LynniaException(Result.USER_NOT_EXIST);
        }
        if (bio.length() > UserConstant.MAX_BIO_LENGTH) {
            throw new LynniaException(Result.BIO_TOO_LONG);
        }
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        userProfileMapper.updateBio(userId, bio, now);
    }

    @Override
    public String sendVerificationCode(String userId) {
        if (!isUserExist(userId)) {
            throw new LynniaException(Result.USER_NOT_EXIST);
        }
        String key = RedisKey.loginCode(userId);
        String code = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(code)) {
            code = UserUtils.generateCode();
            stringRedisTemplate.opsForValue().set(key, code, Duration.ofMinutes(3));
        }
        return code;
    }

    private UserAccount buildUserAccount(String userId, OffsetDateTime dateTime) {
        return UserAccount.builder()
                .userId(userId)
                .status(UserStatus.NORMAL.getCode())
                .taskReviewer(false)
                .banOperator(false)
                .registerTime(dateTime)
                .updateTime(dateTime)
                .build();
    }

    private UserProfile buildUserProfile(TmaUser user, OffsetDateTime dateTime) {
        return UserProfile.builder()
                .userId(user.getId())
                .nickname(UserUtils.getUserName(user.getFirstName(), user.getLastName()))
                .tgUsername(user.getTgUserName())
                .bio("")
                .photoUrl(user.getPhotoUrl())
                .identityType(IdentityType.UNSELECTED.getCode())
                .timezone("Asia/Shanghai")
                .updateTime(dateTime)
                .build();
    }

    private UserGrowth buildUserGrowth(String userId, OffsetDateTime dateTime) {
        return UserGrowth.builder()
                .userId(userId)
                .reputation(UserConstant.DEFAULT)
                .totalCheckinDays(UserConstant.DEFAULT)
                .streakDays(UserConstant.DEFAULT)
                .plusCount(UserConstant.DEFAULT_COUNT)
                .minusCount(UserConstant.DEFAULT_COUNT)
                .taskCount(UserConstant.DEFAULT_COUNT)
                .updateTime(dateTime)
                .build();
    }

}
