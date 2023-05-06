package com.cutefool.commons.advice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Strings;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * global exception handler
 *
 * @author 271007729@qq.com
 * @date 2019-07-02 10:45
 */
@Slf4j
@ControllerAdvice
@SuppressWarnings("unused")
public class ExceptionAdvice {

    /**
     * 参数类型不匹配
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Response<?> handle(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> jsonObject = new HashMap<>(fieldErrors.size());
        fieldErrors.forEach((item) -> jsonObject.put(item.getField(), item.getDefaultMessage()));
        return Response.failure(ResponseCode.API_PARAM_ERROR.getCode(), JsonUtils.toJson(jsonObject));
    }

    /**
     * 参数类型不匹配
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({UnexpectedTypeException.class})
    public Response<?> handle(UnexpectedTypeException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 请求体为空
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Response<?> handle(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof MismatchedInputException) {
            List<JsonMappingException.Reference> paths = ((MismatchedInputException) cause).getPath();
            String message = Lists.empty(paths)
                                  .stream()
                                  .map(JsonMappingException.Reference::getFieldName)
                                  .map(name -> name + ResponseCode.API_PARAM_ERROR.getMessage())
                                  .collect(Collectors.joining(Strings.COMMA));
            return Response.failure(ResponseCode.API_PARAM_ERROR.getCode(), message);
        }
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_BODY_IS_EMPTY.getCode(), ResponseCode.API_BODY_IS_EMPTY.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    public Response<?> handle(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        // 只取第一个错误信息给前端，不显示所有，对用户友好
        return Response.failure(ResponseCode.API_PARAM_ERROR.getCode(), constraintViolations.stream()
                                                                                            .findFirst()
                                                                                            .map(ConstraintViolation::getMessage)
                                                                                            .orElse(ResponseCode.SYSTEM_ERROR.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler({BindException.class})
    public Response<?> handle(BindException e) {
        log.error(e.getMessage(), e);
        Map<String, Object> jsonObject = new HashMap<>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach((item) -> jsonObject.put(item.getField(), item.getDefaultMessage()));
        return Response.failure(ResponseCode.API_PARAM_ERROR.getCode(), JsonUtils.toJson(jsonObject));
    }

    /**
     * user center exception handler
     *
     * @param e exception
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({BizException.class})
    public Response<?> handle(BizException e) {
        log.error("biz exception code = [{}], msg = [{}] ", e.getCode(), e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    /**
     * default exception  handler
     *
     * @param e exception
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({RuntimeException.class})
    public Response<?> handle(RuntimeException e) {
        log.error("default exception msg = [{}] ", e.getMessage(), e);
        return Response.failure(ResponseCode.SYSTEM_ERROR.getCode(), ResponseCode.SYSTEM_ERROR.getMessage());
    }
}
