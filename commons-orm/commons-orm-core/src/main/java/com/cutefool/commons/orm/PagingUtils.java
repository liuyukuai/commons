package com.cutefool.commons.orm;


import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 分页工具类
 *
 * @author 271007729@qq.com
 * @date 2019-07-10 01:33
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class PagingUtils {

    private static PageFunction defaultFunction = (sorts -> {
        if (sorts.isEmpty()) {
            return Collections.emptyList();
        }
        return sorts;
    });

    public static PageRequest of(Paging paging) {
        return of(paging, defaultFunction);
    }


    public static PageRequest of(Paging paging, PageFunction pageFunction) {
        return paging.get(pageFunction);
    }


    public static List<Sort> of(Sort... sorts) {
        return defaultFunction.apply(Arrays.asList(sorts));
    }
}
