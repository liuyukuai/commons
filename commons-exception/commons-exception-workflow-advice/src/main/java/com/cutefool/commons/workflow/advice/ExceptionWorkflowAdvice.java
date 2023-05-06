package com.cutefool.commons.workflow.advice;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.page.ResponseCode;
import org.activiti.engine.ActivitiException;
import org.springframework.core.annotation.Order;
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
public class ExceptionWorkflowAdvice {

    /**
     * 工作流异常处理
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({ActivitiException.class})
    public Response<?> handle(ActivitiException e) {
        Throwable cause = e.getCause();
        log.error(e.getMessage(), e);
        // 如果异常原因是BizException
        if (cause instanceof BizException) {
            return Response.failure(((BizException) cause).getCode(), cause.getMessage());
        }
        return Response.failure(ResponseCode.WORKFLOW_ERROR.getCode(), ResponseCode.WORKFLOW_ERROR.getMessage());
    }
}
