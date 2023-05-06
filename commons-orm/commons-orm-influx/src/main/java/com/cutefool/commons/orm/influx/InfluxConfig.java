/*
 *  
 */
package com.cutefool.commons.orm.influx;

import com.cutefool.commons.orm.influx.support.InfluxTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author 271007729@qq.com
 * @date 11/1/21 4:54 PM
 */
@Component
@Configuration
@EnableConfigurationProperties(value = InfluxProperties.class)
public class InfluxConfig {

    @Bean
    public InfluxTemplate influxTemplate(InfluxProperties influxProperties) {
        return new InfluxTemplate(influxProperties);
    }

}
