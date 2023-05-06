/*
 *
 */
package com.cutefool.commons.config.core.spi;

import com.cutefool.commons.core.util.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * spi loader
 *
 * @author 271007729@qq.com
 * @date 2022/7/26 12:47 AM
 */
public final class SpiLoader {

    public static <S extends Named> S load(String name, Class<S> clazz) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(clazz);
        Iterator<S> sIterator = loadAll(clazz);
        return Lists.newArrayList(sIterator)
                    .stream()
                    .filter(e -> Objects.equals(name, e.name()))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("name = " + name + " class = " + clazz + " is not exists. "));

    }

    public static <S extends Named> List<S> loadExclude(String name, Class<S> clazz) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(clazz);
        Iterator<S> sIterator = loadAll(clazz);
        return Lists.newArrayList(sIterator)
                    .stream()
                    .filter(e -> !Objects.equals(name, e.name()))
                    .collect(Collectors.toList());

    }

    public static <S> Iterator<S> loadAll(Class<S> clazz) {
        Objects.requireNonNull(clazz);
        ServiceLoader<S> loader = ServiceLoader.load(clazz);
        return loader.iterator();
    }
}
