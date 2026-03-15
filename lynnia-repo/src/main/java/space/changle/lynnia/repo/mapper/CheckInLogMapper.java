package space.changle.lynnia.repo.mapper;

import org.apache.ibatis.annotations.Param;
import space.changle.lynnia.repo.entity.CheckInLog;

import java.time.LocalDate;
import java.util.List;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/9 20:19
 * @description
 */
public interface CheckInLogMapper {

    /**
     * 插入签到日志
     * @param checkInLog
     * @return
     */
    int insertCheckInLog( CheckInLog checkInLog);

    /**
     * 查询用户最后一次签到时间
     * @param userId
     * @param today
     * @return
     */
    CheckInLog selectLastCheckInDate(@Param("userId") String userId, @Param("checkInDate") LocalDate today);

    /**
     * 查询用户当月签到天数
     * @param userId
     * @return
     */
    List<LocalDate> selectMonthCheckInDays(@Param("userId")String userId);

    /**
     * 判断用户今天是否签到
     * @param userId
     * @param today
     * @return
     */
    boolean existsTodayCheckIn(@Param("userId") String userId, @Param("today") LocalDate today);

    // 计算用户签到总天数
    int countTotalCheckInDays(@Param("userId") String userId);
}
