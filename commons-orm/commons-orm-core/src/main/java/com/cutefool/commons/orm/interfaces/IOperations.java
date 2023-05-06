/*
 *
 */
package com.cutefool.commons.orm.interfaces;

import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.Sort;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.ProcessUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 11/10/21 5:38 PM
 */
@SuppressWarnings("unused")
public interface IOperations<DTO, E, ID, R> extends Validator<DTO, ID>, IRepository<E, ID>, IConditionOperations<E> {

    /**
     * 获取当前repository
     *
     * @return Mapper
     */
    R getRepository();

    /**
     * 创建对象
     *
     * @param dto context
     * @return id
     */
    E create(DTO dto);


    /**
     * 批量创建对象
     *
     * @param dtoList dtoList
     * @return true
     */
    Boolean create(List<DTO> dtoList);


    /**
     * 批量创建对象
     *
     * @param dtoList  dtoList
     * @param consumer consumer
     * @return true
     */
    Boolean create(List<DTO> dtoList, BiConsumer<E, DTO> consumer);


    /**
     * 创建对象
     *
     * @param dto      context
     * @param consumer 对象转换器
     * @return id
     */
    E create(DTO dto, BiConsumer<E, DTO> consumer);


    /**
     * 更新对象
     *
     * @param id  id
     * @param dto dto对象
     * @return 更新后的对象
     */
    E update(ID id, DTO dto);


    /**
     * 更新对象
     *
     * @param id       id
     * @param consumer 消费方法
     * @return 更新后的对象
     */
    E update(ID id, Consumer<E> consumer);


    /**
     * 更新对象
     *
     * @param e 实体数据
     * @return 更新后的对象
     */
    E update(E e);


    /**
     * 更新对象
     *
     * @param id       id
     * @param dto      dto对象
     * @param consumer 对象转换器
     * @return 更新后的对象
     */
    E update(ID id, DTO dto, BiConsumer<E, DTO> consumer);

    /**
     * 通过id删除对象
     *
     * @param id id
     */
    void delete(ID id);

    /**
     * 通过id删除对象（批量）
     *
     * @param ids ids
     */
    void delete(List<ID> ids);


    /**
     * 通过id查询对象
     *
     * @param id id
     * @return e
     */
    Optional<E> getById(ID id);


    /**
     * 通过名称查询对象
     *
     * @param name name
     * @return e
     */
    default Optional<E> getByName(String name) {
        return Optional.empty();
    }

    /**
     * 通过id查询名称是否重复
     *
     * @param id   id
     * @param name name
     * @return ture or false
     */
    default Boolean validateName(ID id, String name) {
        return Boolean.FALSE;
    }

    /**
     * 通过id查询对象（批量）
     *
     * @param ids ids
     * @return list
     */
    List<E> getById(Collection<ID> ids);

    /**
     * 查询所有的对象
     *
     * @return list
     */
    List<E> list();

    /**
     * 查询所有的对象
     *
     * @param sort sort
     * @return list
     */
    List<E> list(String sort);

    /**
     * 查询所有的对象
     *
     * @param sorts 排序规则
     * @return list
     */
    List<E> list(Sort... sorts);


    /**
     * 查询所有的对象（分页）
     *
     * @param paging 分页对象
     * @return list
     */
    PageResponse<E> list(Paging paging);

    /**
     * 查询总数
     *
     * @return total
     */
    Long count();


    /**
     * 通过id获取某个属性的值
     *
     * @param id              ID
     * @param valueClassifier 属性函数
     * @param <V>             值类型
     * @return 值
     */
    default <V> Optional<V> fieldBy(ID id, Function<E, V> valueClassifier) {
        return this.getById(id).map(valueClassifier);
    }

    /**
     * 通过id获取某个属性的值
     *
     * @param id              ID
     * @param valueClassifier 属性函数
     * @param defaultValue    默认值
     * @param <V>             值类型
     * @return 值
     */
    default <V> V fieldBy(ID id, Function<E, V> valueClassifier, V defaultValue) {
        if (Objects.isNull(id)) {
            return defaultValue;
        }
        Objects.requireNonNull(valueClassifier);
        return this.getById(id).map(valueClassifier).orElse(defaultValue);
    }

