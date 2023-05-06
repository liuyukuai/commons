package com.cutefool.commons.cache.manager;

import com.cutefool.commons.cache.libs.ExpireLibs;
import com.cutefool.commons.cache.autoconfig.CacheExpireConfiguration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

public class RedisCachingManager extends RedisCacheManager {

    private final CacheExpireConfiguration cacheExpireConfiguration;

    public RedisCachingManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, CacheExpireConfiguration cacheExpireConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheExpireConfiguration = cacheExpireConfiguration;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        Map<String, ExpireLibs> expireCachingMap = cacheExpireConfiguration.getExpireCachingMap();
        ExpireLibs localExpireCaching = expireCachingMap.get(name);
        if (Objects.isNull(localExpireCaching) || localExpireCaching.getExpire() <= 0) {
            return super.createRedisCache(name, cacheConfig);
        }
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(localExpireCaching.getExpire()));
        return super.createRedisCache(name, redisCacheConfiguration);
    }
}
