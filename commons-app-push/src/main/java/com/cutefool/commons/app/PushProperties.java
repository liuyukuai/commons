/*
 *
 */
package com.cutefool.commons.app;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 271007729@qq.com
 * @date 2020/4/23 11:29 AM
 */
@Data
@ConfigurationProperties(prefix = "commons.app.push")
public class PushProperties {

    @Value("${commons.app.push.enabled:false}")
    private Boolean enabled;


    @Value("${commons.app.push.type}")
    private String type;
}
