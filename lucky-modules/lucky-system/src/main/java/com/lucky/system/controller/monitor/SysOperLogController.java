package com.lucky.system.controller.monitor;

import com.lucky.common.core.domain.R;
import com.lucky.common.excel.utils.ExcelUtil;
import com.lucky.common.log.annotation.Log;
import com.lucky.common.log.enums.BusinessType;
import com.lucky.common.mybatis.core.controller.BaseController;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.system.domain.SysOperLog;
import com.lucky.system.domain.query.operLog.SysOperLogQuery;
import com.lucky.system.domain.vo.operLog.SysOperLogVO;
import com.lucky.system.service.ISysOperLogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/operLog")
public class SysOperLogController extends BaseController {

    @Resource
    private ISysOperLogService operLogService;

    /**
     * 查询操作日志列表
     */
    @PreAuthorize("@ss.hasPermission('monitor:operLog:list')")
    @GetMapping("/list")
    public TableDataInfo<SysOperLogVO> list(PageQuery pageQuery, SysOperLogQuery query) {
        return operLogService.selectOperLogList(pageQuery, query);
    }

    /**
     * 导出操作日志列表
     */
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermission('monitor:operLog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLogQuery query) {
        List<SysOperLog> list = operLogService.selectOperLogList(query);
        ExcelUtil<SysOperLog> util = new ExcelUtil<>(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }

    /**
     * 删除操作日志
     */
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermission('monitor:operLog:remove')")
    @DeleteMapping("/{operIds}")
    public R<Void> remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    /**
     * 清空操作日志
     */
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermission('monitor:operLog:remove')")
    @DeleteMapping("/clean")
    public R<Void> clean() {
        return toAjax(operLogService.cleanOperLog());
    }

}