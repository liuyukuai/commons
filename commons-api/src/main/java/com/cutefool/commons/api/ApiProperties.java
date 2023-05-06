/*
 *  
 */
package com.cutefool.commons.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 271007729@qq.com
 * @date 2019/9/26 2:21 PM
 */
@Data
@ConfigurationProperties(prefix = "commons.api")
public class ApiProperties {

    /**
     * 地址
     */
    private String address;

    /**
     * token值
     */
    private String token;
}
