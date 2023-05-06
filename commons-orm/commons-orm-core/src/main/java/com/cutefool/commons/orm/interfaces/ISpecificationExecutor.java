package com.cutefool.commons.orm.interfaces;


import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.Sort;
import com.cutefool.commons.core.util.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 多条件查询接口
 *
 * @author 271007729@qq.com
 * @date 2019-07-10 01:33
 */

@SuppressWarnings("unused")
public interface ISpecificationExecutor<E, Query> {

    /**
     * 查询总数
     *
     * @param query query
     * @return
     */
    Long count(Query query);

    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @return list
     */
    List<E> listByWhere(Query query);

    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @param sorts sorts
     * @return list
     */
    List<E> listByWhere(Query query, Sort... sorts);

    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @param sorts sorts
     * @return list
     */
    List<E> listByWhere(Query query, Collection<Sort> sorts);

    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @return list
     */
    List<E> listByWhere(Query query, String sort);

    /**
     * 查询所有的对象
     *
     * @param query  多条件查询对象
     * @param paging paging
     * @return list
     */
    PageResponse<E> listByWhere(Query query, Paging paging);

    /**
     * 查询所有的对象
     *
     * @param paging paging
     * @return list
     */
    default List<E> listByWithoutCnt(Paging paging) {
        throw new BizException("not support.");
    }

    ;

    /**
     * 查询所有的对象
     *
     * @param query  多条件查询对象
     * @param paging paging
     * @return list
     */
    default List<E> listByWithoutCnt(Query query, Paging paging) {
        throw new BizException("not support.");
    }

    default <K> Map<K, E> mapBy(Query query, Function<E, K> keyClassifier) {
        return this.mapBy(query, keyClassifier, e -> e);
    }

    default <K, V> Map<K, V> mapBy(Query query, Function<E, K> keyClassifier, Function<E, V> classifier) {
        return Lists.empty(this.listByWhere(query))
                .stream()
                .collect(Collectors.toMap(keyClassifier, classifier, (x1, x2) -> x1));
    }

    default <T> Map<T, List<E>> groupBy(Query query, Function<E, T> classifier) {
        List<E> entities = this.listByWhere(query);
        return Lists.empty(entities).stream().collect(Collectors.groupingBy(classifier));
    }

    default <T, V> Map<T, List<V>> groupBy(Query query, Function<E, T> classifier, Function<E, V> mappingClassifier) {
        List<E> entities = this.listByWhere(query);
        Collector<Object, ?, Long> counting = Collectors.counting();
        return Lists.empty(entities)
                    .stream()
                    .collect(Collectors.groupingBy(classifier, Collectors.mapping(mappingClassifier, Collectors.toList())));
    }

    default <T, V> Map<T, V> groupBy(Query query, Function<E, T> classifier, Collector<E, ?, V> collector) {
        List<E> entities = this.listByWhere(query);
        return Lists.empty(entities).stream().collect(Collectors.groupingBy(classifier, collector));
    }

}
