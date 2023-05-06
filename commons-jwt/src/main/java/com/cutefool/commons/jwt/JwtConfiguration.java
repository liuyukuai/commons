package com.cutefool.commons.jwt;

import com.cutefool.commons.jwt.context.TokenContext;
import com.cutefool.commons.jwt.listener.DefaultTokenListener;
import com.cutefool.commons.jwt.listener.TokenListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author 271007729@qq.com
 * @date 2019-07-04 12:56
 */
@Configuration
public class JwtConfiguration {

    @Bean
    public JwtBuilder jwtTokenUtil() {
        return new JwtBuilder();
    }

    @Bean
    public TokenContext tokenContext() {
        return new TokenContext();
    }

    @Bean
    public TokenRefresh tokenRefresh() {
        return new TokenRefresh();
    }

    @Bean
    @ConditionalOnMissingBean(TokenListener.class)
    public TokenListener tokenListener() {
        return new DefaultTokenListener();
    }
}
