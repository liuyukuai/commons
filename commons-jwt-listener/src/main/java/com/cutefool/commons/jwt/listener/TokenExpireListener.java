/*
 *  
 */
package com.cutefool.commons.jwt.listener;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.jwt.JwtBuilder;
import com.cutefool.commons.jwt.JwtProperties;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.Resource;

/**
 * @author 271007729@qq.com
 * @date 12/23/20 2:47 PM
 */
@Slf4j
@SuppressWarnings("WeakerAccess")
public class TokenExpireListener extends KeyExpirationEventMessageListener {

    @Resource
    private JwtBuilder jwtBuilder;

    @Resource
    private JwtProperties jwtProperties;


    public TokenExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String tokenKey = jwtProperties.getTokenKey();

        String token = message.toString();

        try {

            if (token.startsWith(tokenKey)) {

                if (log.isDebugEnabled()) {
                    log.debug("get expire token = {} ,pattern = {} ", token, pattern);
                }

                String replace = token.replace(tokenKey, "");
                jwtBuilder.claims(replace);
            }
        } catch (RuntimeException ignored) {

        }
    }
}