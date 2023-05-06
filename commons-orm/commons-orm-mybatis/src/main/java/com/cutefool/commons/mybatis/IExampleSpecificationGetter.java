package com.cutefool.commons.mybatis;

import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.Sort;

/**
 * @author 271007729@qq.com
 * @date 2019-07-10 02:31
 */
public interface IExampleSpecificationGetter<ID, Example extends IExample<ID>, Query> {

    /**
     * 通过自定义对象转换层Example对象
     *
     * @param query 自定义查询对象
     * @return Example
     */
    Example newExample(Query query);


    /**
     * 通过自定义对象，分页转换层Example对象
     *
     * @param query  查询对象
     * @param paging 分页对象
     * @return Example
     */
    default Example newExample(Query query, Paging paging) {
        Example example = this.newExample(query);
        return example.page(paging);
    }

    /**
     * 通过自定义对象，分页转换层Example对象
     *
     * @param query 查询对象
     * @param sorts sorts
     * @return Example
     */
    default Example newExample(Query query, Sort... sorts) {
        Example example = this.newExample(query);
        return example.sort(sorts);
    }

}