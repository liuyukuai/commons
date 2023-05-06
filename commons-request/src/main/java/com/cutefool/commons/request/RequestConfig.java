/*
 *  
 */
package com.cutefool.commons.request;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * request config
 *
 * @author 271007729@qq.com
 */
@Configuration
public class RequestConfig {

    @Bean
    public DataTransferInterceptor dataTransferInterceptor() {
        return new DataTransferInterceptor();
    }

//    @Bean
//    public HystrixConcurrencyStrategy hystrixConcurrencyStrategy() {
//        return new RequestHystrixConcurrencyStrategy();
//    }

}
