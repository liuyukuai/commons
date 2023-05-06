package com.cutefool.commons.bulk;

import com.cutefool.commons.core.ExcelIgnore;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.Naming;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.core.util.Reflects;
import com.cutefool.commons.spring.SpiSpringContext;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SuppressWarnings("all")
public class BulkParser {

    private static final Map<String, List<BulkLibs>> BULKED_MAPS = new ConcurrentHashMap<>();

    private static final Map<String, List<String>> IGNORE_MAPS = new ConcurrentHashMap<>();


    public static <T> Object findFirstObject(Collection<T> objs) {
        return objs.stream().findFirst().orElse(null);
    }

    public static <T> List<BulkLibs> listBulkedFields(Object firstObject, BulkContext context) {
        List<BulkLibs> bulkLibs = listBulkedFields(firstObject);
        return filter(context, bulkLibs);
    }

    public static <T> List<BulkLibs> filter(BulkContext context, List<BulkLibs> bulkLibs) {
        List<String> excludeBulks = Lists.empty(context.getExcludeBulks());
        List<String> includeBulks = Lists.empty(context.getIncludeBulks());

        return Lists.empty(bulkLibs)
                .stream()
                .filter(e -> Lists.isEmpty(includeBulks) || includeBulks.contains(e.getSn()))
                .filter(e -> Lists.isEmpty(excludeBulks) || !excludeBulks.contains(e.getSn()))
                .collect(Collectors.toList());
    }

