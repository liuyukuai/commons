/*
 *
 */
package com.cute.commons.weather;

import com.cute.commons.weather.lib.*;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.http.Http;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 天气工具类
 *
 * @author 271007729@qq.com
 * @date 2020/3/10 3:43 PM
 */
@Data
@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class Weathers {

    @Resource
    private WeatherProperties weatherProperties;

    /**
     * 天气接口默认超时时间
     */
    Duration weatherTimeOut = Duration.ofSeconds(5L);

    /**
     * 加载固定地理位置天气
     *
     * @return 天气
     */
    public WeatherNow load() {
        return load(weatherProperties.getLocation());
    }

    /**
     * 加载固定地理位置七日天气
     *
     * @return 七日天气
     */
    public List<WeatherDaily> loadDaily() {
        return loadDaily(weatherProperties.getLocation());
    }

    /**
     * 加载固定地理位置预警信息
     *
     * @return 天气预警信息
     */
    public List<WeatherWarning> loadWarningNow() {
        return loadWarningNow(weatherProperties.getLocation());
    }

    /**
     * 加载固定地理位置城市信息
     *
     * @return 城市信息
     */
    public List<CityInfo> loadCityInfo() {
        return loadCityInfo(weatherProperties.getLocation());
    }

    /**
     * 加载指定地理位置天气,城市代码
     *
     * @return 天气
     */
    public WeatherNow load(String location) {
        String uri = String.format(WeatherConstants.WEATHER_ADDRESS, "now", location, weatherProperties.getKey());
        WeatherResponseNow response = Http.newInstance().get(uri, WeatherResponseNow.class);
        return Optional.ofNullable(response).map(WeatherResponseNow::getWeatherNow).orElse(new WeatherNow());
    }

    /**
     * 加载给定位置的天气预警信息
     *
     * @param location 城市位置
     * @return 城市信息
     */
    public List<CityInfo> loadCityInfo(String location) {
        String uri = String.format(WeatherConstants.CITY_ADDRESS, location, weatherProperties.getKey());
        WeatherResponseCityInf response = Http.newInstance().get(uri, WeatherResponseCityInf.class);
        return Optional.ofNullable(response).map(WeatherResponseCityInf::getCityInfos).orElse(new ArrayList<>());
    }

    /**
     * 查询7日天气
     *
     * @param location 城市代码或者经纬度拼接
     * @return 七日天气列表
     */
    public List<WeatherDaily> loadDaily(String location) {

        String uri = String.format(WeatherConstants.WEATHER_ADDRESS, "7d", location, weatherProperties.getKey());
        WeatherResponseDaily response = Http.newInstance().get(uri, WeatherResponseDaily.class);
        return Optional.ofNullable(response).map(WeatherResponseDaily::getWeatherDailyList).orElse(new ArrayList<>());
    }

    /**
     * 查询实时预警信息
     *
     * @param location 城市cid或经纬度拼接
     * @return 预警信息列表
     */
    public List<WeatherWarning> loadWarningNow(String location) {

        String uri = String.format(WeatherConstants.WARNING_ADDRESS, location, weatherProperties.getKey());
        WeatherResponseWarning response = Http.newInstance().get(uri, WeatherResponseWarning.class);
        if (Objects.nonNull(response) && Lists.iterable(response.getWeatherWarningList())) {
            return response.getWeatherWarningList();
        }
        return new ArrayList<>();

    }

    /**
     * 加载指定地理位置天气,经纬度
     *
     * @return 天气
     */
    public WeatherNow load(BigDecimal longitude, BigDecimal latitude) {
        //经纬度拼接
        if (Objects.nonNull(longitude) && Objects.nonNull(latitude)) {
            return this.load(longitude.setScale(2, BigDecimal.ROUND_DOWN) + "," + latitude.setScale(2, BigDecimal.ROUND_DOWN));
        }
        return new WeatherNow();
    }

    /**
     * 查询7日天气,经纬度
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 七日天气列表
     */
    public List<WeatherDaily> loadDaily(BigDecimal longitude, BigDecimal latitude) {
        //经纬度拼接
        if (Objects.nonNull(longitude) && Objects.nonNull(latitude)) {
            return this.loadDaily(longitude.setScale(2, BigDecimal.ROUND_DOWN) + "," + latitude.setScale(2, BigDecimal.ROUND_DOWN));
        }
        return new ArrayList<>();
    }

    /**
     * 加载给定经纬度的天气预警信息
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 天气预警信息
     */
    public List<WeatherWarning> loadWarningNow(BigDecimal longitude, BigDecimal latitude) {
        //经纬度拼接
        if (Objects.nonNull(longitude) && Objects.nonNull(latitude)) {
            return this.loadWarningNow(longitude.setScale(2, BigDecimal.ROUND_DOWN) + "," + latitude.setScale(2, BigDecimal.ROUND_DOWN));
        }
        return new ArrayList<>();
    }

    /**
     * 加载给定经纬度的天气预警信息
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 城市信息
     */
    public List<CityInfo> loadCityInfo(BigDecimal longitude, BigDecimal latitude) {
        //经纬度拼接
        if (Objects.nonNull(longitude) && Objects.nonNull(latitude)) {
            return this.loadCityInfo(longitude.setScale(2, BigDecimal.ROUND_DOWN) + "," + latitude.setScale(2, BigDecimal.ROUND_DOWN));
        }
        return new ArrayList<>();
    }

}
