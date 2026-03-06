package space.changle.lynnia.repo.mapper;

import space.changle.lynnia.repo.entity.UserGrowth;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/4 17:38
 * @description
 */
public interface UserGrowthMapper {

    int insertGrowth (UserGrowth userGrowth);


    UserGrowth selectByUserId(String userId);
}
