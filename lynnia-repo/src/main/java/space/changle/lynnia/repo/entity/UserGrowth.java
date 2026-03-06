package space.changle.lynnia.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * @author 长乐
 * @date 2026/3/5 23:48
 * @version 1.0.0
 * @description 用户成长数据表 (t_user_growth) 与 t_user_account 表通过 user_id 建立一对一关联
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGrowth {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 声望值
     */
    private Integer reputation;

    /**
     * 累计签到天数
     */
    private Integer totalCheckinDays;

    /**
     * 连续签到天数
     */
    private Integer streakDays;

    /**
     * 最后签到日期(UTC)
     */
    private LocalDate lastCheckinDate;

    /**
     * 加时次数
     */
    private Integer plusCount;

    /**
     * 减时次数
     */
    private Integer minusCount;

    /**
     * 发布任务次数
     */
    private Integer taskCount;

    /**
     * 更新时间(UTC)
     */
    private OffsetDateTime updateTime;
}
