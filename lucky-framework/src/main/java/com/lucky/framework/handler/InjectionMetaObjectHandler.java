package com.lucky.framework.handler;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lucky.common.constant.HttpStatus;
import com.lucky.common.core.domain.BaseEntity;
import com.lucky.common.core.domain.model.LoginUser;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.utils.SecurityUtils;
import com.lucky.common.utils.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

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
                // 获取当前时间作为创建时间和更新时间，如果创建时间不为空，则使用创建时间，否则使用当前时间
                Date current = StringUtils.isNotNull(baseEntity.getCreateTime()) ? baseEntity.getCreateTime() : new Date();
                baseEntity.setCreateTime(current);

                // 如果创建人为空，则填充当前登录用户的信息
                if (ObjectUtil.isNull(baseEntity.getCreateBy())) {
                    LoginUser loginUser = getLoginUser();
                    if (ObjectUtil.isNotNull(loginUser)) {
                        // 填充创建人
                        baseEntity.setCreateBy(loginUser.getUsername());
                    } else {
                        // 填充创建人默认值
                        baseEntity.setCreateBy(DEFAULT_USER_ID);
                    }
                }
            } else {
                this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
            }
        } catch (Exception e) {
            throw new ServiceException("insertFill异常 => " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                // 获取当前时间作为更新时间，无论原始对象中的更新时间是否为空都填充
                Date current = new Date();
                baseEntity.setUpdateTime(current);

                // 获取当前登录用户名，并填充更新人信息
                LoginUser loginUser = getLoginUser();
                if (ObjectUtil.isNotNull(loginUser)) {
                    baseEntity.setUpdateBy(loginUser.getUsername());
                } else {
                    baseEntity.setUpdateBy(DEFAULT_USER_ID);
                }
            } else {
                this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
            }
        } catch (Exception e) {
            throw new ServiceException("updateFill异常 => " + e.getMessage(), HttpStatus.UNAUTHORIZED);
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
