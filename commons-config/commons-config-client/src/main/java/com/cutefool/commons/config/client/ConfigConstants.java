/*
 *
 */
package com.cutefool.commons.config.client;

import com.ctrip.framework.apollo.spring.config.PropertySourcesConstants;

/**
 * 配置常量
 *
 * @author 271007729@qq.com
 * @date 2022/7/25 11:25 PM
 */
@SuppressWarnings("all")
public final class ConfigConstants {


    /**
     * 配置中心类型默认值（apollo）
     */
    public static final String COMMONS_CONFIG_DEFAULT_VALUE = "apollo";

    /**
     * 配置中心类型默认值（nacos）
     */
    public static final String COMMONS_CONFIG_NACOS_VALUE = "nacos";

    /**
     * apollo启动配置项
     */
    public static final String KEY_APOLLO_BOOTSTRAP_ENABLED = PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED;

    /**
     * nacos启动配置项
     */
    public static final String KEY_NACOS_BOOTSTRAP_ENABLED = "spring.cloud.nacos.discovery.enabled";

    /**
     * eureka启动配置项
     */
    public static final String KEY_EUREKA_CLIENT_ENABLED = "eureka.client.enabled";
}
