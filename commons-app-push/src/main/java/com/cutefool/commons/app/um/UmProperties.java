/*
 *  
 */
package com.cutefool.commons.app.um;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 友盟推送
 *
 * @author 271007729@qq.com
 * @date 2020/4/23 9:37 AM
 */

@Data
@ConfigurationProperties(prefix = "commons.app.push.um")
public class UmProperties {

    /**
     * 安卓key
     */
    @Value("${commons.app.push.um.androidKey:}")
    private String androidKey;

    /**
     * 安卓秘钥
     */
    @Value("${commons.app.push.um.androidSecret:}")
    private String androidSecret;

    /**
     * iosKey
     */
    private String iosKey;

    /**
     * ios秘钥
     */
    private String iosSecret;

    /**
     * 通道
     */
    @Value("${commons.app.push.um.mainActivity:com.tsingyun.yangnong.activity.SplashActivity}")
    private String mainActivity;

    /**
     * 小时过期时长（秒），默认一个小时
     */
    @Value("${commons.app.push.um.expireTime:3600}")
    private Long expireTime;
}
