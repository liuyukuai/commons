package com.itxiaoer.commons.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : liuyk
 */
@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "commons.security.jwt")
public class JwtProperties {
    /**
     * jwt header名称
     */
    @Value("${commons.security.jwt.header:Authorization}")
    private String header;
    /**
     * jwt secret
     */
    @Value("${commons.security.jwt.secret}")
    private String secret;
    /**
     * jwt token 过期时间
     */
    @Value("${commons.security.jwt.expiration:7200}")
    private long expiration;

    /**
     * jwt 过期token存储前缀
     */
    @Value("${commons.security.jwt.prefix:redis_token_prefix}")
    private String prefix;


    /**
     * jwt 过期token存储方式,local 或者redis
     */
    @Value("${commons.security.jwt.storeType:local}")
    private String storeType;
}
