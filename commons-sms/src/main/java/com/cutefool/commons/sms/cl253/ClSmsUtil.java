/*
 *
 */
package com.cutefool.commons.sms.cl253;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.Charset;
import java.time.Duration;

/**
 * 短信发送接口
 *
 * @author 271007729@qq.com
 * @date 2020/3/10 4:25 PM
 */
public class ClSmsUtil {


    public static ClSmsResponse send(String path, String postContent) {
        return WebClient.create(path)
                .post()
                .syncBody(postContent)
                .header("Charset", "UTF-8")
                .header("Content-Type", "Application/json")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(Charset.defaultCharset())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ClSmsResponse>() {
                })
                .blockOptional(Duration.ofSeconds(10000))
                .orElse(new ClSmsResponse());
    }


}
