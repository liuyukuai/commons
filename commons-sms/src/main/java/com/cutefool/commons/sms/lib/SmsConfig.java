/*
 *  
 */
package com.cutefool.commons.sms.lib;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 短信发送配置
 *
 * @author 271007729@qq.com
 * @date 2020/3/10 3:43 PM
 */
@Data
@Component
public class SmsConfig {

    /**
     * 短信 供应商
     *
     * @see SmsConstants
     */
    @Value("${commons.sms.type:cl253}")
    private String type;

    /**
     * 管理员有效
     *
     * @see SmsConstants
     */
    @Value("${commons.sms.email:13241861085}")
    private String email;


    /**
     * 短信发送是否启用
     *
     * @see SmsConstants
     */
    @Value("${commons.sms.enabled:true}")
    private boolean enabled;


}
