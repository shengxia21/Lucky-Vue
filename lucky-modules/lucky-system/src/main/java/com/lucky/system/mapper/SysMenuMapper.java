package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.system.domain.SysMenu;
import com.lucky.system.domain.query.menu.SysMenuQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Arrays;
import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author lucky
 */
public interface SysMenuMapper extends BaseMapperX<SysMenu, SysMenu> {

    /**
     * 根据用户查询系统菜单列表
     *
     * @param query  菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuListByUserId(@Param("query") SysMenuQuery query, @Param("userId") Long userId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    default List<SysMenu> selectList(SysMenuQuery query) {
        LambdaQueryWrapper<SysMenu> wrapper = Wrappers.<SysMenu>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getMenuName()), SysMenu::getMenuName, query.getMenuName())
                .eq(StringUtils.isNotEmpty(query.getVisible()), SysMenu::getVisible, query.getVisible())
                .eq(StringUtils.isNotEmpty(query.getStatus()), SysMenu::getStatus, query.getStatus())
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);
        return selectList(wrapper);
    }

    default List<SysMenu> selectMenuTreeAll() {
        LambdaQueryWrapper<SysMenu> wrapper = Wrappers.<SysMenu>lambdaQuery()
                .in(SysMenu::getMenuType, Arrays.asList("M", "C"))
                .eq(SysMenu::getStatus, "0")
                .orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);
        return selectList(wrapper);
    }

    default Long hasChildByMenuId(Long menuId) {
        LambdaUpdateWrapper<SysMenu> wrapper = Wrappers.<SysMenu>lambdaUpdate()
                .eq(SysMenu::getParentId, menuId);
        return selectCount(wrapper);
    }

    default int updateOrderNumBySort(String menuId, String orderNum) {
        LambdaUpdateWrapper<SysMenu> wrapper = Wrappers.<SysMenu>lambdaUpdate()
                .set(SysMenu::getOrderNum, orderNum)
                .eq(SysMenu::getMenuId, menuId);
        return update(wrapper);
    }

    default SysMenu checkMenuNameUnique(String menuName, Long parentId) {
        LambdaQueryWrapper<SysMenu> wrapper = Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getMenuName, menuName)
                .eq(SysMenu::getParentId, parentId);
        return selectOne(wrapper, false);
    }

    default List<SysMenu> selectListByPathOrRouteName(String path, String routeName) {
        LambdaQueryWrapper<SysMenu> wrapper = Wrappers.<SysMenu>lambdaQuery()
                .in(SysMenu::getMenuType, Arrays.asList("M", "C"))
                .and(w -> w.eq(SysMenu::getPath, path)
                        .or().eq(SysMenu::getPath, routeName)
                        .or().eq(SysMenu::getRouteName, path)
                        .or().eq(SysMenu::getRouteName, routeName)
                );
        return selectList(wrapper);
    }

}
