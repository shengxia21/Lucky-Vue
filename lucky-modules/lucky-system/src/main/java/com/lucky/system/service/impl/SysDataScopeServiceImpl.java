package com.lucky.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.lucky.common.core.service.DataScopeService;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.system.domain.SysRoleDept;
import com.lucky.system.mapper.SysDeptMapper;
import com.lucky.system.mapper.SysRoleDeptMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据权限实现
 * <p>
 * 注意: 此Service内不允许调用标注`数据权限`注解的方法
 * 例如: deptMapper.selectDeptList 此 selectDeptList 方法标注了`数据权限`注解 会出现循环解析的问题
 *
 * @author lucky
 */
@Service("dataScope")
public class SysDataScopeServiceImpl implements DataScopeService {

    @Resource
    private SysRoleDeptMapper roleDeptMapper;

    @Resource
    private SysDeptMapper deptMapper;

    @Override
    public String getRoleCustom(Long roleId) {
        if (ObjectUtil.isNull(roleId)) {
            return "-1";
        }
        List<SysRoleDept> roleDeptList = roleDeptMapper.getRoleCustom(roleId);
        if (CollUtil.isNotEmpty(roleDeptList)) {
            return roleDeptList.stream().map(rd -> Convert.toStr(rd.getDeptId()))
                    .filter(Objects::nonNull).collect(Collectors.joining(StringUtils.SEPARATOR));
        }
        return "-1";
    }

    @Override
    public String getDeptAndChild(Long deptId) {
        if (ObjectUtil.isNull(deptId)) {
            return "-1";
        }
        List<Long> deptIds = deptMapper.selectDeptAndChildById(deptId);
        if (CollUtil.isNotEmpty(deptIds)) {
            return deptIds.stream().map(Convert::toStr).filter(Objects::nonNull)
                    .collect(Collectors.joining(StringUtils.SEPARATOR));
        }
        return "-1";
    }

}
