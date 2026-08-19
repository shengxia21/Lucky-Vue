package com.lucky.common.ai.handler;

import com.lucky.common.core.domain.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

import java.io.IOException;

/**
 * AI 流式推送异常处理器
 * <p>
 * SSE 流式响应（text/event-stream）推送期间，前端停止生成或关闭页面会中断连接，
 * 服务端继续向已断开的 socket 写数据将抛出 IOException（如 Windows 下的
 * "你的主机中的软件中止了一个已建立的连接"）。此类异常属于正常的客户端断连，
 * 且此时响应头已提交为 text/event-stream、无法再写入 JSON 结构，
 * 必须在此静默处理，否则会落入 {@code GlobalExceptionHandler} 兜底逻辑，
 * 进而触发 "No converter for R with preset Content-Type 'text/event-stream'" 二次异常。
 * <p>
 * 优先级需高于 {@code GlobalExceptionHandler}（@Order(2)），
 * 否则 IOException 会先被其兜底处理，本处理器不会生效。
 *
 * @author lucky
 */
@Slf4j
@Order(1)
@RestControllerAdvice
public class AiStreamExceptionHandler {

    /**
     * 响应已不可用（Spring 6.1+：异步请求中客户端断连后响应即标记为不可用）
     * <p>客户端必然已断开，静默处理</p>
     */
    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public R<Void> handleAsyncRequestNotUsable(AsyncRequestNotUsableException e, HttpServletRequest request) {
        log.warn("请求地址'{}',客户端已断开连接，本次流式推送终止", request.getRequestURI());
        // 返回 null 且响应已提交时，Spring 视为已处理，不会再尝试写入响应体
        return null;
    }

    /**
     * IO 异常处理
     * <p>客户端断连引起的 IO 异常仅记录 WARN 日志；其余 IO 异常在响应未提交时正常返回错误信息</p>
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
