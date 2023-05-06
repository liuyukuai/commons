package com.cutefool.commons.core.util;

import com.cutefool.commons.core.function.ThirdFunction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reflects {


    private static final Map<String, Map<String, Field>> FIELDS_MAPS = new ConcurrentHashMap<>();

    public static List<String> fields(Class<?> cls) {
        Field[] declaredFields = cls.getDeclaredFields();
        return Stream.of(declaredFields).map(Field::getName).collect(Collectors.toList());
    }
    public static <T extends Annotation, V> List<V> getAnnotation(Class<?> cls, Class<T> annotationClass, ThirdFunction<V, Map<String, Field>, Field, T> biFunction) {
        if (Objects.isNull(cls)) {
            return Lists.newArrayList();
        }
        Map<String, Field> fieldMap = findDeclaredFields(cls);

        return Lists.empty(fieldMap.values())
                .stream()
                .map(e -> {
                    T annotation = e.getAnnotation(annotationClass);
                    if (Objects.nonNull(annotation)) {
                        return biFunction.apply(fieldMap, e, annotation);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static Map<String, Field> findDeclaredFields(Class<?> cls) {
        Map<String, Field> fieldMap = FIELDS_MAPS.get(cls.getName());
        if (Maps.isEmpty(fieldMap)) {
            List<Field> fields = Lists.newArrayList();
            findDeclaredFields(fields, cls);
            fieldMap = Lists.empty(fields).stream().collect(Collectors.toMap(Field::getName, e -> e, (x1, x2) -> x1));
            FIELDS_MAPS.put(cls.getName(), fieldMap);
            return fieldMap;
        }
        return fieldMap;
    }


    private static void findDeclaredFields(List<Field> fields, Class<?> cls) {
        Field[] declaredFields = cls.getDeclaredFields();
        fields.addAll(Arrays.asList(declaredFields));
        // 查询父类属性
        Class<?> superclass = cls.getSuperclass();
        while (!Objects.equals(superclass.getSimpleName(), Object.class.getSimpleName())) {
            fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
            superclass = superclass.getSuperclass();
        }
    }


    private static <T extends Annotation, V> List<V> getAnnotation(List<V> values, Map<String, Field> fieldMap, Class<?> cls, Class<T> annotationClass, ThirdFunction<V, Map<String, Field>, Field, T> biFunction) {
        if (Objects.isNull(cls)) {
            return Lists.newArrayList();
        }
        Field[] declaredFields = cls.getDeclaredFields();
        return Stream.of(declaredFields)
                .map(e -> {
                    T annotation = e.getAnnotation(annotationClass);
                    if (Objects.nonNull(annotation)) {
                        return biFunction.apply(fieldMap, e, annotation);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
