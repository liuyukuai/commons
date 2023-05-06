package com.cutefool.commons.mysql.advice;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.page.ResponseCode;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

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
public class ExceptionMysqlAdvice {

    /**
     * code 错误
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({MysqlDataTruncation.class})
    public Response<?> handle(MysqlDataTruncation e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.MYSQL_DATA_ERROR);
    }


    @ResponseBody
    @ExceptionHandler({DuplicateKeyException.class})
    public Response<String> handle(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        String message = e.getCause().getMessage();
        message = message.replaceAll("Duplicate entry", "").replaceAll("'", "").trim().split(" ")[0];
        return Response.failure(ResponseCode.DATA_IS_EXISTS.getCode(), String.format(ResponseCode.DATA_IS_EXISTS.getMessage(), message));
    }

    @ResponseBody
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public Response<String> handle(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.DATA_CAN_NOT_COMMIT.getCode(), e.getMessage());
    }

}
