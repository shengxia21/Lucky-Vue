package com.lucky.web.controller.system;

import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.domain.model.RegisterBody;
import com.lucky.common.utils.StringUtils;
import com.lucky.framework.web.service.SysRegisterService;
import com.lucky.system.service.ISysConfigService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 注册验证
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/register")
public class SysRegisterController extends BaseController {

    @Resource
    private SysRegisterService registerService;

    @Resource
    private ISysConfigService configService;

    /**
     * 获取注册状态
     *
     * @return 结果
     */
    @GetMapping("/enabled")
    public AjaxResult getRegisterEnabled() {
        AjaxResult ajax = AjaxResult.success();
        boolean registerEnabled = configService.selectRegisterEnabled();
        ajax.put("registerEnabled", registerEnabled);
        return ajax;
    }

    /**
     * 注册
     *
     * @param user 注册信息
     * @return 结果
     */
    @PostMapping
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

}
