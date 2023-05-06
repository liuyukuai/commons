package com.cutefool.commons.bulk;

import com.cutefool.commons.core.util.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.Naming;
import com.cutefool.commons.core.util.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings("ALL")
public class BulkOperations {


    public static <R, T> R bulking(Class<R> cls, T obj, BulkContext context) {
        return bulking(cls, obj, context, (d, s) -> {
        });
    }


    public static <R, T> List<R> bulking(Class<R> cls, List<T> objs, BulkContext context) {
        return bulkingList(cls, objs, context, (d, s) -> {
        });
    }


    public static <R, T> Object bulking(T obj, BulkContext context) {
        if (Objects.isNull(obj)) {
            return null;
        }
        if (obj instanceof Collection) {
            return bulkingList((Collection<?>) obj, context, (d, s) -> {
            });
        }

        return bulking(obj, context, (d, s) -> {
        });
    }


    public static <R, T> List<Object> bulking(Collection<T> objs, BulkContext context) {
        return bulkingList(objs, context, (d, s) -> {
        });
    }


    public static <R, T> R bulking(Class<R> cls, T obj, BulkContext context, BiConsumer<R, T> consumer) {
        List<BulkLibs> bulkLibs = BulkParser.listBulkedFields(obj, context);
        Map<Naming<?>, BulkedIds> ids = BulkParser.ids(obj, Collections.singletonList(obj), bulkLibs);
        Map<Naming<?>, Map> idsMap = doLoading(ids);
        Map<String, Field> declaredFields = Reflects.findDeclaredFields(cls);
        BiConsumer<R, T> fixedConsumer = (d, s) -> Lists.empty(bulkLibs).forEach(v -> bulking(v, s, idsMap, (dest, sx) -> {
            // 源属性
            try {
                Field field = declaredFields.get(dest.getDn());
                if (Objects.nonNull(field)) {
                    field.set(d, sx);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }));

        consumer = Objects.nonNull(consumer) ? fixedConsumer.andThen(consumer) : fixedConsumer;
        return ProcessUtils.process(cls, obj, consumer);
    }


    public static <R, T> Object bulking(T obj, BulkContext context, BiConsumer<R, T> consumer) {
        List<BulkLibs> bulkLibs = BulkParser.listBulkedFields(obj, context);
        Map<Naming<?>, BulkedIds> ids = BulkParser.ids(obj, Collections.singletonList(obj), bulkLibs);
        Map<Naming<?>, Map> idsMap = doLoading(ids);
        return bulking(obj, bulkLibs, idsMap, context);
    }


    public static <R, T> List<R> bulkingList(Class<R> cls, List<T> objs, BulkContext context, BiConsumer<R, T> consumer) {
        // 如果是数组
        if (Lists.isEmpty(objs)) {
            return Lists.newArrayList();
        }
        Object firstObject = BulkParser.findFirstObject(objs);
        List<BulkLibs> bulkLibs = BulkParser.listBulkedFields(firstObject, context);
        Map<Naming<?>, BulkedIds> ids = BulkParser.ids(firstObject, objs, bulkLibs);
        Map<Naming<?>, Map> idsMap = doLoading(ids);
        Map<String, Field> declaredFields = Reflects.findDeclaredFields(cls);
        BiConsumer<R, T> fixedConsumer = (d, s) -> Lists.empty(bulkLibs).forEach(v -> bulking(v, s, idsMap, (dest, sx) -> {
            // 源属性
            try {
                Field field = declaredFields.get(dest.getDn());
                if (Objects.nonNull(field)) {
                    field.setAccessible(true);
                    field.set(d, sx);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }));
        consumer = Objects.nonNull(consumer) ? fixedConsumer.andThen(consumer) : fixedConsumer;
        return ProcessUtils.processList(cls, objs, consumer);
    }


    public static <R, T> List<Object> bulkingList(Collection<T> objs, BulkContext context, BiConsumer<R, T> consumer) {
        // 如果是数组
        if (Lists.isEmpty(objs)) {
            return Lists.newArrayList();
        }
        Object firstObject = BulkParser.findFirstObject(objs);
        List<BulkLibs> bulkLibs = BulkParser.listBulkedFields(firstObject, context);
        Map<Naming<?>, BulkedIds> ids = BulkParser.ids(firstObject, objs, bulkLibs);
        Map<Naming<?>, Map> idsMap = doLoading(ids);
        return objs.stream().map(e -> bulking(e, bulkLibs, idsMap, context)).collect(Collectors.toList());
    }


    private static Map<Naming<?>, Map> doLoading(Map<Naming<?>, BulkedIds> ids) {
        return Maps.empty(ids)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> {
                    BulkedIds v = e.getValue();
                    if (v.isMultiple()) {
                        return e.getKey().namesMultiple(v.getIds());
                    }
                    return e.getKey().names(v.getIds());
                }));

    }


    private static Object bulking(Object obj, List<BulkLibs> bulkLibs, Map<Naming<?>, Map> idsMap, BulkContext context) {
        Optional<Map<String, Object>> optional = JsonUtils.toMap(obj);
        if (!optional.isPresent()) {
            return obj;
        }

        Map<String, Object> data = BulkParser.filter(optional.get(), context, BulkParser.ignoreFields(obj.getClass()));
        if (Maps.isEmpty(idsMap)) {
            return data;
        }
        Lists.empty(bulkLibs).forEach(e -> bulking(e, obj, idsMap, (dest, o) -> data.put(dest.getDn(), o)));
        return data;
    }


    private static void bulking(BulkLibs v, Object s, Map<Naming<?>, Map> idsMap, BiConsumer<BulkLibs.Dest, Object> consumer) {
        try {
            // 是否多属性加载
            Set<BulkLibs.Dest> destList = v.getDestList();
            if (Lists.isEmpty(destList)) {
                return;
            }
            Naming<?> naming = v.getNaming();

            if (Objects.isNull(naming)) {
                log.warn("naming is null. skip bulking");
                return;
            }


            if (!v.isMultiple()) {
                Optional<BulkLibs.Dest> first = destList.stream().findFirst();
                if (first.isPresent()) {
                    BulkLibs.Dest dest = first.get();
                    // 如果是单属性查询
                    Map<?, String> names = Maps.empty(idsMap.get(naming));
                    // 判断目标属性是否存在
                    Object o = v.getSf().get(s);
                    consumer.accept(dest, names.get(o));
                    return;
                }
            }
            // 多属性查询
            Map<Object, Map<String, Object>> map = Maps.empty(idsMap.get(naming));

            for (BulkLibs.Dest dest : destList) {
                // 源id
                Object o = v.getSf().get(s);
                // 子属性
                Map<String, Object> subMap = Maps.empty(map.get(o));
                // 源属性
                consumer.accept(dest, subMap.get(dest.getName()));
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        C c1 = new C();
        c1.setId1(100L);
        c1.setB("b100");
        c1.setC("c100");

        C c2 = new C();
        c2.setId2(101L);
        c2.setB("b101");
        c2.setC("c101");
        Object fill = bulking(C.class, Arrays.asList(c1, c2), BulkContext.init());
        System.out.println(JsonUtils.toJson(fill));


    }

    @Data
    public static class A {
//        @Bulked(value = "com.cutefool.commons.core.fixed.", mapping = {@BulkMapping(s = "mobile", d = "mobile1"), @BulkMapping(s = "name", d = "name1")})


        @Bulked(value = "com.cutefool.commons.bulk.Loader1", fields = "idName1")
        private Long id1;

        private String idName1;

        @Bulked(value = "com.cutefool.commons.bulk.Loader2", fields = "idName2")
        private Long id2;

        private String idName2;


        private String name1;

        private String mobile1;
    }

    @Data
    public static class B extends A {
        private String b;
    }

    @Data
    public static class C extends B {
        private String c;
    }

    public static class Loader1 implements Naming<Long> {


        @Override
        public Map names(Collection<Long> collection) {
            return Maps.hashMap(101L, "1022");
        }

        @Override
        public Map<Long, Map<String, Object>> namesMultiple(Collection<Long> longs) {
            Map<String, Object> map1 = Maps.hashMap();
            map1.put("id1", "zs101");
            map1.put("mobile", "139101");

            Map<String, Object> map2 = Maps.hashMap();
            map2.put("id2", "zs102");
            map2.put("mobile", "139100");
            Map<Long, Map<String, Object>> longMapMap = Maps.hashMap();
            longMapMap.put(100L, map2);
            longMapMap.put(101L, map1);
            return longMapMap;
        }


        @Override
        public String names(Long aLong) {
            return "aname";
        }
    }

    public static class Loader2 implements Naming<Long> {


        @Override
        public Map names(Collection<Long> collection) {
            return Maps.hashMap(102L, "123");
        }

        @Override
        public Map<Long, Map<String, Object>> namesMultiple(Collection<Long> longs) {
            Map<String, Object> map1 = Maps.hashMap();
            map1.put("name", "zs101");
            map1.put("mobile", "139101");

            Map<String, Object> map2 = Maps.hashMap();
            map2.put("name", "zs100");
            map2.put("mobile", "139100");
            Map<Long, Map<String, Object>> longMapMap = Maps.hashMap();
            longMapMap.put(100L, map2);
            longMapMap.put(101L, map1);
            return longMapMap;
        }


        @Override
        public String names(Long aLong) {
            return "aname";
        }
    }


}
