package com.cutefool.commons.redis.sentinel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis哨兵模式
 *
 * @author 271007729@qq.com
 * @date 2019-07-04 12:35
 */

@Getter
@Setter
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
@ConditionalOnProperty("spring.redis.sentinel.master")
public class RedisSentinelConfig extends CachingConfigurerSupport {


    @Primary
    @Bean(name = "redisTemplate")
    @SuppressWarnings("ALL")
    public <T> RedisTemplate<String, T> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        GenericToStringSerializer<Object> genericToStringSerializer = new GenericToStringSerializer<>(Object.class);
        // 设置key和value序列化格式
        // value is not an integer or out of range
        redisTemplate.setValueSerializer(genericToStringSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
