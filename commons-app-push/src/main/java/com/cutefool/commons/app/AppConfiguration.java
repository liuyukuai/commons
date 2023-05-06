/*
 *
 */
package com.cutefool.commons.app;

import com.cutefool.commons.app.um.UmConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 2020/4/23 9:37 AM
 */

@Configuration
@EnableConfigurationProperties(PushProperties.class)
@ImportAutoConfiguration(value = {UmConfiguration.class})
@ConditionalOnProperty(name = "commons.app.push.enabled", havingValue = "true", matchIfMissing = true)
public class AppConfiguration {


}
