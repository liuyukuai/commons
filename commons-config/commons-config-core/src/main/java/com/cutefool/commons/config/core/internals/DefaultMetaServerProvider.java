package com.cutefool.commons.config.core.internals;

import com.cutefool.commons.config.core.enmus.Env;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.config.core.Constants;
import com.cutefool.commons.config.core.spi.MetaServerProvider;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DefaultMetaServerProvider implements MetaServerProvider {

    public static final int ORDER = 0;

    private final String address;

    public DefaultMetaServerProvider() {
        address = initMetaAddress();
    }

    private String initMetaAddress() {
        // 1. Get from System Property
        String metaAddress = System.getProperty(Constants.CONFIG_META_KEY);
        if (StringUtils.isBlank(metaAddress)) {
            // 2. Get from System Property upper case
            metaAddress = System.getenv(Constants.CONFIG_META_KEY.toUpperCase());
        }

        if (StringUtils.isBlank(metaAddress)) {
            log.warn("Could not find meta server address, because it is not available in neither (1) JVM system property '" + Constants.CONFIG_META_KEY + "', (2) OS env variable '" + Constants.CONFIG_META_KEY.toUpperCase() + "' ");
        } else {
            metaAddress = metaAddress.trim();
            log.info("Located meta services from apollo.meta configuration: {}!", metaAddress);
        }

        return metaAddress;
    }

    @Override
    public String getMetaAddress(Env env) {
        return address;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public String name() {
        return "";
    }
}
