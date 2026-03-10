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
import space.changle.lynnia.common.constant.LynniaConstant;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.LynniaException;
import space.changle.lynnia.common.result.TmaUser;
import space.changle.lynnia.common.util.UserUtils;
import space.changle.lynnia.dto.outdto.*;
import space.changle.lynnia.repo.entity.CheckInLog;
import space.changle.lynnia.repo.entity.UserAccount;
import space.changle.lynnia.repo.entity.UserProfile;
import space.changle.lynnia.repo.mapper.CheckInLogMapper;
import space.changle.lynnia.repo.mapper.UserAccountMapper;
import space.changle.lynnia.repo.mapper.UserProfileMapper;
import space.changle.lynnia.service.constant.RedisKey;
import space.changle.lynnia.security.auth.TelegramAuth;
import space.changle.lynnia.service.constant.UserConstant;
import space.changle.lynnia.api.enums.IdentityType;
import space.changle.lynnia.api.enums.UserStatus;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    private final CheckInLogMapper checkInLogMapper;

    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public boolean isNormalUser(String userId) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisKey.USER_NORMAL_SET, userId));
    }

    @Override
    public boolean isBanUser(String userId) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisKey.USER_BAN_SET, userId));
    }

    @Override
    public void assertNormalUser(String userId) {

        UserStatus status = checkUserStatus(userId);

        if (status == UserStatus.BANNED) {
            throw new LynniaException(Result.USER_BAN);
        }

        if (status == UserStatus.NOT_EXIST) {
            throw new LynniaException(Result.USER_NOT_EXIST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SignupOutDto registerUser(String initData) {
        if (!telegramAuth.isValid(initData)) {
            throw new LynniaException(Result.AUTH_TELEGRAM_INVALID);
        }
        TmaUser user = telegramAuth.getUser(initData);
        String userId = user.getId();
        UserStatus userStatus = checkUserStatus(userId);
        if (userStatus == UserStatus.NORMAL) {
            return SignupOutDto.builder()
                    .signUpResult(true)
                    .userId(userId)
                    .build();
        }
        if (userStatus == UserStatus.BANNED) {
            throw new LynniaException(Result.USER_BAN);
        }

        OffsetDateTime now = OffsetDateTime.now(LynniaConstant.SYSTEM_ZONE);
        UserAccount userAccount = buildUserAccount(userId, now);
        UserProfile userProfile = buildUserProfile(user, now);
        try {
            userAccountMapper.insertAccount(userAccount);
            userProfileMapper.insertProfile(userProfile);
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            stringRedisTemplate.opsForValue().set(RedisKey.USER_STATUS_PREFIX + userId, String.valueOf(UserStatus.NORMAL.getCode()));
                            stringRedisTemplate.opsForValue().set(RedisKey.USER_TIME_ZONE_PREFIX + userId, "Asia/Shanghai");
                        }
                    }
            );
            return SignupOutDto.builder().signUpResult(true).userId(userId).build();
        } catch (DuplicateKeyException e) {
            return SignupOutDto.builder().signUpResult(true).userId(userId).build();
        }

    }

    @Override
    public UserInfoOutDto queryUserInfo(String userId) {
        assertNormalUser(userId);
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);

        return null;
    }

    @Override
    public void saveProfile(String userId, String initData) {
        if (isNormalUser(userId)) {
            boolean valid = telegramAuth.isValid(initData);
            if (!valid) {
                throw new LynniaException(Result.AUTH_TELEGRAM_INVALID);
            }
            TmaUser user = telegramAuth.getUser(initData);
            if (!user.getId().equals(userId)) {
                throw new LynniaException(Result.AUTH_TELEGRAM_INVALID);
            }
            OffsetDateTime now = OffsetDateTime.now(LynniaConstant.SYSTEM_ZONE);
            String userName = UserUtils.getUserName(user.getFirstName(), user.getLastName());
            userProfileMapper.updateProfile(userId, userName, user.getPhotoUrl(), now);
        }
        if (isBanUser(userId)) {
            throw new LynniaException(Result.USER_BAN);
        }
        throw new LynniaException(Result.USER_NOT_EXIST);
    }

    @Override
    public void saveUserBio(String userId, String bio) {

        if (isNormalUser(userId)) {
            if (bio.length() > UserConstant.MAX_BIO_LENGTH) {
                throw new LynniaException(Result.BIO_TOO_LONG);
            }
            OffsetDateTime now = OffsetDateTime.now(LynniaConstant.SYSTEM_ZONE);
            userProfileMapper.updateBio(userId, bio, now);
        }
        if (isBanUser(userId)) {
            throw new LynniaException(Result.USER_BAN);
        }
        throw new LynniaException(Result.USER_NOT_EXIST);
    }

    @Override
    public CodeOutDto sendVerificationCode(String userId) {

        assertNormalUser(userId);
        String key = RedisKey.loginCode(userId);
        String code = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(code)) {
            code = UserUtils.generateCode();
            stringRedisTemplate.opsForValue().set(key, code, Duration.ofMinutes(3));
            return new CodeOutDto(code, 180L);
        }
        Long expire = stringRedisTemplate.getExpire(key);
        return new CodeOutDto(code, expire);

    }

    @Override
    public UserCheckOutDto checkUser(String initData) {
        if (!telegramAuth.isValid(initData)) {
            throw new LynniaException(Result.AUTH_TELEGRAM_INVALID);
        }
        TmaUser user = telegramAuth.getUser(initData);
        String userId = user.getId();

        boolean isNormal = isNormalUser(userId);
        boolean isBan = isBanUser(userId);

        boolean exist = isNormal || isBan;
        String status = isBan ? "BAN" : (isNormal ? "NORMAL" : null);
        return UserCheckOutDto.builder().exist(exist).status(status).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userSignIn(String userId) {
        assertNormalUser(userId);
        LocalDate today = LocalDate.now(LynniaConstant.SYSTEM_ZONE);
        OffsetDateTime now = OffsetDateTime.now(LynniaConstant.SYSTEM_ZONE);
        ;
        CheckInLog checkInLog = CheckInLog.builder()
                .userId(userId)
                .changeVal(UserConstant.SIGN_VALUE)
                .checkInDate(today)
                .updateTime(now)
                .build();

        try {
            checkInLogMapper.insertCheckInLog(checkInLog);
            userProfileMapper.updateReputation(userId, UserConstant.SIGN_VALUE, now);
        } catch (DuplicateKeyException e) {
            throw new LynniaException(Result.USER_CHECKIN_TODAY);
        }
    }

    @Override
    public SignOutDto isSign(String userId) {
        if (isNormalUser(userId)) {
            LocalDate today = LocalDate.now(LynniaConstant.SYSTEM_ZONE);
            boolean existsTodayCheckIn = checkInLogMapper.existsTodayCheckIn(userId, today);
            //localDates转成用户自己的时区
            List<LocalDate> localDates = checkInLogMapper.selectMonthCheckInDays(userId);

            //用户自己设置的时区
            String timeZone = stringRedisTemplate.opsForValue().get(RedisKey.USER_TIME_ZONE_PREFIX + userId);
            ZoneId userZone = ZoneId.of(timeZone);

            List<LocalDate> userDates = localDates.stream()
                    .map(date -> date
                            .atStartOfDay(LynniaConstant.SYSTEM_ZONE)
                            .withZoneSameInstant(userZone)
                            .toLocalDate())
                    .toList();
            return SignOutDto.builder()
                    .isSigned(existsTodayCheckIn)
                    .history(userDates)
                    .build();
        }
        if (isBanUser(userId)) {
            throw new LynniaException(Result.USER_BAN);
        }
        throw new LynniaException(Result.USER_NOT_EXIST);

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
                .reputation(UserConstant.DEFAULT)
                .bio("")
                .plusCount(UserConstant.DEFAULT_COUNT)
                .minusCount(UserConstant.DEFAULT_COUNT)
                .taskCount(UserConstant.DEFAULT_COUNT)
                .photoUrl(user.getPhotoUrl())
                .identityType(IdentityType.UNSELECTED.getCode())
                .timezone("Asia/Shanghai")
                .updateTime(dateTime)
                .build();
    }


    private boolean isValidTimeZone(String zone) {
        return ZoneId.getAvailableZoneIds().contains(zone);
    }


    private UserStatus checkUserStatus(String userId) {
        String cacheValue = stringRedisTemplate.opsForValue().get(RedisKey.USER_STATUS_PREFIX + userId);

        if (cacheValue == null) {
            return UserStatus.NOT_EXIST;
        }
        return UserStatus.fromCode(Integer.parseInt(cacheValue));
    }
}
