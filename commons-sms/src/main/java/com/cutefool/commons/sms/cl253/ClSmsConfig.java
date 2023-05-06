/*
 *  
 */
package com.cutefool.commons.sms.cl253;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 创蓝253 短信 配置
 *
 * @author 271007729@qq.com
 * @date 2020/3/10 3:43 PM
 */
@Data
@Component("clSmsConfig")
@ConfigurationProperties(prefix = "commons.sms.cl")
public class ClSmsConfig {

    /**
     * 地址
     */
    @Value("${commons.sms.cl.address:}")
    private String address;

    /**
     * 用户名
     */
    @Value("${commons.sms.cl.account:}")
    private String account;

    /**
     * 密码
     */
    @Value("${commons.sms.cl.password:}")
    private String password;

    /**
     * 签名
     */
    @Value("${commons.sms.sign:}")
    private String sign;
}
