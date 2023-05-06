package com.cutefool.commons.cache.manager;

import com.cutefool.commons.cache.libs.ExpireLibs;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.cutefool.commons.cache.autoconfig.CacheExpireConfiguration;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LocalCachingManager extends CaffeineCacheManager {

    private final CacheExpireConfiguration cacheExpireConfiguration;


    public LocalCachingManager(CacheExpireConfiguration cacheExpireConfiguration) {
        this.cacheExpireConfiguration = cacheExpireConfiguration;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected Cache<Object, Object> createNativeCaffeineCache(String name) {
        Map<String, ExpireLibs> expireCachingMap = cacheExpireConfiguration.getExpireCachingMap();
        ExpireLibs localExpireCaching = expireCachingMap.get(name);
        if (Objects.isNull(localExpireCaching) || localExpireCaching.getExpire() <= 0) {
            return super.createNativeCaffeineCache(name);
        }
        // 设置单个key的过期时间
        return Caffeine.newBuilder().expireAfterWrite(localExpireCaching.getExpire(), TimeUnit.SECONDS).build();
    }
}
