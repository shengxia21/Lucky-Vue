package com.lucky.system.domain.query.menu;

import lombok.Data;

/**
 * 菜单权限查询参数
 *
 * @author lucky
 */
@Data
public class SysMenuQuery {

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;

}
