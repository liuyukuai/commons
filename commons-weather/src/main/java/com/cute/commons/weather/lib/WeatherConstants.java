/*
 *
 */
package com.cute.commons.weather.lib;

/**
 * 天气配置
 *
 * @author 271007729@qq.com
 * @date 2020/3/10 3:43 PM
 */
public final class WeatherConstants {
    /**
     * 天气接口地址
     */
    public static final String WEATHER_ADDRESS = "https://devapi.qweather.com/v7/weather/%s?location=%s&key=%s";

    /**
     * 天气预警地址
     */
    public static final String WARNING_ADDRESS = "https://devapi.qweather.com/v7/warning/now?location=%s&key=%s";

    /**
     * 城市地址
     */
    public static final String CITY_ADDRESS = "https://geoapi.qweather.com/v2/city/lookup?location=%s&key=%s";

}
