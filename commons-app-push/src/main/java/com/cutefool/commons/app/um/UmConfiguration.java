/*
 *
 */
package com.cutefool.commons.app.um;

import com.cutefool.commons.app.PushService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 271007729@qq.com
 * @date 2020/4/23 11:34 AM
 */
@EnableConfigurationProperties(UmProperties.class)
@ComponentScan(basePackages = "com.cutefool.commons.plugin.app.um")
@ConditionalOnProperty(name = "commons.app.push.type", havingValue = "um")
public class UmConfiguration {

    @Bean
    public PushService pushService() {
        return new UmPushServiceImpl();
    }
}
