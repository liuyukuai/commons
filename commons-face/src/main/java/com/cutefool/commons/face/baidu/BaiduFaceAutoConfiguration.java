package com.cutefool.commons.face.baidu;

import com.cutefool.commons.autoconfig.baidu.BaiduFaceConfiguration;
import com.cutefool.commons.face.FaceInvoker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(BaiduFaceConfiguration.class)
public class BaiduFaceAutoConfiguration {

    @Bean(FaceInvoker.FACE_PREFIX + BaiduConstants.BAIDU_FACE_NAME)
    public BaiduFaceInvoker baiduFaceInvoker(BaiduFaceConfiguration baiduFaceConfiguration) {
        return new BaiduFaceInvoker(baiduFaceConfiguration);
    }
}
