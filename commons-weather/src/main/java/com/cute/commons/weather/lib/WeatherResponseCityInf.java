/*
 *
 */
package com.cute.commons.weather.lib;

import com.cute.commons.weather.CityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2021/6/7 下午3:42
 */
@Data
public class WeatherResponseCityInf {

    @JsonProperty("code")
    private String code;

    @JsonProperty("location")
    List<CityInfo> cityInfos;
}
