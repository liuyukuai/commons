package com.cutefool.commons.servlet.advice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 10:47
 */
@Configuration
@ConditionalOnMissingBean(ExceptionServletAdvice.class)
public class ExceptionServletConfig {

    @Bean
    public ExceptionServletAdvice exceptionServletAdvice() {
        return new ExceptionServletAdvice();
    }

}
