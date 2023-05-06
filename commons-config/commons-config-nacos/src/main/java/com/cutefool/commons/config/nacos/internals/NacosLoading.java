/*
 *  
 */
package com.cutefool.commons.config.nacos.internals;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosPropertySourceRepository;
import com.alibaba.cloud.nacos.client.NacosPropertySource;
import com.cutefool.commons.config.core.spi.ConfigLoading;
import com.cutefool.commons.config.nacos.constants.NacosConstants;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.spring.SpiSpringContext;

import java.util.List;
import java.util.Objects;

/**
 * nacos
 *
 * @author 271007729@qq.com
 * @date 2022/7/26 1:34 PM
 */
public class NacosLoading implements ConfigLoading {

    @Override
    public String doGet(String key) {
        return this.doGet(key, "");
    }

    @Override
    public String doGet(String key, String defaultValue) {
        NacosConfigManager configManager = SpiSpringContext.getOneSpi(NacosConfigManager.class);
        Objects.requireNonNull(configManager);
        List<NacosPropertySource> propertySources = NacosPropertySourceRepository.getAll();
        return Lists.empty(propertySources)
                    .stream()
                    .map(e -> e.getProperty(key))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(String::valueOf)
                    .orElse(defaultValue);
    }

    @Override
    public String name() {
        return NacosConstants.NACOS_NAME;
    }
}
