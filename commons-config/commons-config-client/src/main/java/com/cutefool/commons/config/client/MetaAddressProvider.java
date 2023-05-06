package com.cutefool.commons.config.client;

import com.cutefool.commons.config.core.enmus.Env;
import com.cutefool.commons.config.core.spi.MetaServerProvider;
import com.cutefool.commons.config.core.spi.Ordered;
import com.cutefool.commons.config.core.spi.SpiLoader;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class MetaAddressProvider {
    public static final String DEFAULT_META_URL = "";

    private static final Map<Env, String> metaServerAddressCache = Maps.newConcurrentMap();
    private static volatile List<MetaServerProvider> metaServerProviders = null;

    private static final Logger logger = LoggerFactory.getLogger(MetaAddressProvider.class);

    private static final Object LOCK = new Object();

    public static String getMetaAddress(String name, Env env) {
        if (!metaServerAddressCache.containsKey(env)) {
            initMetaServerAddress(name, env);
        }
        return metaServerAddressCache.get(env);
    }

    private static void initMetaServerAddress(String name, Env env) {
        if (metaServerProviders == null) {
            synchronized (LOCK) {
                if (metaServerProviders == null) {
                    metaServerProviders = initMetaProviders(name);
                }
            }
        }

        String metaAddress = null;

        for (MetaServerProvider provider : metaServerProviders) {
            metaAddress = provider.getMetaAddress(env);
            if (StringUtils.isNotBlank(metaAddress)) {
                logger.info("Located meta server address {} for env {} from {}", metaAddress, env, provider.getClass());
                break;
            }
        }

        if (StringUtils.isBlank(metaAddress)) {
            metaAddress = DEFAULT_META_URL;
            logger.warn("Meta server address fallback to {} for env {}, because it is not available in all MetaServerProviders", metaAddress, env);
        }

        metaServerAddressCache.put(env, metaAddress.trim());
    }

    private static List<MetaServerProvider> initMetaProviders(String name) {
        Iterator<MetaServerProvider> metaServerProviderIterator = SpiLoader.loadAll(MetaServerProvider.class);
        List<MetaServerProvider> metaServerProviders = Lists.newArrayList(metaServerProviderIterator);
        return metaServerProviders.stream()
                                  .filter(e -> StringUtils.isBlank(e.name()) || Objects.equals(name, e.name()))
                                  .sorted(Comparator.comparingInt(Ordered::getOrder))
                                  .collect(Collectors.toList());
    }
}
