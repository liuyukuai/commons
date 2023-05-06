/*
 *
 */
package com.cutefool.commons.config.nacos;

import com.cutefool.commons.config.core.Constants;
import com.cutefool.commons.config.nacos.constants.NacosConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 2022/7/28 6:31 PM
 */
@Configuration

public class NacosRefreshConfiguration {

    @Bean
    @ConditionalOnProperty(value = Constants.COMMONS_CONFIG_TYPE, havingValue = NacosConstants.NACOS_NAME)
    public NacosRefreshListener nacosRefreshListener() {
        return new NacosRefreshListener();
    }
}
