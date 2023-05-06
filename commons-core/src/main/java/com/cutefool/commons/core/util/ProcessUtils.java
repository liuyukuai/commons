package com.cutefool.commons.core.util;

import com.cutefool.commons.core.page.PageResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * processUtils
 *
 * @author liuyk
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ProcessUtils {


    private static Cache<String, BeanCopier> BEAN_COPIER_MAP = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    private ProcessUtils() {
    }

    /**
     * 拷贝list对象
     *
     * @param clazz 目标类型
     * @param src   原对象集合
     * @param <T>   原数据类型
     * @param <R>   目标数据类型
     * @return 目标对象集合
     */
    public static <T, R> List<R> processList(Class<R> clazz, List<T> src) {
        return processList(clazz, src, (r, s) -> {
        });
    }

    /**
     * 拷贝list对象
     *
     * @param clazz      目标类型
     * @param src        原对象集合
     * @param <T>        原数据类型
     * @param <R>        目标数据类型
     * @param comparator 排序规则
     * @return 目标对象集合
     */
    public static <T, R> List<R> processList(Class<R> clazz, List<T> src, Comparator<R> comparator) {
        return processList(clazz, src, comparator, (r, s) -> {
        });
    }

    /**
     * 拷贝list对象
     *
     * @param clazz      目标类型
     * @param src        原对象集合
     * @param biConsumer 回调函数
     * @param <T>        原数据类型
     * @param <R>        目标数据类型
     * @return 目标对象集合
     */
    public static <T, R> List<R> processList(Class<R> clazz, List<T> src, BiConsumer<R, T> biConsumer) {
        if (!Lists.iterable(src)) {
            return Lists.newArrayList();
        }
        return src.stream().map(e -> process(clazz, e, biConsumer)).collect(Collectors.toList());
    }

    /**
     * 拷贝list对象
     *
     * @param clazz      目标类型
     * @param src        原对象集合
     * @param biConsumer 回调函数
     * @param <T>        原数据类型
     * @param <R>        目标数据类型
     * @param comparator 排序规则
     * @return 目标对象集合
     */
    public static <T, R> List<R> processList(Class<R> clazz, List<T> src, Comparator<R> comparator, BiConsumer<R, T> biConsumer) {
        if (!Lists.iterable(src)) {
            return Lists.newArrayList();
        }

        if (Objects.isNull(comparator)) {
            return processList(clazz, src, biConsumer);
        }

        return src.stream().map(e -> process(clazz, e, biConsumer)).sorted(comparator).collect(Collectors.toList());
    }

    /**
     * 拷贝单个对象
     *
     * @param clazz 目标类型
     * @param src   原对象
     * @param <T>   原数据类型
     * @param <R>   目标数据类型
     * @return 目标对象
     */
    public static <T, R> R process(Class<R> clazz, T src) {
        return process(clazz, src, (r, s) -> {
        });
    }

    /**
     * 拷贝单个对象
     *
     * @param clazz      目标类型
     * @param src        原对象
     * @param biConsumer 回调函数
     * @param <T>        原数据类型
     * @param <R>        目标数据类型
     * @return 目标对象
     */
    public static <T, R> R process(Class<R> clazz, T src, BiConsumer<R, T> biConsumer) {
        if (Objects.isNull(clazz)) {
            throw new IllegalArgumentException("the clazz argument must be not null");
        }
        if (Objects.isNull(src)) {
            return null;
        }
        try {
            R r = clazz.newInstance();
            processObject(r, src, biConsumer);
            return r;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * 拷贝单个对象
     *
     * @param r   目标对象
     * @param src 原对象
     * @param <T> 原数据类型
     * @param <R> 目标数据类型
     */
    public static <T, R> void processObject(R r, T src) {
        processObject(r, src, (r1, src1) -> {
        });
    }

    /**
     * 拷贝单个对象
     *
     * @param r          目标对象
     * @param src        原对象
     * @param biConsumer 回调函数
     * @param <T>        原数据类型
     * @param <R>        目标数据类型
     */

    public static <T, R> void processObject(R r, T src, BiConsumer<R, T> biConsumer) {

        try {
            if (Objects.isNull(src)) {
                return;
            }
            String beanKey = generateKey(src.getClass(), r.getClass());
            BeanCopier copier = BEAN_COPIER_MAP.getIfPresent(beanKey);
            if (copier == null) {
                copier = BeanCopier.create(src.getClass(), r.getClass(), false);
                BEAN_COPIER_MAP.put(beanKey, copier);
            }
            copier.copy(src, r, null);
            if (biConsumer != null) {
                biConsumer.accept(r, src);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * 拷贝分页对象
     *
     * @param clazz        目标类型
     * @param pageResponse 原对象
     * @param <T>          原数据类型
     * @param <R>          目标数据类型
     * @return 目标对象
     */
    public static <T, R> PageResponse<R> processPage(Class<R> clazz, PageResponse<T> pageResponse) {
        return processPage(clazz, pageResponse, (r1, src1) -> {
        });
    }

    /**
     * 拷贝分页对象
     *
     * @param clazz        目标类型
     * @param pageResponse 原对象
     * @param <T>          原数据类型
     * @param <R>          目标数据类型
     * @param comparator   排序规则
     * @return 目标对象
     */
    public static <T, R> PageResponse<R> processPage(Class<R> clazz, PageResponse<T> pageResponse, Comparator<T> comparator) {
        return processPage(clazz, pageResponse, (r1, src1) -> {
        });
    }

    /**
     * 拷贝分页对象
     *
     * @param clazz        目标类型
     * @param pageResponse 原对象
     * @param biConsumer   回调函数
     * @param <T>          原数据类型
     * @param <R>          目标数据类型
     * @return 目标对象
     */
    public static <T, R> PageResponse<R> processPage(Class<R> clazz, PageResponse<T> pageResponse, BiConsumer<R, T> biConsumer) {
        if (pageResponse == null || pageResponse.isEmpty()) {
            return PageResponse.empty();
        }
        return PageResponse.apply(pageResponse.getTotal(), processList(clazz, pageResponse.getList(), biConsumer));
    }

    /**
     * 拷贝分页对象
     *
     * @param clazz        目标类型
     * @param pageResponse 原对象
     * @param biConsumer   回调函数
     * @param <T>          原数据类型
     * @param <R>          目标数据类型
     * @param comparator   排序规则
     * @return 目标对象
     */
    public static <T, R> PageResponse<R> processPage(Class<R> clazz, PageResponse<T> pageResponse, Comparator<R> comparator, BiConsumer<R, T> biConsumer) {
        if (pageResponse == null || pageResponse.isEmpty()) {
            return PageResponse.empty();
        }
        return PageResponse.apply(pageResponse.getTotal(), processList(clazz, pageResponse.getList(), comparator, biConsumer));
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return String.join(",", class1.toString(), class2.toString());
    }
}
