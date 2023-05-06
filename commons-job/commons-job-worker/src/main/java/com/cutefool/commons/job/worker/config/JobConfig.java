/*
 *
 */
package com.cutefool.commons.job.worker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 271007729@qq.com
 * @date 9/6/21 11:42 AM
 */
@Data
@Component
@ConfigurationProperties(prefix = "commons.job")
public class JobConfig {

    private String address;

    private String token;

    private String appId;
}
