/*
 *  
 */
package com.cutefool.commons.app.um;

import com.cutefool.commons.app.AppNotice;
import com.cutefool.commons.app.um.lib.AndroidNotification;
import com.cutefool.commons.app.PushService;
import com.cutefool.commons.app.um.lib.PushClient;
import com.cutefool.commons.app.um.lib.android.AndroidUnicast;
import com.cutefool.commons.core.util.Times;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 友盟推送接口
 *
 * @author 271007729@qq.com
 * @date 2020/4/23 11:38 AM
 */

public class UmPushServiceImpl implements PushService {

    private PushClient client = new PushClient();

    @Resource
    private UmProperties umProperties;

    @Override
    public void push(AppNotice notice) {
        notice.clean();
        AndroidUnicast unicast;
        try {
            unicast = new AndroidUnicast(umProperties.getAndroidKey(), umProperties.getAndroidSecret());
            unicast.setDeviceToken(notice.getDevice());
            unicast.setTicker(notice.getTicker());
            unicast.setTitle(notice.getTitle());
            unicast.setText(notice.getText());
            unicast.setBadge(notice.getBadge());
            unicast.goCustomAfterOpen("tymobile://main");
            unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            unicast.setProductionMode();
            unicast.setChannelProperties(this.umProperties.getMainActivity());

            // 策略（消息一个小时后没有推送，失效不发送）
            unicast.setPredefinedKeyValue("expire_time", Times.format(LocalDateTime.now()
                                                                                   .plusSeconds(umProperties.getExpireTime())));

            Map<String, String> params = notice.getParams();

            if (Objects.nonNull(params) && !params.isEmpty()) {
                params.forEach((k, v) -> {
                    try {
                        unicast.setExtraField(k, v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            client.send(unicast);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
