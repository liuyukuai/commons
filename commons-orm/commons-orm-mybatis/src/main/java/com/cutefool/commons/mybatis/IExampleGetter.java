package com.cutefool.commons.mybatis;

import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.Sort;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 2019-07-10 02:31
 */
@SuppressWarnings("unused")
public interface IExampleGetter<ID, Example extends IExample<ID>> {
    /**
     * 创建Example对象
     *
     * @return Example
     */
    Example newExample();

    /**
     * 通过id集合创建Example对象
     *
     * @param ids 主键集合
     * @return Example
     */
    Example newExample(List<ID> ids);


    /**
     * 创建Example对象
     *
     * @param name name
     * @return Example
     */
    default Example newExample(String name) {
        return newExample();
    }

    /**
     * 通过分页创建Example对象
     *
     * @param paging 分页对象
     * @return Example
     */
    default Example newExample(Paging paging) {
        Example example = newExample();
        return example.page(paging);
    }

    /**
     * 通过分页创建Example对象
     *
     * @param sorts sorts
     * @return Example
     */
    default Example newExample(Sort... sorts) {
        Example example = newExample();
        return example.sort(sorts);
    }

}
