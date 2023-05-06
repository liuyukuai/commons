/*
 *  
 */
package com.cutefool.commons.config.core.spi;

import com.cutefool.commons.config.core.Constants;

/**
 * 配置获取类
 *
 * @author 271007729@qq.com
 * @date 2022/7/26 1:27 PM
 */
public interface ConfigLoading extends Named {

    String doGet(String key);

    String doGet(String key, String defaultValue);

    static String get(String key, String defaultValue) {
        String property = System.getProperty(Constants.COMMONS_CONFIG_TYPE);
        ConfigLoading configLoading = SpiLoader.load(property, ConfigLoading.class);
        return configLoading.doGet(key, defaultValue);
    }
}
