
/*
 *  
 */
package com.cutefool.commons.request;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author 271007729@qq.com
 */
@Data
@Component
@ConfigurationProperties(prefix = "commons.request")
public class RequestProperties {
    /**
     * 请求后台允许传递的头信息，逗号隔开
     */
    @Value("${commons.request.allowHeaders:x-token,x-user,x-upload-token}")
    private String allowHeader;

}
