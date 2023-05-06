/*
 *
 */
package com.cutefool.commons.test;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * spi上下文
 *
 * @author 271007729@qq.com
 * @date 2019-07-16 09:22
 */
public class TestableContext implements ApplicationContextAware {

    private static ApplicationContext APPLICATIONCONTEXT;

    private static void set(ApplicationContext applicationContext) {
        TestableContext.APPLICATIONCONTEXT = applicationContext;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        set(applicationContext);
    }

    /**
     * 根据类型获取对象
     *
     * @return 扩展点对象集合
     */
    public static Collection<Testable> get() {
        Map<String, Testable> beansOfType = APPLICATIONCONTEXT.getBeansOfType(Testable.class);
        return beansOfType.values();
    }

    /**
     * 根据类型获取对象
     *
     * @return 扩展点对象集合
     */
    public static Collection<Testable> get(String name) {
        Map<String, Testable> beansOfType = APPLICATIONCONTEXT.getBeansOfType(Testable.class);
        return Collections.singleton(beansOfType.get(name));
    }

}
