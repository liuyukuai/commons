package com.cutefool.commons.face.autoconfig;

import com.cutefool.commons.autoconfig.face.FaceConfiguration;
import com.cutefool.commons.face.baidu.BaiduFaceAutoConfiguration;
import com.cutefool.commons.face.factory.FaceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({BaiduFaceAutoConfiguration.class})
@EnableConfigurationProperties(FaceConfiguration.class )
@Configuration(proxyBeanMethods = false)
public class FaceAutoConfiguration {

    @Bean
    public FaceFactory faceContext() {
        return new FaceFactory();
    }
}
