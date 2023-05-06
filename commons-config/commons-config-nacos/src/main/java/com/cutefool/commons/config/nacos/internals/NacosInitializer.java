/*
 *
 */
package com.cutefool.commons.config.nacos.internals;

import com.cutefool.commons.config.nacos.constants.NacosConstants;
import com.cutefool.commons.config.core.Constants;
import com.cutefool.commons.config.core.spi.Initializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * nacos初始化类
 *
 * @author 271007729@qq.com
 * @date 2022/7/26 1:26 AM
 */

public class NacosInitializer implements Initializer {

    @Override
    public String name() {
        return "nacos";
    }

    @Override
    public void clean(ConfigurableEnvironment environment) {
        System.setProperty(NacosConstants.NACOS_DISCOVERY_ENABLE_KEY, String.valueOf(Boolean.FALSE));
        System.setProperty(NacosConstants.NACOS_CONFIG_ENABLE_KEY, String.valueOf(Boolean.FALSE));

        Map<String, Object> envMap = new HashMap<>();
        envMap.put(NacosConstants.NACOS_DISCOVERY_ENABLE_KEY, String.valueOf(Boolean.FALSE));
        envMap.put(NacosConstants.NACOS_CONFIG_ENABLE_KEY, String.valueOf(Boolean.FALSE));
        // 设置环境的值
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new MapPropertySource("mapSource", envMap));

    }

    @Override
    public void initialize(ConfigurableEnvironment environment) {
        Objects.requireNonNull(environment);
        System.setProperty(NacosConstants.NACOS_DISCOVERY_ENABLE_KEY, String.valueOf(Boolean.TRUE));
        System.setProperty(NacosConstants.NACOS_CONFIG_ENABLE_KEY, String.valueOf(Boolean.TRUE));

        String address = System.getProperty(Constants.CONFIG_META_KEY);
        if (StringUtils.isNotBlank(address)) {
            System.setProperty(NacosConstants.NACOS_DISCOVERY_SERVER_KEY, address);
            System.setProperty(NacosConstants.NACOS_CONFIG_SERVER_KEY, address);
        }

        String namespaces = System.getProperty(Constants.CONFIG_NAMESPACE_KEY);
        if (StringUtils.isNotBlank(namespaces)) {
            System.setProperty(NacosConstants.NACOS_DISCOVERY_NAMESPACE_KEY, namespaces);
            System.setProperty(NacosConstants.NACOS_CONFIG_NAMESPACE_KEY, namespaces);
        }

        Map<String, Object> envMap = new HashMap<>();
        envMap.put(NacosConstants.NACOS_DISCOVERY_ENABLE_KEY, String.valueOf(Boolean.TRUE));
        envMap.put(NacosConstants.NACOS_CONFIG_ENABLE_KEY, String.valueOf(Boolean.TRUE));

        if (StringUtils.isNotBlank(address)) {
            envMap.put(NacosConstants.NACOS_DISCOVERY_SERVER_KEY, address);
            envMap.put(NacosConstants.NACOS_CONFIG_SERVER_KEY, address);
        }

        if (StringUtils.isNotBlank(namespaces)) {
            envMap.put(NacosConstants.NACOS_DISCOVERY_NAMESPACE_KEY, namespaces);
            envMap.put(NacosConstants.NACOS_CONFIG_NAMESPACE_KEY, namespaces);
        }

        // 设置环境的值
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new MapPropertySource("mapSource", envMap));
    }
}
