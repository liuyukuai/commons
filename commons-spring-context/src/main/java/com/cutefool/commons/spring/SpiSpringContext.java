/*
 *  
 */
package com.cutefool.commons.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * spi上下文
 *
 * @author 271007729@qq.com
 * @date 2019-07-16 09:22
 */
@Configuration
@SuppressWarnings("unused")
public class SpiSpringContext implements ApplicationContextAware {


    private static ApplicationContext APPLICATIONCONTEXT;

    private static void set(ApplicationContext applicationContext) {
        SpiSpringContext.APPLICATIONCONTEXT = applicationContext;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        set(applicationContext);
    }

    /**
     * 根据类型获取对象
     *
     * @param clz      clz类型
     * @param <T>具体的对象
     * @return 扩展点对象集合
     */
    public static <T> Collection<T> getSpi(Class<T> clz) {
        Map<String, T> beansOfType = APPLICATIONCONTEXT.getBeansOfType(clz);
        return beansOfType.values();
    }

    /**
     * 根据类型获取对象
     *
     * @param clz      clz类型
     * @param <T>具体的对象
     * @return 扩展点对象集合
     */
    public static <T> Collection<T> getSpiByPrefix(String prefix, Class<T> clz) {
        Map<String, T> beansOfType = APPLICATIONCONTEXT.getBeansOfType(clz);
        return beansOfType.entrySet().stream().filter(e -> e.getKey().startsWith(prefix)).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    /**
     * 根据类型获取对象
     *
     * @param clz      clz类型
     * @param <T>具体的对象
     * @return 扩展点对象集合
     */
    public static <T> T getOneSpi(Class<T> clz) {
        return APPLICATIONCONTEXT.getBean(clz);
    }

    /**
     * 根据类型获取对象
     *
     * @param clz      clz类型
     * @param <T>具体的对象
     * @return 扩展点对象集合
     */
    public static <T> T getSpi(String name, Class<T> clz) {
        Map<String, T> beansOfType = APPLICATIONCONTEXT.getBeansOfType(clz);
        return beansOfType.get(name);
    }
}
