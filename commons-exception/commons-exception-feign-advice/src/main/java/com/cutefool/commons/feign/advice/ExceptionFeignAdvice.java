package com.cutefool.commons.feign.advice;

import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.page.ResponseCode;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.UnknownHostException;

/**
 * global exception handler
 *
 * @author 271007729@qq.com
 * @date 2019-07-02 10:45
 */
@Slf4j
@Order(1)
@ControllerAdvice
@SuppressWarnings("all")
public class ExceptionFeignAdvice {

    /**
     * code 错误
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({RetryableException.class})
    public Response<?> handle(RetryableException e) {
        log.error(e.getMessage(), e);
        Throwable cause = e.getCause();
        if (cause instanceof java.net.UnknownHostException) {
            UnknownHostException unknown = (UnknownHostException) cause;
            String message = unknown.getMessage();
            return Response.failure(ResponseCode.SERVICE_IS_DOWN.getCode(), String.format(ResponseCode.SERVICE_IS_DOWN.getMessage(), message));
        }
        String message = e.getMessage();
        return Response.failure(ResponseCode.SERVICE_IS_DOWN.getCode(), message);
    }
}
