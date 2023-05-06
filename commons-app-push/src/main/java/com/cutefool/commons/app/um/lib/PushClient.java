package com.cutefool.commons.app.um.lib;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.http.Http;
import org.springframework.util.DigestUtils;

import java.util.Map;

@Slf4j
public class PushClient {

    // The user agent
    protected final String USER_AGENT = "Mozilla/5.0";

    // The host
    protected static final String host = "http://msg.umeng.com";

    // The post path
    protected static final String postPath = "/api/send";

    public boolean send(UmengNotification msg) throws Exception {
        String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
        msg.setPredefinedKeyValue("timestamp", timestamp);
        String url = host + postPath;
        String postBody = msg.getPostBody();
        String sign = DigestUtils.md5DigestAsHex(("POST" + url + postBody + msg.getAppMasterSecret()).getBytes("utf8"));
        url = url + "?sign=" + sign;
        Map<String, String> hashMap = Maps.hashMap();
        hashMap.put("User-Agent", USER_AGENT);
        UmResp umResp = Http.newInstance().post(url, postBody, hashMap, UmResp.class);
        log.info("UmResp = {}", umResp);
        return true;
    }
}
