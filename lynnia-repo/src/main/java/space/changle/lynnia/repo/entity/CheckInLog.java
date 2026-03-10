package space.changle.lynnia.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/9 19:49
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInLog  {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 变更值
     */
    private Integer changeVal;

    /**
     * 签到时间(UTC)
     */
    private LocalDate checkInDate;

    /**
     * 更新时间(UTC)
     */
    private OffsetDateTime updateTime;

}
