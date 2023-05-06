/*
 *
 */
package com.cute.commons.weather;

import lombok.Data;

/**
 * @author 271007729@qq.com
 * @date 2021/6/3 下午7:22
 */
@Data
public class WeatherNow {

    /**
     * 观测时间
     */
    private String obsTime;
    /**
     * 温度
     */
    private String temp;
    /**
     * 体感温度
     */
    private String feelsLike;
    /**
     * 天气图标
     */
    private String icon;
    /**
     * 天气文字描述
     */
    private String text;
    /**
     * 风向360
     */
    private String wind360;
    /**
     * 风向
     */
    private String windDir;
    /**
     * 风力
     */
    private String windScale;
    /**
     * 风速 公里/小时
     */
    private String windSpeed;
    /**
     * 相对湿度 %
     */
    private String humidity;
    /**
     * 小时降水量 默认单位：毫米
     */
    private String precip;
    /**
     * 压力 默认单位：百帕
     */
    private String pressure;
    /**
     * 能见度 默认单位：公里
     */
    private String vis;
    /**
     * 云量 %
     */
    private String cloud;
    /**
     * 露点温度
     */
    private String dew;

}
