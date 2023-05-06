package com.cutefool.commons.core.util;

import com.cutefool.commons.core.Nullable;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * list utils
 *
 * @author 271007729@qq.com
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Lists {

    /**
     * create arrayList
     *
     * @param <E> element type
     * @return arrayList
     */
    public static <E> List<E> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * create arrayList
     *
     * @param <E> element type
     * @return arrayList
     */
    public static <E> List<E> newArrayList(E e) {
        List<E> es = new ArrayList<>(1);
        es.add(e);
        return es;
    }

    /**
     * create arrayList
     *
     * @param <E> element type
     * @return arrayList
     */
    public static <E> List<E> newArrayList(Iterator<E> elements) {
        List<E> list = newArrayList();
        if (Objects.nonNull(elements)) {
            while (elements.hasNext()) {
                list.add(elements.next());
            }
        }
        return list;
    }

    /**
     * list is iterable
     *
     * @param list list
     * @param <E>  element type
     * @return true | false
     */
    public static <E> boolean iterable(List<E> list) {
        return list != null && !list.isEmpty();
    }

    /**
     * list is iterable
     *
     * @param list list
     * @param <E>  element type
     * @return true | false
     */
    public static <E> boolean iterable(Collection<E> list) {
        return list != null && !list.isEmpty();
    }

    /**
     * array is iterable
     *
     * @param array array
     * @param <E>   element type
     * @return true | false
     */
    public static <E> boolean iterable(E[] array) {
        return array != null && Array.getLength(array) != 0;
    }

    /**
     * 是否为空
     *
     * @param list list
     * @return 是否为空
     */
    public static <E> boolean isEmpty(List<E> list) {
        return Objects.isNull(list) || list.isEmpty();
    }

    /**
     * 是否为空
     *
     * @param list list
     * @return 是否为空
     */
    public static <E> boolean isEmpty(Collection<E> list) {
        return Objects.isNull(list) || list.isEmpty();
    }

    /**
     * 是否为空
     *
     * @param array array
     * @param <E>   element type
     * @return 是否为空
     */
    public static <E> boolean isEmpty(E[] array) {
        return Objects.isNull(array) || Array.getLength(array) == 0;
    }

    /**
     * null to empty list
     *
     * @param list list
     * @param <E>  element type
     * @return list
     */
    public static <E> List<E> empty(List<E> list) {
        return Optional.ofNullable(list).orElse(newArrayList());
    }

    /**
     * null to empty list
     *
     * @param list list
     * @param <E>  element type
     * @return list
     */
    public static <E> Collection<E> empty(Collection<E> list) {
        return Optional.ofNullable(list).orElse(newArrayList());
    }

    public static <E> List<E> distinct(Collection<E> list, Function<? super E, ?> keyExtractor) {
        return distinctStream(list, keyExtractor).collect(Collectors.toList());
    }

    public static <E> List<E> distinct(List<E> list, Function<? super E, ?> keyExtractor) {
        return distinctStream(list, keyExtractor).collect(Collectors.toList());
    }

    public static <E> Stream<E> distinctStream(List<E> list, Function<? super E, ?> keyExtractor) {
        return Lists.empty(list).stream().filter(distinctByKey(keyExtractor));
    }

    public static <E> Stream<E> distinctStream(Collection<E> list, Function<? super E, ?> keyExtractor) {
        return Lists.empty(list).stream().filter(distinctByKey(keyExtractor));
    }

    public static <E> Predicate<E> distinctByKey(Function<? super E, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>(16);
        return t -> Objects.isNull(seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE));
    }

    /**
     * null to empty list
     *
     * @param list list
     * @param size size
     * @param <E>  element type
     * @return list
     */
    public static <E> List<List<E>> subList(List<E> list, int size) {
        List<List<E>> lists = new ArrayList<>();
        List<E> empty = Lists.empty(list);
        int listSize = empty.size();
        if (listSize <= size || size <= 0) {
            lists.add(list);
            return lists;
        }
        int num = listSize / size + 1;
        for (int i = 0; i < num; i++) {
            int min = size * i;
            int max = size * (i + 1);
            List<E> subList = list.subList(min, max > (listSize - 1) ? listSize : max);
            if (Lists.iterable(subList)) {
                lists.add(subList);
            }
        }
        return lists;
    }

    /**
     * clean null element
     *
     * @param list list
     * @param <E>  element type
     * @return list
     */
    public static <E> List<E> clean(List<E> list) {
        return clean(list, (d) -> d);
    }

    /**
     * clean null element
     *
     * @param list list
     * @param <E>  element type
     * @return list
     */
    public static <E, R> List<R> clean(List<E> list, Function<E, R> function) {
        return empty(list).stream().filter(e -> {
            if (Objects.isNull(e)) {
                return false;
            }
            if (e instanceof Nullable) {
                return !((Nullable) e).isNull();
            }

            if (e instanceof Collection) {
                return iterable((Collection<?>) e);
            }
            return true;
        }).map(function).collect(Collectors.toList());
    }

    /**
     * null to empty list
     *
     * @param objects objects
     * @param index   index
     * @param <E>     element type
     * @return list
     */
    public static <E> E computeIfAbsent(List<E> objects, int index) {
        return computeIfAbsent(objects, index, null);
    }

    /**
     * null to empty list
     *
     * @param objects objects
     * @param index   index
     * @param <E>     element type
     * @return list
     */
    public static <E> E computeIfAbsent(List<E> objects, int index, E defaultValue) {
        Objects.requireNonNull(objects);
        int size = objects.size();
        if (index < size) {
            E e = objects.get(index);
            if (Objects.nonNull(e)) {
                return e;
            }
        }
        int c = index - size;
        for (int i = 0; i < c; i++) {
            objects.add(null);
        }
        objects.add(index, defaultValue);
        return defaultValue;
    }

    /**
     * create arrayList
     *
     * @return arrayList
     */
    public static Collection<?> toCollection(Object value) {
        if (value instanceof Collection) {
            return (Collection<?>) value;
        }
        if (value instanceof Object[]) {
            return Arrays.asList((Object[]) value);
        }

        if (value instanceof String) {
            return Strings.split(Strings.empty(value));
        }
        return Collections.singletonList(value);
    }

    public static boolean isCollection(Object value) {
        return value instanceof Collection;
    }

    public static boolean isArray(Object value) {
        return value instanceof Object[];
    }

    public static <T> boolean equals(List<T> v1, List<T> v2) {
        v1 = Lists.empty(v1).stream().filter(Objects::nonNull).sorted(Comparator.comparing(Object::toString)).collect(Collectors.toList());
        v2 = Lists.empty(v2).stream().filter(Objects::nonNull).sorted(Comparator.comparing(Object::toString)).collect(Collectors.toList());
        return v1.equals(v2);
    }
}
