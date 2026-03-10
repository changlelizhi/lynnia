package space.changle.lynnia.dto.outdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/5 16:39
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserInfoOutDto {

    // 用户名
    private String nickname;

    // 总声望
    private int reputation;

    // 加次数
    private int plusCount;

    // 减次数
    private int minusCount;

    // 发布任务次数
    private int taskCount;

    // 个人简介
    private String bio;

    // 个人头像
    private String photoUrl;

    // 身份
    private String identityType;

    // 时区
    private String timezone;

    // 总签到天数
    private int totalCheckinDays;

    // 连续签到天数
    private int streakDays;

}
