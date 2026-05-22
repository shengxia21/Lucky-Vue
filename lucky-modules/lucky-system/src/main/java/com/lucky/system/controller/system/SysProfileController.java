package com.lucky.system.controller.system;

import com.lucky.common.core.config.LuckyConfig;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.domain.dto.UserDTO;
import com.lucky.common.core.domain.model.LoginUser;
import com.lucky.common.core.utils.DateUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.bean.BeanUtils;
import com.lucky.common.core.utils.file.FileUploadUtils;
import com.lucky.common.core.utils.file.FileUtils;
import com.lucky.common.core.utils.file.MimeTypeUtils;
import com.lucky.common.log.annotation.Log;
import com.lucky.common.log.enums.BusinessType;
import com.lucky.common.mybatis.core.controller.BaseController;
import com.lucky.common.security.utils.SecurityUtils;
import com.lucky.system.domain.SysUser;
import com.lucky.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {

    @Resource
    private ISysUserService userService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = getLoginUser();
        UserDTO user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUserName()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUserName()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = getLoginUser();
        UserDTO currentUser = loginUser.getUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty(user.getPhoneNumber()) && !userService.checkPhoneUnique(currentUser.getUserId(), user.getPhoneNumber())) {
            return R.fail("修改用户'" + loginUser.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser.getUserId(), user.getEmail())) {
            return R.fail("修改用户'" + loginUser.getUserName() + "'失败，邮箱账号已存在");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(currentUser, sysUser);
        if (userService.updateUserProfile(sysUser) > 0) {
            // 更新用户信息
            SecurityUtils.refreshLoginUser(loginUser);
            return R.ok();
        }
        return R.fail("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public R<Void> updatePwd(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        LoginUser loginUser = getLoginUser();
        Long userId = loginUser.getUserId();
        SysUser user = userService.selectUserById(userId);
        String password = user.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return R.fail("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return R.fail("新密码不能与旧密码相同");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(userId, newPassword) > 0) {
            // 更新用户密码&密码最后更新时间
            loginUser.getUser().setPwdUpdateDate(DateUtils.getNowDate());
            loginUser.getUser().setPassword(newPassword);
            SecurityUtils.refreshLoginUser(loginUser);
            return R.ok();
        }
        return R.fail("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(LuckyConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION, true);
            if (userService.updateUserAvatar(loginUser.getUserId(), avatar)) {
                String oldAvatar = loginUser.getUser().getAvatar();
                if (StringUtils.isNotEmpty(oldAvatar)) {
                    FileUtils.deleteFile(LuckyConfig.getProfile() + FileUtils.stripPrefix(oldAvatar));
                }
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新用户头像
                loginUser.getUser().setAvatar(avatar);
                SecurityUtils.refreshLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }

}
