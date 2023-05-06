/*
 *  
 */
package com.cute.commons.weather.lib;

import com.cute.commons.weather.WeatherWarning;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2021/6/1 下午2:26
 */
@Data
public class WeatherResponseWarning {

    @JsonProperty("warning")
    private List<WeatherWarning> weatherWarningList;
    @JsonProperty("code")
    private String code;
    @JsonProperty("updateTime")
    private String updateTime;

}
