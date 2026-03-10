package space.changle.lynnia.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;


/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/5 23:47
 * @description 用户信息表 (t_user_profile)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    /**
     * 用户唯一ID
     */
    private String userId;

    /**
     * 平台用户名
     */
    private String nickname;

    /**
     * Telegram用户名
     */
    private String tgUsername;

    /**
     * 声望值
     */
    private Integer reputation;

    /**
     * 个人简介
     */
    private String bio;

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
     * 头像URL
     */
    private String photoUrl;

    /**
     * 身份类型（0未选 1Dom 2Sub 3Switch）
     */
    private Integer identityType;

    /**
     * IANA时区
     */
    private String timezone;

    /**
     * 更新时间(UTC)
     */
    private OffsetDateTime updateTime;
}
