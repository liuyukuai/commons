/*
 *
 */
package com.cutefool.commons.orm;

import com.cutefool.commons.core.Exclude;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.util.Lists;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 11/2/21 5:08 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Transforms {

    private Transforms() {
    }

    public static <T> Map<String, Transformation> fields(T queryable) {
        return fields(queryable, (name) -> true);
    }


    public static <T> Map<String, Transformation> fields(T queryable, Predicate<String> predicate) {

        Exclude annotation = queryable.getClass().getAnnotation(Exclude.class);
        // 查询需要排除的属性
        Set<String> excludeFields = Optional.ofNullable(annotation).map(e -> Arrays.asList(e.value())).map(HashSet::new).orElse(new HashSet<>());

        // 该类型
        Reflect reflect = Reflect.on(queryable);
        // 查询需要过滤的属性
        // 获取所有属性
        Map<String, Reflect> fields = reflect.fields();
        // 过滤掉排除的属性
        return fields.entrySet()
                .stream()
                .filter(e -> !exclude(queryable.getClass(), e.getKey()))
                .filter(e -> predicate.test(e.getKey()))
                .filter(e -> excludeFields.isEmpty() || !excludeFields.contains(e.getKey()))
                // 转换
                .collect(Collectors.toMap(Map.Entry::getKey, e -> Transforms.transform(queryable, e.getKey(), e.getValue())));
    }


    private static <T> Transformation transform(T queryable, String name, Reflect value) {
        Class<?> clazz = queryable.getClass();
        Transform transform = getDeclaredField(clazz, name).getAnnotation(Transform.class);
        // 如果属性没有配置注解，采用默认
        if (transform == null) {
            return new Transformation(new String[]{name}, value.get(), operator(null, value.get()), Operator.OR, true);
        }
        // 是否有配置属性，没有取字段名称
        String[] names = Lists.iterable(transform.value()) ? transform.value() : new String[]{name};
        return new Transformation(names, value.get(), operator(transform.operator(), value.get()), transform.relation(), transform.ignoreEmpty());
    }

    private static <T> boolean exclude(Class<T> clazz, String name) {
        Exclude annotation = getDeclaredField(clazz, name).getAnnotation(Exclude.class);
        return annotation != null;
    }

    private static <T> Field getDeclaredField(Class<T> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            // 查询父类是否有
            return getDeclaredField(clazz.getSuperclass(), name);
        }
    }

    private static Operator operator(Operator operator, Object value) {
        if (Objects.nonNull(operator) || Objects.isNull(value)) {
            return operator;
        }

        // 如果值是集合或者是数组
        if (value instanceof Collection || value instanceof Object[]) {
            return Operator.IN;
        }
        return Operator.EQ;
    }
}
