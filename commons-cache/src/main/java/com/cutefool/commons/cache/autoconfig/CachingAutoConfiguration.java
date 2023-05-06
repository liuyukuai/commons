package com.cutefool.commons.cache.autoconfig;

import com.cutefool.commons.cache.CachingConstants;
import com.cutefool.commons.cache.manager.LocalCachingManager;
import com.cutefool.commons.cache.manager.RedisCachingManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@EnableCaching
@Configuration
public class CachingAutoConfiguration {

    @Bean
    public CacheExpireConfiguration cacheExpireConfiguration() {
        return new CacheExpireConfiguration();
    }

    @Primary
    @DependsOn("cacheExpireConfiguration")
    @Bean(CachingConstants.LOCAL_CACHE_MANAGER)
    public LocalCachingManager localCacheManager(CacheExpireConfiguration cacheExpireConfiguration) {
        return new LocalCachingManager(cacheExpireConfiguration);
    }

    @DependsOn("cacheExpireConfiguration")
    @Bean(CachingConstants.REDIS_CACHE_MANAGER)
    public RedisCachingManager redisCachingManager(RedisConnectionFactory redisConnectionFactory, CacheExpireConfiguration cacheExpireConfiguration) {
        return new RedisCachingManager(RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory), RedisCacheConfiguration.defaultCacheConfig(), cacheExpireConfiguration);
    }
}
