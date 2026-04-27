package com.lucky.system.service.impl;

import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.spring.SpringUtils;
import com.lucky.common.core.utils.text.Convert;
import com.lucky.common.mybatis.annotation.DataScope;
import com.lucky.common.security.utils.SecurityUtils;
import com.lucky.system.domain.SysDept;
import com.lucky.system.domain.SysRole;
import com.lucky.system.domain.query.dept.SysDeptQuery;
import com.lucky.system.domain.query.dept.SysDeptSaveQuery;
import com.lucky.system.domain.vo.TreeSelect;
import com.lucky.system.domain.vo.dept.SysDeptVO;
import com.lucky.system.mapper.SysDeptMapper;
import com.lucky.system.mapper.SysRoleMapper;
import com.lucky.system.mapper.SysUserMapper;
import com.lucky.system.service.ISysDeptService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门管理 服务实现
 *
 * @author lucky
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {

    @Resource
    private SysDeptMapper deptMapper;

    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysUserMapper userMapper;

    @Override
    @DataScope(deptAlias = "d")
    public List<SysDeptVO> selectDeptList(SysDeptQuery query) {
        return deptMapper.selectDeptList(query);
    }

    @Override
    public List<TreeSelect> selectDeptTreeList(SysDeptQuery dept) {
        List<SysDeptVO> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
        List<SysDept> deptList = MapstructUtils.convert(depts, SysDept.class);
        return buildDeptTreeSelect(deptList);
    }

    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = depts.stream().map(SysDept::getDeptId).toList();
        for (SysDept dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).toList();
    }

    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    @Override
    public SysDeptVO selectDeptById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    @Override
    public Long selectNormalChildrenDeptById(Long deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return deptMapper.hasChildByDeptId(deptId) > 0;
    }

    @Override
    public boolean checkDeptExistUser(Long deptId) {
        return userMapper.checkDeptExistUser(deptId) > 0;
    }

    @Override
    public boolean checkDeptNameUnique(SysDeptSaveQuery dept) {
        long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getDeptId() != deptId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!SecurityUtils.isAdmin() && StringUtils.isNotNull(deptId)) {
            SysDeptQuery query = new SysDeptQuery();
            query.setDeptId(deptId);
            List<SysDeptVO> list = SpringUtils.getAopProxy(this).selectDeptList(query);
            if (StringUtils.isEmpty(list)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    @Override
    public int insertDept(SysDeptSaveQuery dept) {
        SysDept info = deptMapper.selectById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        SysDept sysDept = MapstructUtils.convert(dept, SysDept.class);
        sysDept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return deptMapper.insert(sysDept);
    }

    @Override
    public int updateDept(SysDeptSaveQuery dept) {
        SysDept newParentDept = deptMapper.selectById(dept.getParentId());
        SysDept oldDept = deptMapper.selectById(dept.getDeptId());
        SysDept sysDept = MapstructUtils.convert(dept, SysDept.class);
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            sysDept.setAncestors(newAncestors);
            updateDeptChildren(sysDept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateById(sysDept);
        if (UserConstants.DEPT_NORMAL.equals(sysDept.getStatus()) && StringUtils.isNotEmpty(sysDept.getAncestors())
                && !StringUtils.equals("0", sysDept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(sysDept);
        }
        return result;
    }

    @Override
    @Transactional
    public void updateDeptSort(String[] deptIds, String[] orderNums) {
        try {
            for (int i = 0; i < deptIds.length; i++) {
                deptMapper.updateSortByDeptId(deptIds[i], orderNums[i]);
            }
        } catch (Exception e) {
            throw new ServiceException("保存排序异常，请联系管理员");
        }
    }

    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteById(deptId);
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        deptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (!children.isEmpty()) {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<>();
        for (SysDept n : list) {
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return !getChildList(list, t).isEmpty();
    }

}