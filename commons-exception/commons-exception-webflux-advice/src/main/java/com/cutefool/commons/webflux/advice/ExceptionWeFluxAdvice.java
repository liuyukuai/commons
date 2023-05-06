package com.cutefool.commons.webflux.advice;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Lists;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * WebFlux 全局异常处理
 *
 * @author 271007729@qq.com
 */
@Slf4j
@ControllerAdvice
public class ExceptionWeFluxAdvice {

    @ResponseBody
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<Response<String>> webExchangeBindException(WebExchangeBindException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> errors = bindingResult.getFieldErrors();
        Map<String, String> collect = Lists.empty(errors).stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return Mono.just(Response.failure(JsonUtils.toJson(collect)));
    }

    @ResponseBody
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<Response<String>> webInputException(ServerWebInputException e) {
        log.error(e.getMessage(), e);
        return Mono.just(Response.failure(ResponseCode.API_PARAM_ERROR));
    }
}
