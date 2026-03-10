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

    CheckInLog selectLastCheckInDate(@Param("userId") String userId, @Param("checkInDate") LocalDate today);


    List<LocalDate> selectMonthCheckInDays(@Param("userId")String userId);

    boolean existsTodayCheckIn(@Param("userId") String userId, @Param("today") LocalDate today);
}
