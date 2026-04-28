package com.lucky.common.security.domain.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

/**
 * 认证令牌事件类
 *
 * @author lucky
 */
@Data
public class AuthenticationTokenEvent {

    private HttpServletRequest request;

    public AuthenticationTokenEvent(HttpServletRequest request) {
        this.request = request;
    }

}
