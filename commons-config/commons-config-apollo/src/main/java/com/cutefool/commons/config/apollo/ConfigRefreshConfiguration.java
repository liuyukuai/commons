package com.cutefool.commons.config.apollo;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.config.apollo.constant.ApolloConstants;
import com.cutefool.commons.config.core.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 配置自动刷新
 *
 * @author 271007729@qq.com
 * @date 2019-07-07 09:58
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = Constants.COMMONS_CONFIG_TYPE, havingValue = ApolloConstants.APOLLO_NAME, matchIfMissing = true)
public class ConfigRefreshConfiguration {

    @Resource
    private ContextRefresher contextRefresher;

    @SuppressWarnings("unused")
    @ApolloConfigChangeListener
    public void onChange(ConfigChangeEvent changeEvent) {
        boolean debug = log.isDebugEnabled();
        if (debug) {
            Set<String> strings = changeEvent.changedKeys();
            strings.forEach(e -> {
                ConfigChange change = changeEvent.getChange(e);
                log.debug("change key = [{}] ,namespace = [{}] oldValue =[{}], newValue = [{}]", e, change.getNamespace(), change.getOldValue(), change.getNewValue());
            });
        }
        log.info("The current service configuration is ready to be updated.");
        contextRefresher.refresh();
        log.info("The current service configuration is successfully updated.");
    }
}
