package com.cutefool.commons.advice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 10:47
 */
@Configuration
@ConditionalOnMissingBean(ExceptionAdvice.class)
public class ExceptionConfig {

    @Bean
    public ExceptionAdvice exceptionAdvice() {
        return new ExceptionAdvice();
    }
}
