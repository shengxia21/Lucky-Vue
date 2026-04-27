package com.lucky.admin.controller.system;

import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.log.annotation.Log;
import com.lucky.common.log.enums.BusinessType;
import com.lucky.common.mybatis.core.controller.BaseController;
import com.lucky.system.domain.query.dept.SysDeptQuery;
import com.lucky.system.domain.query.dept.SysDeptSaveQuery;
import com.lucky.system.domain.vo.dept.SysDeptVO;
import com.lucky.system.service.ISysDeptService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    @Resource
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermission('system:dept:list')")
    @GetMapping("/list")
    public R<List<SysDeptVO>> list(SysDeptQuery query) {
        return R.ok(deptService.selectDeptList(query));
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermission('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public R<List<SysDeptVO>> excludeChild(@PathVariable(required = false) Long deptId) {
        List<SysDeptVO> list = deptService.selectDeptList(new SysDeptQuery());
        list.removeIf(d -> d.getDeptId().intValue() == deptId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""));
        return R.ok(list);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public R<SysDeptVO> getInfo(@PathVariable Long deptId) {
        deptService.checkDeptDataScope(deptId);
        return R.ok(deptService.selectDeptById(deptId));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermission('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysDeptSaveQuery dept) {
        if (!deptService.checkDeptNameUnique(dept)) {
            return R.fail("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermission('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysDeptSaveQuery dept) {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (!deptService.checkDeptNameUnique(dept)) {
            return R.fail("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(deptId)) {
            return R.fail("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0) {
            return R.fail("该部门包含未停用的子部门！");
        }
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 保存部门排序
     */
    @PreAuthorize("@ss.hasPermission('system:dept:edit')")
    @Log(title = "保存部门排序", businessType = BusinessType.UPDATE)
    @PutMapping("/updateSort")
    public R<Void> updateSort(@RequestBody Map<String, String> params) {
        String[] deptIds = params.get("deptIds").split(",");
        String[] orderNums = params.get("orderNums").split(",");
        deptService.updateDeptSort(deptIds, orderNums);
        return R.ok();
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermission('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public R<Void> remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return R.warn("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return R.warn("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        return toAjax(deptService.deleteDeptById(deptId));
    }

}
