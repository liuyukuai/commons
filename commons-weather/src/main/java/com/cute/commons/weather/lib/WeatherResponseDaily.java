/*
 *
 */
package com.cute.commons.weather.lib;

import com.cute.commons.weather.WeatherDaily;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2021/5/31 下午6:44
 */
@Data
public class WeatherResponseDaily {

    @JsonProperty("daily")
    List<WeatherDaily> weatherDailyList;
    @JsonProperty("code")
    private String code;
    @JsonProperty("updateTime")
    private String updateTime;

}
