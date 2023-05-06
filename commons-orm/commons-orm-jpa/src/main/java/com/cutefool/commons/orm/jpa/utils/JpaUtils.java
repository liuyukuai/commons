package com.cutefool.commons.orm.jpa.utils;

import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.ProcessUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JpaUtils {

    private static final PageFunction defaultFunction = (sorts -> {
        if (sorts.isEmpty()) {
            return Sort.unsorted();
        }
        List<Sort.Order> orders = sorts.stream().map(sort -> new Sort.Order(Sort.Direction.fromString(sort.getDirection()), sort.getName())).collect(Collectors.toList());
        return Sort.by(orders);
    });

    public static PageRequest of(Paging paging) {
        return of(paging, defaultFunction);
    }


    public static PageRequest of(Paging paging, PageFunction pageFunction) {
        return paging.get(pageFunction);
    }

    public static <E> PageResponse<E> of(Page<E> page) {
        return PageResponse.apply(page.getTotalElements(), page.getContent());
    }

    public static <E, T> PageResponse<T> of(Page<E> page, Class<T> t) {
        return of(page, t, null);
    }

    public static <E, T> PageResponse<T> of(Page<E> page, Class<T> t, BiConsumer<T, E> biConsumer) {
        List<E> content = page.getContent();
        if (Lists.iterable(content)) {
            return PageResponse.apply(page.getTotalElements(), ProcessUtils.processList(t, content, biConsumer));
        }
        return PageResponse.apply(page.getTotalElements(), Lists.newArrayList());
    }

    public static Sort of(com.cutefool.commons.core.page.Sort... sorts) {
        return defaultFunction.apply(Arrays.asList(sorts));
    }

    public static Sort of(Collection<com.cutefool.commons.core.page.Sort> sorts) {
        return defaultFunction.apply(sorts);
    }
}
