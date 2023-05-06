/*
 *
 */
package com.cutefool.commons.core.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * map工具类
 *
 * @author 271007729@qq.com
 * @date 8/3/21 2:52 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Maps {
    private Maps() {
    }

    /**
     * create hashMap
     *
     * @param <K> key
     * @param <V> value
     * @return arrayList
     */
    public static <K, V> Map<K, V> hashMap() {
        return new HashMap<>(16);
    }

    /**
     * create hashMap
     *
     * @param k   k
     * @param v   v
     * @param <K> key
     * @param <V> value
     * @return arrayList
     */
    public static <K, V> Map<K, V> hashMap(K k, V v) {
        Map<K, V> map = new HashMap<>(1);
        map.put(k, v);
        return map;
    }

    /**
     * create linkedHashMap
     *
     * @param <K> key
     * @param <V> value
     * @return arrayList
     */
    public static <K, V> Map<K, V> linkedHashMap() {
        return new LinkedHashMap<>(16);
    }

    /**
     * create linkedHashMap
     *
     * @param <K> key
     * @param <V> value
     * @return arrayList
     */
    public static <K, V> Map<K, V> linkedHashMap(K k, V v) {
        Map<K, V> map = new LinkedHashMap<>(1);
        map.put(k, v);
        return map;
    }

    /**
     * create linkedHashMap
     *
     * @param <K> key
     * @param <V> value
     * @return arrayList
     */
    public static <K, V> Map<K, V> newConcurrentMap() {
        return new ConcurrentHashMap<>(16);
    }

    /**
     * create linkedHashMap
     *
     * @param <K> key
     * @param <V> value
     * @return arrayList
     */
    public static <K, V> Map<K, V> newConcurrentMap(K k, V v) {
        Map<K, V> map = new ConcurrentHashMap<>(1);
        map.put(k, v);
        return map;
    }

    /**
     * map is iterable
     *
     * @param map map
     * @param <K> key
     * @param <V> value
     * @return Map
     */
    public static <K, V> boolean iterable(Map<K, V> map) {
        return Objects.nonNull(map) && !map.isEmpty();
    }

    /**
     * null to empty map
     *
     * @param map list
     * @param <K> key
     * @param <V> value
     * @return list
     */
    public static <K, V> Map<K, V> empty(Map<K, V> map) {
        return Objects.isNull(map) ? hashMap() : map;
    }

    /**
     * null to empty map
     *
     * @param map list
     * @param <K> key
     * @param <V> value
     * @return list
     */
    public static <K, V> Map<K, V> empty(HashMap<K, V> map) {
        return Objects.isNull(map) ? hashMap() : map;
    }

    /**
     * null to empty map
     *
     * @param map list
     * @param <K> key
     * @param <V> value
     * @return list
     */
    public static <K, V> Map<K, V> empty(LinkedHashMap<K, V> map) {
        return Objects.isNull(map) ? linkedHashMap() : map;
    }

    /**
     * 是否为空
     *
     * @param map map
     * @return 是否为空
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return Objects.isNull(map) || map.isEmpty();
    }

    /**
     * null to empty map
     *
     * @param map list
     * @param <K> key
     * @param <V> value
     * @return list
     */
    public static <K, V> Map<V, K> reverse(Map<K, V> map) {
        return empty(map).entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    /**
     * null to empty map
     *
     * @param map list
     * @param <K> key
     * @param <V> value
     * @return list
     */
    public static <K, V> Map<K, V> clean(Map<K, V> map) {
        return Maps.empty(map)
                   .entrySet()
                   .stream()
                   .filter(e -> Objects.nonNull(e.getKey()) && Objects.nonNull(e.getValue()))
                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
