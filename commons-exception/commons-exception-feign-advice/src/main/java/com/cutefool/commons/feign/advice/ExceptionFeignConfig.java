package com.cutefool.commons.feign.advice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 10:47
 */
@Configuration
@ConditionalOnMissingBean(ExceptionFeignAdvice.class)
public class ExceptionFeignConfig {

    @Bean
    public ExceptionFeignAdvice exceptionFeignAdvice() {
        return new ExceptionFeignAdvice();
    }

}
