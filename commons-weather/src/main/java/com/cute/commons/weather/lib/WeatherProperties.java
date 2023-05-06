/*
 *
 */
package com.cute.commons.weather.lib;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 天气配置
 *
 * @author 271007729@qq.com
 * @date 2020/3/10 3:43 PM
 */
@Data
@Component
@ConfigurationProperties(prefix = "commons.weather")
public class WeatherProperties {

    /**
     * 应用key
     */
    @Value("${commons.weather.key:470a2905a6324461893cd836eb3097c4}")
    private String key;

    /**
     * 默认地址
     */
    @Value("${commons.weather.location:119.15,32.26}")
    private String location;
}
