package com.lucky.admin.controller.system;

import com.lucky.common.core.config.LuckyConfig;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.domain.dto.UserDTO;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.security.utils.SecurityUtils;
import com.lucky.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 首页
 *
 * @author ruoyi
 */
@RestController
public class SysIndexController {

    /**
     * 系统基础配置
     */
    @Resource
    private LuckyConfig luckyConfig;

    @Resource
    private ISysUserService userService;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", luckyConfig.getName(), luckyConfig.getVersion());
    }

    /**
     * 解锁屏幕
     */
    @PostMapping("/unlockscreen")
    public R<Void> unlockScreen(@RequestBody Map<String, String> body) {
        String password = body.get("password");
        if (StringUtils.isEmpty(password)) {
            return R.fail("密码不能为空");
        }
        String username = SecurityUtils.getUsername();
        UserDTO user = userService.selectUserByUserName(username);
        if (user == null) {
            return R.fail("服务器超时，请重新登录");
        }
        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            return R.fail("密码错误，请重新输入");
        }
        return R.ok();
    }

}
