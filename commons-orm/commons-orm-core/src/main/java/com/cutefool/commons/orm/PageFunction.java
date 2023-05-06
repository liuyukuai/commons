package com.cutefool.commons.orm;

import com.cutefool.commons.core.function.ThirdFunction;
import com.cutefool.commons.core.page.Sort;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2019-07-10 01:33
 */
@FunctionalInterface
public interface PageFunction extends ThirdFunction<PageRequest, Integer, Integer, List<Sort>> {
    /**
     * create  PageRequest object
     *
     * @param page  page
     * @param size  size
     * @param sorts sorts
     * @return pageRequest
     */
    @Override
    default PageRequest apply(Integer page, Integer size, List<Sort> sorts) {
        return new PageRequest(page - 1, size, this.apply(sorts));
    }

    /**
     * 排序规则
     *
     * @param sorts 排序规则
     * @return Jpa 排序
     */
    List<Sort> apply(List<Sort> sorts);
}
