package com.cutefool.commons.mybatis.support;

import com.cutefool.commons.mybatis.IBaseMapper;
import com.cutefool.commons.mybatis.IBaseSpecificationService;
import com.cutefool.commons.mybatis.IExample;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.Sort;
import com.cutefool.commons.core.page.Sorts;
import com.cutefool.commons.core.util.Lists;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 2019-07-08 11:32
 */

@SuppressWarnings("unused")
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public class BaseSpecificationMybatisService<DTO, E, ID, Mapper extends IBaseMapper<E, ID, Example>, Example extends IExample<ID>, Query> extends BaseMybatisService<DTO, E, ID, Mapper, Example> implements IBaseSpecificationService<DTO, E, ID, Mapper, Example, Query> {

    @Override
    public Long count(Query query) {
        return this.getMapper().countByExample(this.newExample(query));
    }

    @Override
    public List<E> listByWhere(Query query) {
        Example example = newExample(query);
        return this.getRepository().selectByExample(example);

    }

    @Override
    public PageResponse<E> listByWhere(Query query, Paging paging) {
        Example example = newExample(query, paging);
        List<E> elements = this.getRepository().selectByExample(example);
        long total = this.getRepository().countByExample(example);
        return PageResponse.apply(total, elements);
    }

    @Override
    public List<E> listByWithoutCnt(Paging paging) {
        Example example = newExample(paging);
        return this.getRepository().selectByExample(example);
    }

    @Override
    public List<E> listByWithoutCnt(Query query, Paging paging) {
        Example example = newExample(query, paging);
        return this.getRepository().selectByExample(example);
    }

    @Override
    @SuppressWarnings("ALL")
    public Class<E> getDomainClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), BaseSpecificationMybatisService.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[1];
    }

    @Override
    @SuppressWarnings("ALL")
    public Class<Example> getExampleClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), BaseSpecificationMybatisService.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<Example>) genericInterfaces.getActualTypeArguments()[4];
    }

    @Override
    public List<E> listByWhere(Query query, Sort... sorts) {
        Example example = newExample(query, sorts);
        return this.getRepository().selectByExample(example);
    }

    @Override
    public List<E> listByWhere(Query query, Collection<Sort> sorts) {
        Example example = newExample(query, Lists.empty(sorts).toArray(new Sort[]{}));
        return this.getRepository().selectByExample(example);
    }

    @Override
    public List<E> listByWhere(Query query, String sort) {
        return listByWhere(query, Sorts.newInstance().sorts(sort).toArray(new Sort[]{}));
    }


    @Override
    public <K> Map<K, E> mapBy(Query query, Function<E, K> keyClassifier) {
        return this.mapBy(query, keyClassifier, e -> e);
    }

    @Override
    public <K, V> Map<K, V> mapBy(Query query, Function<E, K> keyClassifier, Function<E, V> classifier) {
        return Lists.empty(this.listByWhere(query))
                    .stream()
                    .collect(Collectors.toMap(keyClassifier, classifier, (x1, x2) -> x1));
    }

    @Override
    public <T> Map<T, List<E>> groupBy(Query query, Function<E, T> classifier) {
        List<E> entities = this.listByWhere(query);
        return Lists.empty(entities).stream().collect(Collectors.groupingBy(classifier));
    }

    @Override
    public <T, V> Map<T, List<V>> groupBy(Query query, Function<E, T> classifier, Function<E, V> mappingClassifier) {
        List<E> entities = this.listByWhere(query);
        Collector<Object, ?, Long> counting = Collectors.counting();
        return Lists.empty(entities)
                .stream()
                .collect(Collectors.groupingBy(classifier, Collectors.mapping(mappingClassifier, Collectors.toList())));
    }

    @Override
    public <T, V> Map<T, V> groupBy(Query query, Function<E, T> classifier, Collector<E, ?, V> collector) {
        List<E> entities = this.listByWhere(query);
        return Lists.empty(entities).stream().collect(Collectors.groupingBy(classifier, collector));
    }

    @Override
    public Example newExample(Query query) {
        return null;
    }
}
