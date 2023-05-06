/*
 *
 */
package com.cutefool.commons.api;

import com.cutefool.commons.api.yapi.YapiOperation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 2019/9/26 2:25 PM
 */
@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class ApiConfig {


    @Bean
    public ApiOperation apiOperation() {
        return new YapiOperation();
    }

}
