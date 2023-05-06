package com.cutefool.commons.orm.jpa.utils;

import com.cutefool.commons.core.function.ThirdFunction;
import com.cutefool.commons.core.page.Sort;
import org.springframework.data.domain.PageRequest;

import java.util.Collection;
import java.util.List;

/**
 * @author : liuyk
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
        return PageRequest.of(page - 1, size, this.apply(sorts));
    }

    /**
     * 排序规则
     *
     * @param sorts 排序规则
     * @return Jpa 排序
     */
    org.springframework.data.domain.Sort apply(Collection<Sort> sorts);
}
