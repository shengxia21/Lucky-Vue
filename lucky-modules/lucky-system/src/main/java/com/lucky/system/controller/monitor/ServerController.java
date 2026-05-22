package com.lucky.system.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lucky.common.core.domain.R;
import com.lucky.common.web.domain.Server;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {

    /**
     * 获取服务器信息
     */
    @SaCheckPermission("monitor:server:list")
    @GetMapping()
    public R<Server> getInfo() {
        Server server = new Server();
        server.copyTo();
        return R.ok(server);
    }

}
