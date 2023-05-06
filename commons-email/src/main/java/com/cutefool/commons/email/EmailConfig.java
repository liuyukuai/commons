/*
 *
 */
package com.cutefool.commons.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 邮件发送配置
 *
 * @author 271007729@qq.com
 * @date 2019-07-03 10:47
 */
@Configuration
public class EmailConfig {

    @Bean
    public EmailClient emailClient() {
        return new EmailClient();
    }
}
