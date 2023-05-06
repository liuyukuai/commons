package com.cutefool.commons.mybatis.dsl;

import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import org.mybatis.dynamic.sql.SortSpecification;
import org.mybatis.dynamic.sql.where.AbstractWhereDSL;
import org.mybatis.dynamic.sql.where.WhereApplier;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface MybatisSpecificationExecutor<E, Query> {

    /**
     * 查询所有的对象
     *
     * @param sorts sorts
     * @return list
     */
    List<E> listSp(SortSpecification... sorts);


    /**
     * 查询所有的对象
     *
     * @param whereApplier whereApplier
     * @param sorts        sorts
     * @return list
     */
    List<E> listSp(WhereApplier whereApplier, SortSpecification... sorts);

    /**
     * 查询所有的对象
     *
     * @param consumer consumer
     * @param sorts    sorts
     * @return list
     */
    List<E> listSp(Consumer<AbstractWhereDSL<?>> consumer, SortSpecification... sorts);

    /**
     * 查询所有的对象
     *
     * @param sorts sorts
     * @return list
     */
    List<E> listSp(Collection<SortSpecification> sorts);

    /**
     * 查询所有的对象
     *
     * @param sorts sorts
     * @return list
     */
    List<E> listSp(WhereApplier whereApplier, Collection<SortSpecification> sorts);

    /**
     * 查询所有的对象
     *
     * @param sorts sorts
     * @return list
     */
    List<E> listSp(Consumer<AbstractWhereDSL<?>> consumer, Collection<SortSpecification> sorts);

    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @param sorts sorts
     * @return list
     */
    List<E> listBySpWhere(Query query, SortSpecification... sorts);

    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @param sorts sorts
     * @return list
     */
    List<E> listBySpWhere(Query query, Collection<SortSpecification> sorts);

    /**
     * 分页查询
     *
     * @param whereApplier 条件
     * @param paging       分页
     * @return page
     */
    PageResponse<E> listByWhere(WhereApplier whereApplier, Paging paging);
}
