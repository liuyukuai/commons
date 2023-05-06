package com.cutefool.commons.mybatis;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.ProcessUtils;
import com.cutefool.commons.expression.libs.Conditional;
import com.cutefool.commons.expression.libs.Conditionals;
import com.cutefool.commons.orm.interfaces.IOperations;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author 271007729@qq.com
 * @date 2019-07-08 11:32
 */

@SuppressWarnings("unused")
public interface IBaseService<DTO, E, ID, Mapper extends IBaseMapper<E, ID, Example>, Example extends IExample<ID>> extends IExampleGetter<ID, Example>, IOperations<DTO, E, ID, Mapper> {
    /**
     * 获取当前mapper
     *
     * @return Mapper
     */
    Mapper getMapper();

    /**
     * 创建对象
     *
     * @param dto context
     * @return id
     */
    default E replace(DTO dto) {
        return this.replace(dto, (dest, source) -> {
        });
    }

    ;

    /**
     * 批量创建对象
     *
     * @param dtoList dtoList
     * @return true
     */
    default Boolean replace(List<DTO> dtoList) {
        return this.replace(dtoList, (d, s) -> {
        });
    }

    ;

    /**
     * 批量创建对象
     *
     * @param dtoList  dtoList
     * @param consumer consumer
     * @return true
     */
    default Boolean replace(List<DTO> dtoList, BiConsumer<E, DTO> consumer) {
        if (Lists.iterable(dtoList)) {
            BiConsumer<E, DTO> biConsumer = this.preCreate();
            if (Objects.nonNull(consumer)) {
                biConsumer = biConsumer.andThen(consumer);
            }
            List<E> es = ProcessUtils.processList(this.getDomainClass(), dtoList, biConsumer);
            int i = this.getRepository().batchReplace(es);
            for (int j = 0; j < es.size(); j++) {
                this.postCreate().accept(es.get(j), dtoList.get(j));
            }
            return i == dtoList.size();
        }
        return Boolean.FALSE;
    }

    ;

    /**
     * 创建对象
     *
     * @param dto      context
     * @param consumer 对象转换器
     * @return id
     */
    default E replace(DTO dto, BiConsumer<E, DTO> consumer) {
        this.valid(dto);
        E process = process(dto, this.preCreate().andThen(consumer));
        this.getRepository().replace(process);
        this.postCreate().accept(process, dto);
        return process;
    }

    ;

    /**
     * 更新对象
     *
     * @param id  id
     * @param dto dto对象
     * @return 更新后的对象
     */
    E updateWithBlobs(ID id, DTO dto);

    /**
     * 更新对象
     *
     * @param id       id
     * @param consumer 消费方法
     * @return 更新后的对象
     */
    E updateWithBlobs(ID id, Consumer<E> consumer);

    /**
     * 更新对象（增量更新）
     *
     * @param e       实体数据
     * @param example 创建需要更新的对象
     * @return 更新后的对象
     */
    E update(E e, Example example);

    /**
     * 更新对象
     *
     * @param e 实体数据
     * @return 更新后的对象
     */
    E updateWithBlobs(E e);

    /**
     * 更新对象
     *
     * @param id       id
     * @param dto      dto对象
     * @param consumer 对象转换器
     * @return 更新后的对象
     */
    E updateWithBlobs(ID id, DTO dto, BiConsumer<E, DTO> consumer);

    /**
     * 通过条件查询
     *
     * @param conditional conditional
     * @return list
     */
    List<E> list(Conditional conditional);

    /**
     * 通过条件查询
     *
     * @param conditional conditional
     * @param paging      paging
     * @return list
     */
    PageResponse<E> list(Conditional conditional, Paging paging);

    /**
     * 通过条件查询
     *
     * @param conditionals conditionals
     * @return list
     */
    List<E> list(Conditionals conditionals);

    /**
     * 通过条件查询
     *
     * @param conditionals conditionals
     * @param paging       paging
     * @return list
     */
    PageResponse<E> list(Conditionals conditionals, Paging paging);

    /**
     * 通过条件查询
     *
     * @param condition condition
     * @return list
     */
    List<E> list(Condition condition);

    /**
     * 通过条件查询
     *
     * @param condition condition
     * @param paging    paging
     * @return list
     */
    PageResponse<E> list(Condition condition, Paging paging);

    /**
     * 通过条件查询
     *
     * @param conditions conditions
     * @return list
     */
    default List<E> list(List<Condition> conditions) {
        return this.list(conditions, Operator.AND);
    }

    /**
     * 通过条件查询
     *
     * @param conditions conditions
     * @param paging     paging
     * @return list
     */
    default PageResponse<E> list(List<Condition> conditions, Paging paging) {
        return this.list(conditions, Operator.AND, paging);
    }

    /**
     * 通过条件查询
     *
     * @param conditions conditions
     * @param operator   operator
     * @return list
     */
    List<E> list(List<Condition> conditions, Operator operator);

    /**
     * 通过条件查询
     *
     * @param conditions conditions
     * @param operator   operator
     * @param paging     paging
     * @return list
     */
    PageResponse<E> list(List<Condition> conditions, Operator operator, Paging paging);

    /**
     * 通过example 查询
     *
     * @param example @param
     * @param paging  paging
     * @return list
     */
    PageResponse<E> list(Example example, Paging paging);

    @Override
    Optional<E> getById(ID id);


    /**
     * 自动处理内置变量
     * <p>
     * 自定义处理条件（条件中有自定义变量时使用）
     *
     * @param condition condition
     */
    default void postCondition(Condition condition) {

    }

    /**
     * 获取  Example对象
     *
     * @return example
     */
    Class<Example> getExampleClass();

    /**
     * 获取  Example对象
     *
     * @return
     */
    @Override
    @SuppressWarnings("ALL")
    default Example newExample() {
        Class exampleClass = this.getExampleClass();
        try {
            Constructor constructor = exampleClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Example example = (Example) constructor.newInstance();
            example.init();
            return example;
        } catch (Exception e) {
            throw new BizException(ResponseCode.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 获取  Example对象
     *
     * @param ids 主键集合
     * @return
     */
    @Override
    @SuppressWarnings("ALL")
    default Example newExample(List<ID> ids) {
        Example example = this.newExample();
        return (Example) example.ids(ids);
    }
}
