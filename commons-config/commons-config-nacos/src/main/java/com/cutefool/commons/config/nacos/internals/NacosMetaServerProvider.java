package com.cutefool.commons.config.nacos.internals;

import com.cutefool.commons.config.core.enmus.Env;
import com.cutefool.commons.config.nacos.constants.NacosConstants;
import com.cutefool.commons.config.core.ResourceUtils;
import com.cutefool.commons.config.core.spi.MetaServerProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class NacosMetaServerProvider implements MetaServerProvider {

    public static final int ORDER = MetaServerProvider.LOWEST_PRECEDENCE - 1;

    private static final Map<Env, String> domains = new HashMap<>();

    public NacosMetaServerProvider() {
        initialize();
    }

    private void initialize() {
        Properties prop = new Properties();
        prop = ResourceUtils.readConfigFile(NacosConstants.CONFIG_ENV_FILE_NAME, prop);

        domains.put(Env.DEV, getMetaAddress(prop, "dev_meta", "dev.meta"));
        domains.put(Env.FAT, getMetaAddress(prop, "fat_meta", "fat.meta"));
        domains.put(Env.UAT, getMetaAddress(prop, "uat_meta", "uat.meta"));
        domains.put(Env.PRO, getMetaAddress(prop, "pro_meta", "pro.meta"));
    }

    private String getMetaAddress(Properties prop, String sourceName, String propName) {
        String metaAddress = System.getProperty(sourceName);
        if (StringUtils.isBlank(metaAddress)) {
            metaAddress = System.getenv(sourceName.toUpperCase());
        }
        if (StringUtils.isBlank(metaAddress)) {
            metaAddress = prop.getProperty(propName);
        }
        return metaAddress;
    }

    @Override
    public String getMetaAddress(Env env) {
        String metaServerAddress = domains.get(env);
        return metaServerAddress == null ? null : metaServerAddress.trim();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public String name() {
        return NacosConstants.NACOS_NAME;
    }
}
