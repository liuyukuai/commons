package com.cutefool.commons.config.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * (系统）默认配置设置
 */
public class ConfigDefaultInitializer {

    public static void initialize(ConfigurableEnvironment environment) {
        System.setProperty("spring.flyway.ignoreMissingMigrations", "true");

        String property = environment.getProperty("management.endpoints.web.exposure.include");

        if (StringUtils.isBlank(property)) {
            // 设置开发的端点
            System.setProperty("management.endpoints.web.exposure.include", "info,health");
        }

        String consumerGroup = environment.getProperty("spring.kafka.consumer.group-id");
        if (StringUtils.isBlank(consumerGroup)) {
            // 设置开发的端点
            System.setProperty("spring.kafka.consumer.group-id", "${spring.application.name}");
        }

        // 设置密码
//        System.setProperty("spring.security.user.name", "admin");
//        System.setProperty("spring.security.password.password", "Tsingyun@2022");
//        System.setProperty("spring.security.password.roles", "ADMIN");
        // 设置mybatis的xml加载路径
        System.setProperty(" mybatis.mapper-locations", "classpath*:mapper/**/*.xml");


    }
}
