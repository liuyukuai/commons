/*
 *
 */
package com.cutefool.commons.wx;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.wx.libs.WxResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

/**
 * @author 271007729@qq.com
 * @date 10/17/21 8:26 PM
 */
@Slf4j
@SuppressWarnings("WeakerAccess")
public class WxConstants {

    public static final Duration TIME_OUT = Duration.ofSeconds(30);

    /**
     * 获取token
     *
     * @param uri URI
     * @return token
     */
    public static <T extends WxResponse> T execute(String uri, Class<T> cls) {
        T response = WebClient.create().get().uri(uri).retrieve().bodyToMono(cls).block(WxConstants.TIME_OUT);
        log.info("execute uri = {} response = {}", uri, response);
        return response;
    }
}
