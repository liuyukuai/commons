/*
 *
 */
package com.cutefool.commons.config.nacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author 271007729@qq.com
 * @date 2022/7/28 6:31 PM
 */
@Slf4j
public class NacosRefreshListener implements ApplicationListener<RefreshEvent> {

    @Resource
    private ContextRefresher contextRefresher;

    @Override
    public void onApplicationEvent(RefreshEvent e) {
        log.info("The current service configuration is ready to be updated.");
        contextRefresher.refresh();
        log.info("The current service configuration is successfully updated.");
    }
}
