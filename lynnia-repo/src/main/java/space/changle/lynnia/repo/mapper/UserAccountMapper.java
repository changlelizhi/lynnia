package space.changle.lynnia.repo.mapper;

import org.apache.ibatis.annotations.Param;
import space.changle.lynnia.repo.entity.UserAccount;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/4 14:15
 * @description
 */
public interface UserAccountMapper {

    /**
     * 根据用户ID查询
     */
    UserAccount selectById(@Param("userId") String userId);

    /**
     * 插入用户
     */
    int insertAccount(UserAccount userAccount);
}
