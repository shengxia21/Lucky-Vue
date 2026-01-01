package com.lucky.web.controller.monitor;

import com.lucky.common.annotation.Log;
import com.lucky.common.constant.CacheConstants;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.domain.model.LoginUser;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.core.redis.RedisCache;
import com.lucky.common.enums.BusinessType;
import com.lucky.common.utils.StringUtils;
import com.lucky.system.domain.SysUserOnline;
import com.lucky.system.service.ISysUserOnlineService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {

    @Resource
    private ISysUserOnlineService userOnlineService;

    @Resource
    private RedisCache redisCache;

    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ipaddr, String userName) {
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
                userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
            } else if (StringUtils.isNotEmpty(ipaddr)) {
                userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
            } else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getUser())) {
                userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
            } else {
                userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId) {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return success();
    }

}
