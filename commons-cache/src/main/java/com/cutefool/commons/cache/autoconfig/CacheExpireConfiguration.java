package com.cutefool.commons.cache.autoconfig;

import com.cutefool.commons.cache.annotion.ExpiredCaching;
import com.cutefool.commons.cache.annotion.LocalExpireCaching;
import com.cutefool.commons.cache.annotion.RemoteExpireCaching;
import com.cutefool.commons.cache.libs.ExpireLibs;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.ReflectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CacheExpireConfiguration implements SmartInitializingSingleton, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Getter
    private final Map<String, ExpireLibs> expireCachingMap = new ConcurrentHashMap<>();


    @SuppressWarnings("NullableProblems")
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(ExpiredCaching.class);
        if (Maps.iterable(beansWithAnnotation)) {
            for (Object cacheValue : beansWithAnnotation.values()) {
                ReflectionUtils.doWithMethods(cacheValue.getClass(), method -> {
                    ReflectionUtils.makeAccessible(method);
                    LocalExpireCaching localExpireCaching = method.getAnnotation(LocalExpireCaching.class);
                    if (Objects.nonNull(localExpireCaching)) {
                        log.info("class = {},method = {} caching = {}", cacheValue.getClass(), method.getName(), localExpireCaching);
                        this.doExpired(localExpireCaching.value(), localExpireCaching.expireSecond());
                    }

                    RemoteExpireCaching remoteExpireCaching = method.getAnnotation(RemoteExpireCaching.class);
                    if (Objects.nonNull(remoteExpireCaching)) {
                        log.info("class = {},method = {} caching = {}", cacheValue.getClass(), method.getName(), remoteExpireCaching);
                        this.doExpired(remoteExpireCaching.value(), remoteExpireCaching.expireSecond());
                    }
                });
            }
        }
    }

    private void doExpired(String[] value, long expireSecond) {
        if (Lists.iterable(value)) {
            for (String s : value) {
                expireCachingMap.put(s, new ExpireLibs(expireSecond));
            }
        }
    }
}
