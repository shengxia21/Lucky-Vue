package com.lucky.system.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.lucky.common.core.constant.CacheConstants;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.domain.dto.UserOnlineDTO;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.bean.BeanUtils;
import com.lucky.common.log.annotation.Log;
import com.lucky.common.log.enums.BusinessType;
import com.lucky.common.mybatis.core.controller.BaseController;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.redis.utils.RedisCache;
import com.lucky.system.domain.vo.userOnline.SysUserOnlineVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author lucky
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {

    @Resource
    private RedisCache redisCache;

    /**
     * 获取在线用户列表
     */
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/list")
    public TableDataInfo<SysUserOnlineVo> list(String ipaddr, String userName) {
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        List<UserOnlineDTO> userOnlineList = new ArrayList<>();
        for (String key : keys) {
            String token = StringUtils.substringAfterLast(key, ":");
            // 如果已经过期则跳过
            if (StpUtil.stpLogic.getTokenActiveTimeoutByToken(token) < -1) {
                continue;
            }
            // 获取缓存对象
            UserOnlineDTO userOnline = redisCache.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY + token);
            // 如果获取到的对象为 null（可能是刚好过期或被删除），直接跳过，不加入列表
            if (userOnline == null) {
                continue;
            }
            userOnlineList.add(userOnline);
        }
        if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
            userOnlineList = userOnlineList.stream().filter(userOnline ->
                    StringUtils.equals(ipaddr, userOnline.getIpaddr()) && StringUtils.equals(userName, userOnline.getUserName())
            ).toList();
        } else if (StringUtils.isNotEmpty(ipaddr)) {
            userOnlineList = userOnlineList.stream().filter(userOnline ->
                    StringUtils.equals(ipaddr, userOnline.getIpaddr())
            ).toList();
        } else if (StringUtils.isNotEmpty(userName)) {
            userOnlineList = userOnlineList.stream().filter(userOnline ->
                    StringUtils.equals(userName, userOnline.getUserName())
            ).toList();
        }
        Collections.reverse(userOnlineList);
        List<SysUserOnlineVo> list = new ArrayList<>();
        for (UserOnlineDTO userOnlineDTO : userOnlineList) {
            SysUserOnlineVo vo = new SysUserOnlineVo();
            BeanUtils.copyProperties(userOnlineDTO, vo);
            list.add(vo);
        }
        return TableDataInfo.build(list);
    }

    /**
     * 强退用户
     */
    @SaCheckPermission("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public R<Void> forceLogout(@PathVariable String tokenId) {
        try {
            StpUtil.kickoutByTokenValue(tokenId);
        } catch (NotLoginException ignored) {
        }
        return R.ok();
    }

}