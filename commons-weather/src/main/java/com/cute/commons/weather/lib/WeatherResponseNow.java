/*
 *
 */
package com.cute.commons.weather.lib;

import com.cute.commons.weather.WeatherNow;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 271007729@qq.com
 * @date 2021/5/31 下午5:49
 */
@Data
public class WeatherResponseNow {

    @JsonProperty("now")
    private WeatherNow weatherNow;
    @JsonProperty("code")
    private String code;
    @JsonProperty("updateTime")
    private String updateTime;

}
