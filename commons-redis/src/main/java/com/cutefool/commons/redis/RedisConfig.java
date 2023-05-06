package com.cutefool.commons.redis;

import com.cutefool.commons.redis.sentinel.RedisSentinelConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * redis主从模式配置
 *
 * @author 271007729@qq.com
 * @date 2019-07-04 12:34
 */

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
@AutoConfigureAfter(RedisSentinelConfig.class)
@ConditionalOnMissingBean({RedisSentinelConfig.class})
public class RedisConfig {

    @Resource
    private RedisProperties redisProperties;

    private LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "redisTemplate")
    @SuppressWarnings("ALL")
    @ConditionalOnMissingBean(RedisTemplate.class)
    public <T> RedisTemplate<String, T> redisTemplate() {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        LettuceConnectionFactory lettuceConnectionFactory = lettuceConnectionFactory();
        //必须初始化实例
        lettuceConnectionFactory.afterPropertiesSet();
        template.setConnectionFactory(lettuceConnectionFactory);
        GenericToStringSerializer<Object> genericToStringSerializer = new GenericToStringSerializer<>(Object.class);
        // 设置key和value序列化格式
        // value is not an integer or out of range
        template.setValueSerializer(genericToStringSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }


}
