package com.lucky.common.mybatis.handler;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lucky.common.core.constant.HttpStatus;
import com.lucky.common.core.domain.model.LoginUser;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.mybatis.core.domain.BaseEntity;
import com.lucky.common.security.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * mybatis-plus注入处理器
 *
 * @author lucky
 */
public class InjectionMetaObjectHandler implements MetaObjectHandler {

    /**
     * 如果用户不存在默认注入-1代表无用户
     */
    private static final String DEFAULT_USER_ID = "-1";

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                // 获取当前时间作为创建时间
                LocalDateTime current = LocalDateTime.now();
                baseEntity.setCreateTime(current);
                baseEntity.setUpdateTime(current);

                // 如果创建人为空，则填充当前登录用户的信息
                if (ObjectUtil.isNull(baseEntity.getCreateBy())) {
                    LoginUser loginUser = getLoginUser();
                    Long createDept = baseEntity.getCreateDept();
                    if (ObjectUtil.isNotNull(loginUser)) {
                        String userName = loginUser.getUserName();
                        // 填充创建部门、创建人和更新人的信息
                        baseEntity.setCreateDept(ObjectUtil.isNotNull(createDept) ? createDept : loginUser.getDeptId());
                        baseEntity.setCreateBy(userName);
                        baseEntity.setUpdateBy(userName);
                    } else {
                        // 填充创建部门、创建人和更新人的信息
                        baseEntity.setCreateDept(ObjectUtil.isNotNull(createDept) ? createDept : Long.valueOf(DEFAULT_USER_ID));
                        baseEntity.setCreateBy(DEFAULT_USER_ID);
                        baseEntity.setUpdateBy(DEFAULT_USER_ID);
                    }
                }
            } else {
                LocalDateTime date = LocalDateTime.now();
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, date);
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, date);
                Date legacyDate = new Date();
                this.strictInsertFill(metaObject, "createTime", Date.class, legacyDate);
                this.strictInsertFill(metaObject, "updateTime", Date.class, legacyDate);
            }
        } catch (Exception e) {
            throw new ServiceException("insertFill异常 => " + e.getMessage(), HttpStatus.ERROR);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                // 获取当前时间作为更新时间，无论原始对象中的更新时间是否为空都填充
                LocalDateTime current = LocalDateTime.now();
                baseEntity.setUpdateTime(current);

                // 如果更新人为空，则填充当前登录用户的信息
                if (ObjectUtil.isNull(baseEntity.getUpdateBy())) {
                    LoginUser loginUser = getLoginUser();
                    // 填充更新人信息
                    if (ObjectUtil.isNotNull(loginUser)) {
                        baseEntity.setUpdateBy(loginUser.getUserName());
                    } else {
                        baseEntity.setUpdateBy(DEFAULT_USER_ID);
                    }
                }
            } else {
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
                this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
            }
        } catch (Exception e) {
            throw new ServiceException("updateFill异常 => " + e.getMessage(), HttpStatus.ERROR);
        }
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户的信息，如果用户未登录则返回 null
     */
    private LoginUser getLoginUser() {
        LoginUser loginUser;
        try {
            loginUser = SecurityUtils.getLoginUser();
        } catch (Exception e) {
            return null;
        }
        return loginUser;
    }

}
