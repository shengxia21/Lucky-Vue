package com.lucky.framework.handler;

import cn.hutool.http.HttpStatus;
import com.lucky.common.core.domain.AjaxResult;
import jakarta.servlet.http.HttpServletRequest;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Mybatis 异常处理器
 *
 * @author lucky
 */
@RestControllerAdvice
public class MybatisExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(MybatisExceptionHandler.class);

    /**
     * 主键或UNIQUE索引，数据重复异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public AjaxResult handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',数据库中已存在记录'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.HTTP_CONFLICT, "数据库中已存在该记录，请联系管理员确认");
    }

    /**
     * Mybatis系统异常 通用处理
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public AjaxResult handleCannotFindDataSourceException(MyBatisSystemException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',Mybatis系统异常", requestURI, e);
        return AjaxResult.error(HttpStatus.HTTP_INTERNAL_ERROR, e.getMessage());
    }

}
