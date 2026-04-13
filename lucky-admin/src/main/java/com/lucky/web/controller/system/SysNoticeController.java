package com.lucky.web.controller.system;

import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.core.text.Convert;
import com.lucky.common.enums.BusinessType;
import com.lucky.system.domain.query.notice.SysNoticeQuery;
import com.lucky.system.domain.query.notice.SysNoticeSaveQuery;
import com.lucky.system.domain.vo.notice.SysNoticeReadVO;
import com.lucky.system.domain.vo.notice.SysNoticeVO;
import com.lucky.system.service.ISysNoticeReadService;
import com.lucky.system.service.ISysNoticeService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {

    @Resource
    private ISysNoticeService noticeService;

    @Resource
    private ISysNoticeReadService noticeReadService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo<SysNoticeVO> list(PageQuery pageQuery, SysNoticeQuery query) {
        return noticeService.selectNoticeList(pageQuery, query);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public R<SysNoticeVO> getInfo(@PathVariable Long noticeId) {
        return R.ok(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysNoticeSaveQuery notice) {
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysNoticeSaveQuery notice) {
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 首页顶部公告列表（返回全部正常公告，带当前用户已读标记，最多5条）
     */
    @GetMapping("/listTop")
    public AjaxResult listTop() {
        Long userId = getUserId();
        List<SysNoticeReadVO> list = noticeReadService.selectNoticeListWithReadStatus(userId, 5);
        long unreadCount = list.stream().filter(n -> !n.isRead()).count();
        AjaxResult result = AjaxResult.success(list);
        result.put("unreadCount", unreadCount);
        return result;
    }

    /**
     * 标记公告已读
     */
    @PostMapping("/markRead")
    public R<Void> markRead(Long noticeId) {
        Long userId = getUserId();
        noticeReadService.markRead(noticeId, userId);
        return R.ok();
    }

    /**
     * 批量标记已读
     */
    @PostMapping("/markReadAll")
    public R<Void> markReadAll(String ids) {
        Long userId = getUserId();
        Long[] noticeIds = Convert.toLongArray(ids);
        noticeReadService.markReadBatch(userId, noticeIds);
        return R.ok();
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public R<Void> remove(@PathVariable Long[] noticeIds) {
        noticeReadService.deleteByNoticeIds(noticeIds);
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }

}