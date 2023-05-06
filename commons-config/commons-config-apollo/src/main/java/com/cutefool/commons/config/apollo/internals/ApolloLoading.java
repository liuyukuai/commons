/*
 *
 */
package com.cutefool.commons.config.apollo.internals;

import com.ctrip.framework.apollo.ConfigService;
import com.cutefool.commons.config.apollo.constant.ApolloConstants;
import com.cutefool.commons.config.core.spi.ConfigLoading;

import java.util.Optional;

/**
 * apollo配置获取类
 *
 * @author 271007729@qq.com
 * @date 2022/7/26 1:30 PM
 */
public class ApolloLoading implements ConfigLoading {

    @Override
    public String doGet(String key) {
        return doGet(key, "");
    }

    @Override
    public String doGet(String key, String defaultValue) {
        return Optional.ofNullable(ConfigService.getConfig(ApolloConstants.DEFAULT_APPLICATION_CONFIG_NAMESPACE))
                       .map(s -> s.getProperty(key, null))
                       .orElseGet(() -> Optional.ofNullable(ConfigService.getConfig(ApolloConstants.DEFAULT_PUBLIC_CONFIG_NAMESPACE))
                                                .map(s -> s.getProperty(key, defaultValue))
                                                .orElse(defaultValue));

    }

    @Override
    public String name() {
        return ApolloConstants.APOLLO_NAME;
    }
}
