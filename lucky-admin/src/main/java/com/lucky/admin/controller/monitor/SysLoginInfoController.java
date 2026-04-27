package com.lucky.admin.controller.monitor;

import com.lucky.admin.service.SysPasswordService;
import com.lucky.common.core.domain.R;
import com.lucky.common.excel.utils.ExcelUtil;
import com.lucky.common.log.annotation.Log;
import com.lucky.common.log.enums.BusinessType;
import com.lucky.common.mybatis.core.controller.BaseController;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.system.domain.SysLoginInfo;
import com.lucky.system.domain.query.loginInfo.SysLoginInfoQuery;
import com.lucky.system.domain.vo.loginInfo.SysLoginInfoVO;
import com.lucky.system.service.ISysLoginInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问记录
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/loginInfo")
public class SysLoginInfoController extends BaseController {

    @Resource
    private ISysLoginInfoService loginInfoService;

    @Resource
    private SysPasswordService passwordService;

    /**
     * 获取登录日志列表
     */
    @PreAuthorize("@ss.hasPermission('monitor:loginInfo:list')")
    @GetMapping("/list")
    public TableDataInfo<SysLoginInfoVO> list(PageQuery pageQuery, SysLoginInfoQuery query) {
        return loginInfoService.selectLoginInfoList(pageQuery, query);
    }

    /**
     * 导出登录日志
     */
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermission('monitor:loginInfo:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoginInfoQuery query) {
        List<SysLoginInfo> list = loginInfoService.selectLoginInfoList(query);
        ExcelUtil<SysLoginInfo> util = new ExcelUtil<>(SysLoginInfo.class);
        util.exportExcel(response, list, "登录日志");
    }

    /**
     * 删除登录日志
     */
    @PreAuthorize("@ss.hasPermission('monitor:loginInfo:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public R<Void> remove(@PathVariable Long[] infoIds) {
        return toAjax(loginInfoService.deleteLoginInfoByIds(infoIds));
    }

    /**
     * 清空登录日志
     */
    @PreAuthorize("@ss.hasPermission('monitor:loginInfo:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public R<Void> clean() {
        return toAjax(loginInfoService.cleanLoginInfo());
    }

    /**
     * 解锁账户
     */
    @PreAuthorize("@ss.hasPermission('monitor:loginInfo:unlock')")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public R<Void> unlock(@PathVariable String userName) {
        passwordService.clearLoginRecordCache(userName);
        return R.ok();
    }

}