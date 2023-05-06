package com.cutefool.commons.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 安全配置
 *
 * @author 271007729@qq.com
 * @date 2019-07-24 03:54
 */
@Data
@Component
@ConfigurationProperties(prefix = "commons.security.access")
public class SecurityProperties {
    /**
     * 允许的 origin
     */
    @Value("${commons.security.access.allowedOrigin:*}")
    private String allowedOrigin;

    /**
     * 允许的请求方式
     */
    @Value("${commons.security.access.allowedMethod:GET,OPTIONS,POST,PUT,DELETE}")
    private String allowedMethod;

    /**
     * 允许的头信息
     */

    @Value("${commons.security.access.allowedHeader:*}")
    private String allowedHeader;
}