    /**
     * 通过id获取某个属性的值
     *
     * @param ids             ids
     * @param keyClassifier   属性函数
     * @param valueClassifier 属性函数
     * @param <V>             值类型
     * @return 值
     */
    default <V> Map<ID, V> fieldBy(List<ID> ids, Function<E, ID> keyClassifier, Function<E, V> valueClassifier) {
        if (Lists.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Objects.requireNonNull(keyClassifier);
        Objects.requireNonNull(valueClassifier);
        return Lists.empty(this.getById(ids)).stream().collect(Collectors.toMap(keyClassifier, valueClassifier));
    }

    /**
     * 转map查询
     *
     * @param keyClassifier 主键函数
     * @param <K>           主键类型
     * @return map
     */
    default <K> Map<K, E> mapBy(Function<E, K> keyClassifier) {
        return this.mapByFilter(keyClassifier, e -> e, e -> true);
    }

    /**
     * 转map查询
     *
     * @param keyClassifier 主键函数
     * @param <K>           主键类型
     * @param predicate     过滤函数
     * @return map
     */
    default <K> Map<K, E> mapByFilter(Function<E, K> keyClassifier, Predicate<? super E> predicate) {
        return this.mapByFilter(keyClassifier, e -> e, predicate);
    }

    /**
     * 转map查询
     *
     * @param keyClassifier 主键函数
     * @param <K>           主键类型
     * @param classifier    值函数
     * @return map
     */
    default <K, V> Map<K, V> mapBy(Function<E, K> keyClassifier, Function<E, V> classifier) {
        return this.mapByFilter(keyClassifier, classifier, e -> true);
    }

    /**
     * 转map查询
     *
     * @param keyClassifier 主键函数
     * @param <K>           主键类型
     * @param classifier    值函数
     * @param predicate     过滤函数
     * @return map
     */
    default <K, V> Map<K, V> mapByFilter(Function<E, K> keyClassifier, Function<E, V> classifier, Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return Lists.empty(this.list())
                .stream()
                .filter(predicate)
                .collect(Collectors.toMap(keyClassifier, classifier, (x1, x2) -> x1));
    }

    /**
     * 分组查询
     *
     * @param classifier 主键函数
     * @param <T>        主键类型
     * @return map
     */
    default <T> Map<T, List<E>> groupBy(Function<E, T> classifier) {
        List<E> entities = this.list();
        return Lists.empty(entities).stream().collect(Collectors.groupingBy(classifier));
    }

    /**
     * 分组查询
     *
     * @param classifier 主键函数
     * @param <T>        主键类型
     * @param predicate  过滤函数
     * @return map
     */
    default <T> Map<T, List<E>> groupBy(Function<E, T> classifier, Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        List<E> entities = this.list();
        return Lists.empty(entities).stream().filter(predicate).collect(Collectors.groupingBy(classifier));
    }

    /**
     * 分组查询
     *
     * @param classifier        主键函数
     * @param <T>               主键类型
     * @param mappingClassifier 值函数
     * @return map
     */
    default <T, V> Map<T, List<V>> groupBy(Function<E, T> classifier, Function<E, V> mappingClassifier) {
        List<E> entities = this.list();
        return Lists.empty(entities)
                .stream()
                .collect(Collectors.groupingBy(classifier, Collectors.mapping(mappingClassifier, Collectors.toList())));
    }

    /**
     * 分组查询
     *
     * @param classifier        主键函数
     * @param <T>               主键类型
     * @param mappingClassifier 值函数
     * @param predicate         过滤函数
     * @return map
     */
    default <T, V> Map<T, List<V>> groupBy(Function<E, T> classifier, Function<E, V> mappingClassifier, Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        List<E> entities = this.list();
        return Lists.empty(entities)
                .stream()
                .filter(predicate)
                .collect(Collectors.groupingBy(classifier, Collectors.mapping(mappingClassifier, Collectors.toList())));
    }

    /**
     * 分组查询
     *
     * @param classifier 主键函数
     * @param collector  Collectors
     * @param <T>        主键类型
     * @param <V>        值类型
     * @return map
     */
    default <T, V> Map<T, V> groupBy(Function<E, T> classifier, Collector<E, ?, V> collector) {
        return Lists.empty(this.list()).stream().collect(Collectors.groupingBy(classifier, collector));
    }

    /**
     * context convert to e
     *
     * @param dto context
     * @return e
     */
    default E process(DTO dto) {
        return process(dto, preCreate());
    }

    /**
     * context convert to e
     *
     * @param dto      context
     * @param consumer process
     * @return e
     */

    default E process(DTO dto, BiConsumer<E, DTO> consumer) {
        Class<E> clazz = this.getDomainClass();
        return ProcessUtils.process(clazz, dto, consumer);
    }


    /**
     * 转换对象
     *
     * @param e        目标对象
     * @param dto      原对象
     * @param consumer 额外转换器
     */
    default void process(E e, DTO dto, BiConsumer<E, DTO> consumer) {
        ProcessUtils.processObject(e, dto, consumer);
    }

    /**
     * 创建对象前置方法
     *
     * @return 返回对象转换器
     */
    default BiConsumer<E, DTO> preCreate() {
        return (dest, source) -> {
        };
    }

    /**
     * 创建对象后置通知
     *
     * @return consumer
     */
    default BiConsumer<E, DTO> postCreate() {
        return (dest, source) -> {
        };
    }

    /**
     * 更新对象前置方法
     *
     * @return 返回对象转换器
     */
    default BiConsumer<E, DTO> preUpdate() {
        return (dest, source) -> {
        };
    }


    /**
     * 后置通知(更新)
     *
     * @return biConsumer
     */
    default BiConsumer<E, DTO> postUpdate() {
        return (dest, source) -> {
        };
    }


    /**
     * 后置通知(更新)
     *
     * @param isUpdate 是否有更新
     * @return biConsumer
     */
    default BiConsumer<E, DTO> postUpdate(boolean isUpdate) {
        return (dest, source) -> {
        };
    }

    /**
     * 后置通知(删除)
     *
     * @return biConsumer
     */
    default Consumer<List<ID>> postDelete() {
        return (dest) -> {
        };
    }

    /**
     * get class
     *
     * @return class
     */

    Class<E> getDomainClass();
}
