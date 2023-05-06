/*
 *
 */
package com.cutefool.commons.wx;

import com.cutefool.commons.wx.work.WxWorkProperties;
import com.cutefool.commons.wx.work.WxWorks;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 8/12/21 12:54 PM
 */
@Configuration
@EnableConfigurationProperties(WxWorkProperties.class)
public class WxConfig {


    @Bean
    public WxWorks wxWorks() {
        return new WxWorks();
    }

}
