package com.cutefool.commons.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author 271007729@qq.com
 */
@Configuration
@SuppressWarnings("all")
public class CrossOriginConfig implements WebMvcConfigurer {

    @Resource
    private SecurityProperties securityProperties;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(securityProperties.getAllowedOrigin())
                .allowedMethods(StringUtils.split(securityProperties.getAllowedMethod(), ","))
                .allowedHeaders(securityProperties.getAllowedHeader())
                .allowCredentials(true)
                .maxAge(3600);
    }
}
