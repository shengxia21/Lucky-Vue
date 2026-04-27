package com.lucky.common.security.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

/**
 * 退出成功事件类
 *
 * @author lucky
 */
@Data
public class LogoutSuccessEvent {

    private HttpServletRequest request;

    public LogoutSuccessEvent(HttpServletRequest request) {
        this.request = request;
    }

}
