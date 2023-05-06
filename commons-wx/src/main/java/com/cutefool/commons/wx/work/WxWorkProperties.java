/*
 *  
 */
package com.cutefool.commons.wx.work;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 271007729@qq.com
 * @date 8/12/21 12:59 PM
 */
@Data
@Component
@ConfigurationProperties(prefix = "commons.wx.work")
public class WxWorkProperties {

    /**
     * 企业微信的CorpID
     */
    @Value("${commons.wx.work.appId:ww575c0a85de782579}")
    private String appId;

    /**
     * 授权方的网页应用ID
     */
    @Value("${commons.wx.work.agentId:1000013}")
    private String agentId;

    /**
     * 授权方的网页应用ID
     */
    private String secret;
}
