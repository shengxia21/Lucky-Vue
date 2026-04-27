package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.system.domain.SysDept;
import com.lucky.system.domain.query.dept.SysDeptQuery;
import com.lucky.system.domain.vo.dept.SysDeptVO;
import org.apache.ibatis.annotations.Param;

import java.util.Arrays;
import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author lucky
 */
public interface SysDeptMapper extends BaseMapperX<SysDept, SysDeptVO> {

    /**
     * 查询部门列表
     *
     * @param query 查询参数
     * @return 部门列表
     */
    List<SysDeptVO> selectDeptList(SysDeptQuery query);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    List<Long> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * 根据ID查询部门
     *
     * @param deptId 部门ID
     * @return 部门
     */
    SysDeptVO selectDeptById(@Param("deptId") Long deptId);

    /**
     * 修改子元素关系
     *
     * @param children 子部门列表
     * @return 结果
     */
    int updateDeptChildren(@Param("children") List<SysDept> children);

    default List<SysDept> selectChildrenDeptById(Long deptId) {
        return selectList(Wrappers.<SysDept>lambdaQuery()
                .apply("find_in_set({0}, ancestors)", deptId));
    }

    default Long selectNormalChildrenDeptById(Long deptId) {
        return selectCount(Wrappers.<SysDept>lambdaQuery()
                .apply("find_in_set({0}, ancestors)", deptId)
                .eq(SysDept::getStatus, UserConstants.DEPT_NORMAL));
    }

    default Long hasChildByDeptId(Long deptId) {
        return selectCount(Wrappers.<SysDept>lambdaQuery()
                .eq(SysDept::getParentId, deptId));
    }

    default SysDept checkDeptNameUnique(String deptName, Long parentId) {
        return selectOne(Wrappers.lambdaQuery(SysDept.class)
                .eq(SysDept::getDeptName, deptName)
                .eq(SysDept::getParentId, parentId), false);
    }

    default int updateDeptStatusNormal(Long[] deptIds) {
        return update(Wrappers.<SysDept>lambdaUpdate()
                .set(SysDept::getStatus, UserConstants.DEPT_NORMAL)
                .in(SysDept::getDeptId, Arrays.asList(deptIds)));
    }

    default int updateSortByDeptId(String deptId, String orderNum) {
        return update(Wrappers.<SysDept>lambdaUpdate()
                .set(SysDept::getOrderNum, orderNum)
                .eq(SysDept::getDeptId, deptId));
    }

}