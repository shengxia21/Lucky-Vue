package com.lucky.common.ai.handler;

import com.lucky.common.core.domain.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * AI 流式推送异常处理器
 *
 * @author lucky
 */
@Slf4j
@Order(1)
@RestControllerAdvice
public class AiStreamExceptionHandler {

    /**
     * IO 异常处理
     */
    @ExceptionHandler(IOException.class)
    public R<Void> handleIOException(IOException e, HttpServletRequest request, HttpServletResponse response) {
        if (isClientAbort(e)) {
            log.warn("请求地址'{}',客户端已中断连接，本次流式推送终止", request.getRequestURI());
            return null;
        }
        // 流式响应中途出错时响应头已提交，无法再返回 JSON 错误结构，强行写入会触发二次异常
        if (response.isCommitted()) {
            log.error("请求地址'{}',响应已提交，流式推送发生IO异常.", request.getRequestURI(), e);
            return null;
        }
        log.error("请求地址'{}',发生IO异常.", request.getRequestURI(), e);
        return R.fail(e.getMessage());
    }

    /**
     * 判断异常是否由客户端主动断开连接引起
     * <p>Tomcat 会包装为 ClientAbortException；Undertow 直接抛出 IOException，
     * 只能沿 cause 链按典型异常文案识别（覆盖 Linux 与中英文 Windows 环境）</p>
     *
     * @param throwable 待判断异常
     * @return true 表示客户端已断开连接
     */
    private boolean isClientAbort(Throwable throwable) {
        while (throwable != null) {
            if (throwable.getClass().getName().contains("ClientAbortException")) {
                return true;
            }
            String message = throwable.getMessage();
            if (message != null) {
                String msg = message.toLowerCase();
                if (msg.contains("broken pipe") || msg.contains("connection reset")
                        || msg.contains("connection abort") || msg.contains("an established connection was aborted")
                        || msg.contains("forcibly closed by the remote host")
                        // Windows 中文环境文案
                        || message.contains("你的主机中的软件中止了一个已建立的连接")
                        || message.contains("远程主机强迫关闭了一个现有的连接")) {
                    return true;
                }
            }
            throwable = throwable.getCause();
        }
        return false;
    }

}
