package com.itxiaoer.commons.orm;

import com.itxiaoer.commons.core.Exclude;
import com.itxiaoer.commons.core.Operator;
import com.itxiaoer.commons.core.Transform;
import com.itxiaoer.commons.core.util.Lists;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 查询对象处理类
 *
 * @author : liuyk
 */
public final class TransformHandler {

    private TransformHandler() {
    }

    public static <T> Map<String, Transformation> fields(T queryable) {
        // 获取所有属性
        Map<String, Reflect> fields = Reflect.on(queryable).fields();
        // 过滤掉排除的属性
        return fields.entrySet().stream().filter(e -> !exclude(queryable.getClass(), e.getKey()))
                // 转换
                .collect(Collectors.toMap(Map.Entry::getKey, e -> TransformHandler.transform(queryable, e.getKey(), e.getValue())));
    }


    private static <T> Transformation transform(T queryable, String name, Reflect value) {
        Class<?> clazz = queryable.getClass();
        Transform transform = getDeclaredField(clazz, name).getAnnotation(Transform.class);
        // 如果属性没有配置注解，采用默认
        if (transform == null) {
            return new Transformation(new String[]{name}, value.get(), Operator.EQ, Operator.OR, true);
        }
        // 是否有配置属性，没有取字段名称
        String[] names = Lists.iterable(transform.value()) ? transform.value() : new String[]{name};
        return new Transformation(names, value.get(), transform.operator(), transform.relation(), transform.ignoreEmpty());
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
}
