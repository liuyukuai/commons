package com.cutefool.commons.webflux.advice;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

/**
 * webFlux全局异常处理器
 *
 * @author 271007729@qq.com
 */

@Slf4j
public class ErrorWebFluxExceptionHandler extends DefaultErrorWebExceptionHandler {

    private static final String msg = "Unable to find instance for";

    @SuppressWarnings("WeakerAccess")
    public ErrorWebFluxExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = this.getError(request);
        log.error(error.getMessage(), error);
        if (error instanceof BizException) {
            BizException cast = (BizException) error;
            Response<ErrorData> response = Response.failure(cast.getCode(), cast.getMessage());
            return JsonUtils.toMap(response).orElse(Collections.emptyMap());
        }
        if (error instanceof NotFoundException) {
            NotFoundException notFound = (NotFoundException) error;
//            503 SERVICE_UNAVAILABLE "Unable to find instance for tsingyun-message"
            String reason = notFound.getReason();
            String substring = StringUtils.isNotBlank(reason) ? reason.substring(reason.lastIndexOf(msg) + msg.length()) : reason;
            return JsonUtils.toMap(Response.failure(ResponseCode.SERVICE_IS_DOWN.getCode(), String.format(ResponseCode.SERVICE_IS_DOWN.getMessage(), substring)))
                            .orElse(Collections.emptyMap());

        }

        Response<ErrorData> response = Response.failure(new ErrorData(request.path(), request.methodName(), error.getMessage()), ResponseCode.SYSTEM_ERROR.getCode(), ResponseCode.SYSTEM_ERROR.getMessage());
        return JsonUtils.toMap(response).orElse(Collections.emptyMap());
    }

//    @Override
//    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
//        Throwable error = this.getError(request);
//        log.error(error.getMessage(), error);
//        if (error instanceof BizException) {
//            BizException cast = (BizException) error;
//            Response<ErrorData> response = Response.failure(cast.getCode(), cast.getMessage());
//            return JsonUtils.toMap(response).orElse(Collections.emptyMap());
//        }
//        Response<ErrorData> response = Response.failure(new ErrorData(request.path(), request.methodName(), error.getMessage()), ResponseCode.SYSTEM_ERROR.getCode(), ResponseCode.SYSTEM_ERROR.getMessage());
//        return JsonUtils.toMap(response).orElse(Collections.emptyMap());
//    }

    @Override
    @SuppressWarnings("ALL")
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        // 所有的路由
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        exchange.getResponse().getHeaders().set("access-control-allow-origin", "*");
        return super.handle(exchange, throwable);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        return super.renderErrorResponse(request);
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.OK.value();
    }

    public static void main(String[] args) {
        String message = " 503 SERVICE_UNAVAILABLE \"Unable to find instance for tsingyun-message\"";

        String substring = message.substring(message.lastIndexOf(""), message.length());
        System.out.println(substring);

    }
}
