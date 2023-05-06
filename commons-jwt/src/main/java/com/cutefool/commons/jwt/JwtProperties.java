package com.cutefool.commons.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 271007729@qq.com
 * @date 2019-07-04 12:55
 */
@Data
@Component
@ConfigurationProperties(prefix = "commons.security.jwt")
public class JwtProperties {
    /**
     * jwt header名称
     */
    @Value("${commons.security.jwt.header:Authorization}")
    private String header;

    /**
     * jwt params名称
     */
    @Value("${commons.security.jwt.params:token}")
    private String token;

    /**
     * 是否支持多终端登录
     */
    @Value("${commons.security.jwt.multi:false}")
    private boolean multi;

    /**
     * jwt secret
     */
    @Value("${commons.security.jwt.secret:tsingyun}")
    private String secret;

    /**
     * jwt token 过期时间
     */
    @Value("${commons.security.jwt.expiration:7200}")
    private long expiration;


    /**
     * jwt token 过期时间(APP)
     */
    @Value("${commons.security.jwt.app.expiration:25992000}")
    private long appExpiration;

    /**
     * jwt 过期token存储前缀
     */
    @Value("${commons.security.jwt.prefix:redis_token_prefix}")
    private String prefix;


    /**
     * jwt 过期token存储前缀
     */
    @Value("${commons.security.jwt.tokenKey:redis_token_token}")
    private String tokenKey;


    /**
     * jwt 过期token存储方式,local 或者redis
     */
    @Value("${commons.security.jwt.storeType:redis}")
    private String storeType;

    /**
     * 可刷新剩余时间
     */
    @Value("${commons.security.jwt.refresh.time:120}")
    private long refresh;
}
