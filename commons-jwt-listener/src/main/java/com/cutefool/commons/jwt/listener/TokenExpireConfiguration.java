package com.cutefool.commons.jwt.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 配置类
 *
 * @author 271007729@qq.com
 * @date 2019-07-04 12:56
 */
@Configuration
public class TokenExpireConfiguration {

    @Bean
   public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Bean
    public TokenExpireListener tokenExpireListener(RedisMessageListenerContainer redisMessageListenerContainer) {
        return new TokenExpireListener(redisMessageListenerContainer);
    }
}
