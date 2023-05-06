/*
 *  
 */
package com.cutefool.commons.feign;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 271007729@qq.com
 * @date 2020/1/16 5:07 PM
 */
@Data
@Component
@ConfigurationProperties(prefix = "commons.feign.rules.dev")
public class DevRuleProperties {
    /**
     * ip地址，多个逗号隔开
     */
    @Value("${commons.feign.rules.dev.ip:}")
    private String ip;
    /**
     * 是否启用开发路由规则
     */
    @Value("${commons.feign.rules.dev.enabled:false}")
    private boolean enabled;
}
