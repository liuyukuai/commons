/*
 *
 */
package com.cutefool.commons.orm.influx;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 271007729@qq.com
 * @date 11/1/21 4:52 PM
 */
@Data
@ConfigurationProperties(prefix = "spring.influx")
public class InfluxProperties {

    /**
     * influx username
     */
    @Value("${spring.influx.username:tsingyun}")
    private String username;

    /**
     * influx token
     */
    @Value("${spring.influx.token:-b9AUHLdA_gjttoHod9G9hRd7O_co4GyPrncARu1QEzvvj9BEi84CgNxN2_bkyHKkQZMkz84U5k55YzfiDOZ1Q==}")
    private String token;

    /**
     * influx database
     */
    @Value("${spring.influx.database:e_demo}")
    private String database;

    /**
     * influx url
     */
    @Value("${spring.influx.url:http://172.24.0.102:8086}")
    private String url;
}
