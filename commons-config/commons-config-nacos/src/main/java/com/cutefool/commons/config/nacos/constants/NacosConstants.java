/*
 *
 */
package com.cutefool.commons.config.nacos.constants;

/**
 * @author 271007729@qq.com
 * @date 2022/7/26 12:01 PM
 */
public class NacosConstants {

    /**
     * apollo
     */
    public static final String NACOS_NAME = "nacos";

    /**
     * properties
     */
    public static final String CONFIG_ENV_FILE_NAME = NACOS_NAME + "-env.properties";

    /**
     * discovery enabled
     */
    public static final String NACOS_DISCOVERY_ENABLE_KEY = "spring.cloud.nacos.discovery.enabled";

    /**
     * config enabled
     */
    public static final String NACOS_CONFIG_ENABLE_KEY = "spring.cloud.nacos.config.enabled";

    /**
     * discovery address
     */
    public static final String NACOS_DISCOVERY_SERVER_KEY = "spring.cloud.nacos.discovery.server-addr";

    /**
     * config address
     */
    public static final String NACOS_CONFIG_SERVER_KEY = "spring.cloud.nacos.config.server-addr";

    /**
     * discovery namespace
     */
    public static final String NACOS_DISCOVERY_NAMESPACE_KEY = "spring.cloud.nacos.discovery.namespace";

    /**
     * config namespace
     */
    public static final String NACOS_CONFIG_NAMESPACE_KEY = "spring.cloud.nacos.config.namespace";
}
