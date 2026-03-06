package space.changle.lynnia.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * @author 长乐
 * @date 2026/3/5 23:44
 * @version 1.0.0
 * @description 用户账户表 (t_user_account)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

    /**
     * 用户唯一ID
     */
    private String userId;

    /**
     * 1正常 2冻结 3封禁
     */
    private Integer status;

    /**
     * 任务审核权限
     */
    private Boolean taskReviewer;

    /**
     * 封禁解封权限
     */
    private Boolean banOperator;

    /**
     * 注册时间(UTC)
     */
    private OffsetDateTime registerTime;

    /**
     * 更新时间(UTC)
     */
    private OffsetDateTime updateTime;
}
