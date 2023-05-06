/*
 *
 */
package com.cutefool.commons.kafka;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 271007729@qq.com
 * @date 2020-08-10 17:25
 */
@Data
@ConfigurationProperties(prefix = "commons.kafka")
public class CommonsKafkaProperties {

    @Value("${commons.kafka.enabled:true}")
    private Boolean enabled;
}