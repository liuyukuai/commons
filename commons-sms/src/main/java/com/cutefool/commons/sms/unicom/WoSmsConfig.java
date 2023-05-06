/*
 *
 */
package com.cutefool.commons.sms.unicom;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 联通短信 配置
 *
 * @author 271007729@qq.com
 * @date 2020-10-30 13:24
 */
@Data
@Component
@ConfigurationProperties(prefix = "commons.sms.wo")
class WoSmsConfig {

    /**
     * 接口地址：http://rcsapi.wo.cn:8000/umcinterface/sendtempletmsg
     */
    @Value("${commons.sms.wo.address:}")
    private String address;

    /**
     * 帐号
     */
    @Value("${commons.sms.wo.account:}")
    private String account;

    /**
     * 私钥
     */
    @Value("${commons.sms.wo.password:}")
    private String password;


    /**
     * 渠道自定义接入号的扩展码，可为空；为时传空字符串""
     */
    @Value("${commons.sms.wo.extend.code:}")
    private String extendCode;


}
