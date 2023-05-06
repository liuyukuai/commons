/*
 *
 */
package com.cutefool.commons.config.apollo.internals;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.spring.config.PropertySourcesConstants;
import com.cutefool.commons.config.apollo.constant.ApolloConstants;
import com.cutefool.commons.config.core.Constants;
import com.cutefool.commons.config.core.spi.Initializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 271007729@qq.com
 * @date 2022/7/26 1:26 AM
 */

public class ApolloInitializer implements Initializer {

    @Override
    public String name() {
        return ApolloConstants.APOLLO_NAME;
    }

    @Override
    public void clean(ConfigurableEnvironment environment) {
        // 配置类型
        System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, String.valueOf(Boolean.FALSE));
        System.setProperty("eureka.client.enabled", String.valueOf(Boolean.FALSE));

        Map<String, Object> envMap = new HashMap<>();
        envMap.put("eureka.client.enabled", String.valueOf(Boolean.FALSE));
        envMap.put(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, String.valueOf(Boolean.FALSE));
        // 设置环境的值
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new MapPropertySource("mapSource", envMap));
    }

    @Override
    public void initialize(ConfigurableEnvironment environment) {
        // 设置系统的值
        System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, String.valueOf(Boolean.TRUE));
        System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_EAGER_LOAD_ENABLED, String.valueOf(Boolean.TRUE));
        System.setProperty("eureka.client.enabled", String.valueOf(Boolean.TRUE));
        Map<String, Object> envMap = new HashMap<>();

        String address = System.getProperty(Constants.CONFIG_META_KEY);
        if (StringUtils.isNotBlank(address)) {
            System.setProperty(ConfigConsts.APOLLO_META_KEY, address);
            envMap.put(ConfigConsts.APOLLO_META_KEY, address);
        }

        String namespaces = System.getProperty(Constants.CONFIG_NAMESPACE_KEY);
        if (StringUtils.isNotBlank(namespaces)) {
            System.setProperty(PropertySourcesConstants.APOLLO_BOOTSTRAP_NAMESPACES, namespaces);
        }

        envMap.put("eureka.client.enabled", String.valueOf(Boolean.TRUE));
        envMap.put(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED, String.valueOf(Boolean.TRUE));
        // 设置环境的值
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new MapPropertySource("mapSource", envMap));

    }
}