    public static <T> Map<String, Object> filter(Map<String, Object> data, BulkContext context, List<String> ignores) {
        List<String> excludes = Lists.empty(context.getExcludes());
        List<String> includes = Lists.empty(context.getIncludes());


        return Maps.empty(data)
                .entrySet()
                .stream()
                .filter(e -> Lists.isEmpty(includes) || includes.contains(e.getKey()))
                .filter(e -> Lists.isEmpty(excludes) || !excludes.contains(e.getKey()))
                .filter(e -> !context.isExport() || Lists.isEmpty(ignores) || !ignores.contains(e.getKey()))
                .collect(HashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), HashMap::putAll);
    }


    private static <T> List<BulkLibs> listBulkedFields(T object) {
        List<BulkLibs> fixedMap = BULKED_MAPS.get(object.getClass().getName());
        if (Lists.isEmpty(fixedMap)) {
            fixedMap = bulkedFields(object.getClass());
            BULKED_MAPS.put(object.getClass().getName(), fixedMap);
        }
        return fixedMap;
    }

    private static <T> List<String> listIgnoreFields(T object) {
        List<String> fixedMap = IGNORE_MAPS.get(object.getClass().getName());
        if (Lists.isEmpty(fixedMap)) {
            fixedMap = ignoreFields(object.getClass());
            IGNORE_MAPS.put(object.getClass().getName(), fixedMap);
        }
        return fixedMap;
    }


    public static List<BulkLibs> bulkedFields(Class<?> cls) {
        if (Objects.isNull(cls) || Objects.equals(cls, Object.class)) {
            return Lists.newArrayList();
        }
        return bulkedLibs(cls);
    }

    public static List<String> ignoreFields(Class<?> cls) {
        if (Objects.isNull(cls) || Objects.equals(cls, Object.class)) {
            return Lists.newArrayList();
        }
        return Reflects.getAnnotation(cls, ExcelIgnore.class, ((fieldMap, field, excelIgnore) -> {
            return field.getName();
        }));
    }

    private static List<BulkLibs> bulkedLibs(Class<?> cls) {
        return Reflects.getAnnotation(cls, Bulked.class, ((fieldMap, field, fixed) -> {
            BulkLibs libs = BulkLibs.init();
            libs.setNaming(naming(fixed.value()));
            field.setAccessible(true);
            libs.setSf(field);
            // 查询目标属性
            libs.setSn(field.getName());
            String fields = fixed.fields();
            libs.setMultiple(false);
            // 如果映射有值，映射优先
            BulkMapping[] mapping = fixed.mapping();

            if (Lists.iterable(mapping)) {
                libs.setDestList(dest(fieldMap, mapping));
                libs.setMultiple(Lists.empty(libs.getDestList()).size() > 1);
                return libs;
            }
            libs.setDestList(dest(fieldMap, fixed.fields()));
            libs.setMultiple(Lists.empty(libs.getDestList()).size() > 1);
            return libs;
        }));
    }

    private static Set<BulkLibs.Dest> dest(Map<String, Field> fieldMap, BulkMapping[] mapping) {
        return Stream.of(mapping)
                .map(e -> {
                    // 源
                    String s = e.s();
                    // 目标
                    String d = e.d();

                    BulkLibs.Dest dest = new BulkLibs.Dest();
                    // 判断属性是否存在
                    Field df = fieldMap.get(d);
                    if (Objects.nonNull(df)) {
                        df.setAccessible(true);
                    }
                    dest.setName(s);
                    dest.setDn(d);
                    dest.setExists(Objects.nonNull(df));
                    return dest;
                }).collect(Collectors.toSet());
    }

    private static Set<BulkLibs.Dest> dest(Map<String, Field> fieldMap, String fields) {
        return Stream.of(fields)
                .map(e -> {
                    BulkLibs.Dest dest = new BulkLibs.Dest();
                    // 判断属性是否存在
                    Field df = fieldMap.get(e);
                    if (Objects.nonNull(df)) {
                        df.setAccessible(true);
                    }
                    dest.setDn(e);
                    dest.setExists(Objects.nonNull(df));
                    return dest;
                }).collect(Collectors.toSet());
    }


    public static Naming<?> naming(String naming) {
        if (StringUtils.isBlank(naming)) {
            return null;
        }
        try {
            // 优先通过class加载
            Class<? extends Naming> aClass = (Class<? extends Naming>) Class.forName(naming);
            log.debug("load by type success. naming = {}, class = {}", naming, aClass);
            return SpiSpringContext.getOneSpi(aClass);
        } catch (Exception e) {
            log.debug("load by type failed. naming = {}", naming);
            // 尝试通过名称获取
            Collection<Naming> spi = SpiSpringContext.getSpi(Naming.class);
            Naming loader = Lists.empty(spi)
                    .stream()
                    .filter(s -> Objects.equals(naming, s.name()))
                    .findAny().orElse(null);

            log.debug("load by name success. naming = {} instance = {}", naming, loader);
            return loader;
        }

    }

    public static <T> Map<Naming<?>, BulkedIds> ids(Object firstObject, Collection<T> objs, List<BulkLibs> fixedList) {
        if (firstObject instanceof Map) {
            return Maps.hashMap();
        }
        Map<? extends Naming<?>, List<BulkLibs>> namingLibs = Lists.empty(fixedList).stream().filter(e -> Objects.nonNull(e.getNaming())).collect(Collectors.groupingBy(e -> e.getNaming()));

        Map<Naming<?>, BulkedIds> idsMap = new HashMap<>();

        for (T obj : objs) {
            namingLibs.forEach((k, v) -> {
                BulkedIds bulkedIds = idsMap.get(k);
                if (Objects.isNull(bulkedIds)) {
                    bulkedIds = new BulkedIds();
                    idsMap.put(k, bulkedIds);
                }
                for (BulkLibs e : v) {
                    bulkedIds.setMultiple(bulkedIds.isMultiple() & e.isMultiple());
                    bulkedIds.getIds().add(ids(obj, e));
                }
                idsMap.put(k, bulkedIds);
            });
        }
//        namingLibs.forEach((k, v) -> {
//            BulkedIds bulkedIds = new BulkedIds();
//            for (BulkLibs e : v) {
//                bulkedIds.setMultiple(bulkedIds.isMultiple() & e.isMultiple());
//                Set<Object> ids = Lists.empty(objs).stream().map(x -> {
//                    return ids(x, e);
//
//                }).collect(Collectors.toSet());
//                bulkedIds.getIds().addAll(ids);
//            }
//            idsMap.put(k, bulkedIds);
//        });
        return idsMap;
//        return Lists.empty(fixedList)
//                .stream()
//                .filter(e -> Objects.nonNull(e.getNaming()))
//                .collect(Collectors.groupingBy(e -> e.getNaming(), Collectors.reducing(new BulkedIds(), e -> {
//                    BulkedIds bulkedIds = new BulkedIds();
//                    bulkedIds.setMultiple(bulkedIds.isMultiple() & e.isMultiple());
//                    Set<Object> ids = Lists.empty(objs).stream().map(v -> {
//                        return ids(v, e);
//
//                    }).collect(Collectors.toSet());
//                    bulkedIds.setIds(ids);
//                    return bulkedIds;
//                }, (x1, x2) -> {
//                    x1.setMultiple(x1.isMultiple() & x2.isMultiple());
//                    x1.getIds().addAll(x2.getIds());
//                    return x1;
//                })));


    }


    private static Object ids(Object s, BulkLibs e) {
        try {
            return e.getSf().get(s);
        } catch (IllegalAccessException ex) {
            return null;
        }
    }
}
