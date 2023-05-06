package com.cutefool.commons.oauth.advice;

import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.page.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


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
public class ExceptionOauthAdvice {

    /**
     * code 错误
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({InvalidGrantException.class})
    public Response<?> handle(InvalidGrantException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.OAUTH_CODE_ERROR);
    }

    /**
     * Client 错误
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({BadClientCredentialsException.class})
    public Response<?> handle(BadClientCredentialsException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.OAUTH_CLIENT_ERROR);
    }

    /**
     * Client 错误
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({InvalidClientException.class})
    public Response<?> handle(InvalidClientException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.OAUTH_CLIENT_ERROR);
    }

    /**
     * not unauthorized 错误
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({ClientAuthenticationException.class})
    public Response<?> handle(ClientAuthenticationException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.OAUTH_NOT_UNAUTHORIZED_ERROR);
    }

    /**
     * grant type 错误
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({UnsupportedGrantTypeException.class})
    public Response<?> handle(UnsupportedGrantTypeException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.OAUTH_NOT_SUPPORT_GRANT_TYPE_ERROR);
    }


    /**
     * token错误
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({InvalidTokenException.class})
    public Response<?> handle(InvalidTokenException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.OAUTH_TOKEN_ERROR);
    }

    /**
     * scope 错误
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({InvalidScopeException.class})
    public Response<?> handle(InvalidScopeException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.OAUTH_SCOPE_ERROR);
    }

    /**
     * 认证信息错误 错误
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({BadCredentialsException.class})
    public Response<?> handle(BadCredentialsException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.OAUTH_BAD_CREDENTIALS_ERROR.getCode(), String.format(ResponseCode.OAUTH_BAD_CREDENTIALS_ERROR.getMessage(), e.getMessage()));
    }


}
