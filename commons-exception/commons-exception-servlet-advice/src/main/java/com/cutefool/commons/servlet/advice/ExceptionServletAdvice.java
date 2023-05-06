package com.cutefool.commons.servlet.advice;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.page.ResponseCode;
import org.springframework.core.annotation.Order;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;


/**
 * global exception handler
 *
 * @author 271007729@qq.com
 * @date 2019-07-02 10:45
 */
@Slf4j
@Order(1)
@ControllerAdvice
@SuppressWarnings("unused")
public class ExceptionServletAdvice {

    /**
     * 请求方式不匹配
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Response<?> handle(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_UN_SUPPORT_METHOD.getCode(), String.format(ResponseCode.API_UN_SUPPORT_METHOD.getMessage(), e.getMethod()));
    }

    /**
     * 参数异常
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Response<?> handle(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_PARAM_IS_EMPTY.getCode(), String.format(ResponseCode.API_PARAM_IS_EMPTY.getMessage(), e.getParameterName()));
    }

    /**
     * 参数类型不匹配
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public Response<?> handle(MethodArgumentTypeMismatchException e) {
        Executable executable = e.getParameter().getExecutable();
        Method method = e.getParameter().getMethod();
        log.error("executable = {} method = {}", executable, method, e);
        return Response.failure(ResponseCode.API_PARAM_TYPE_ERROR.getCode(), String.format(ResponseCode.API_PARAM_TYPE_ERROR.getMessage(), e.getName(), e.getRequiredType()));
    }

    /**
     * 不支持的媒体类型
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public Response<?> handle(HttpMediaTypeNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_UN_SUPPORT_MEDIA_TYPE.getCode(), String.format(ResponseCode.API_UN_SUPPORT_MEDIA_TYPE.getMessage(), e.getContentType()));
    }

    /**
     * 请求资源不存在
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({NoHandlerFoundException.class})
    public Response<?> handle(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_NOT_EXISTS.getCode(), String.format(ResponseCode.API_NOT_EXISTS.getMessage(), e.getRequestURL(), e.getHttpMethod()));
    }


    @ResponseBody
    @ExceptionHandler({ServletRequestBindingException.class})
    public Response<?> handle(ServletRequestBindingException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_PARAM_ERROR.getCode(), ResponseCode.API_PARAM_ERROR.getMessage());
    }

    /**
     * 其他servlet exception 处理
     *
     * @param e exception
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({ServletException.class})
    public Response<?> handle(ServletException e) {
        log.error("default ServletException msg = [{}] ", e.getMessage(), e);
        return Response.failure(ResponseCode.SYSTEM_ERROR.getCode(), ResponseCode.SYSTEM_ERROR.getMessage());
    }
}
