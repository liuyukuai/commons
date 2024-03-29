package com.cutefool.commons.snowflake;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * id生成器配置
 *
 * @author 271007729@qq.com
 */
@Configuration
public class IdWorkerConfiguration {

    @Resource
    private IdWorkerProperties properties;

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(properties.getWorkerId(), properties.getDataCenterId());
    }
}
