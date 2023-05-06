/*
 *
 */
package com.cutefool.commons.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 2020/7/2 10:35 AM
 */
@Configuration
public class TestsConfig {

    @Bean
    public TestableContext testableContext() {
        return new TestableContext();
    }


    @Bean
    public Tests tests() {
        return new Tests();
    }
}
