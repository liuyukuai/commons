/*
 *
 */
package com.cute.commons.weather;

import com.cute.commons.weather.lib.WeatherProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 2020/7/2 10:29 AM
 */
@Configuration
@EnableConfigurationProperties(WeatherProperties.class)
public class WeatherConfig {


    @Bean
    public Weathers weathers() {
        return new Weathers();
    }
}
