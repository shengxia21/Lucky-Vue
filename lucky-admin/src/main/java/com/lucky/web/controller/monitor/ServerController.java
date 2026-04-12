package com.lucky.web.controller.monitor;

import com.lucky.common.core.domain.R;
import com.lucky.framework.web.domain.Server;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public R<Server> getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return R.ok(server);
    }

}
