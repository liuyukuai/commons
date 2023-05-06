package com.cutefool.commons.workflow.advice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 10:47
 */
@Configuration
@ConditionalOnMissingBean(ExceptionWorkflowAdvice.class)
public class ExceptionWorkflowConfig {

    @Bean
    public ExceptionWorkflowAdvice exceptionWorkflowAdvice() {
        return new ExceptionWorkflowAdvice();
    }

}
