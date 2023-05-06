package com.cutefool.commons.mybatis.support;

import com.cutefool.commons.core.page.*;
import com.cutefool.commons.mybatis.Condition;
import com.cutefool.commons.mybatis.IBaseMapper;
import com.cutefool.commons.mybatis.IBaseService;
import com.cutefool.commons.mybatis.IExample;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.expression.libs.Conditional;
import com.cutefool.commons.expression.libs.Conditionals;
import com.cutefool.commons.orm.support.BasicOperations;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author 271007729@qq.com
 * @date 2019-07-08 11:32
 */

@SuppressWarnings("unused")
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public abstract class BaseMybatisService<DTO, E, ID, Mapper extends IBaseMapper<E, ID, Example>, Example extends IExample<ID>> extends BasicOperations<DTO, E, ID, Mapper> implements IBaseService<DTO, E, ID, Mapper, Example> {

    @Override
    public Mapper getMapper() {
        return this.getRepository();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E nativeCreate(E e) {
        this.getRepository().insert(e);
        return e;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int nativeCreate(List<E> es) {
        return this.getRepository().batchInsert(es);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int nativeUpdate(E e) {
        return this.getRepository().updateByPrimaryKey(e);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void nativeDelete(ID id) {
        this.getRepository().deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void nativeDelete(List<ID> ids) {
        this.getRepository().deleteByExample(newExample(ids));
    }

    @Override
    public E nativeById(ID id) {
        return this.getRepository().selectByPrimaryKey(id);
    }

    @Override
    public List<E> nativeById(Collection<ID> ids) {
        return this.getRepository().selectByExample(newExample(new ArrayList<>(ids)));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E updateWithBlobs(ID id, DTO dto) {
        return this.updateWithBlobs(id, dto, (dest, source) -> {
        });
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E updateWithBlobs(ID id, Consumer<E> consumer) {
        this.idValid(id);
        Example example = this.newExample();
        example.ids(Collections.singletonList(id));
        E e = this.getRepository().selectOneByExampleWithBLOBs(example);
        if (Objects.nonNull(e)) {
            consumer.accept(e);
            int index = this.getRepository().updateByPrimaryKeyWithBLOBs(e);
            this.postUpdate().accept(e, null);
            this.postUpdate(index != 0).accept(e, null);
            return e;
        }
        throw new BizException(ResponseCode.DATA_NOT_EXISTS.getCode(), "data not exists. ");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E update(E e, Example example) {
        if (Objects.nonNull(e) && Objects.nonNull(example)) {
            this.getRepository().updateByExampleSelective(e, example);
        }
        return e;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E updateWithBlobs(E e) {
        if (Objects.nonNull(e)) {
            int index = this.getRepository().updateByPrimaryKeyWithBLOBs(e);
            this.postUpdate().accept(e, null);
            this.postUpdate(index != 0).accept(e, null);
        }
        return e;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E updateWithBlobs(ID id, DTO dto, BiConsumer<E, DTO> consumer) {
        this.idValid(id);
        this.valid(id, dto);
        Example example = this.newExample();
        example.ids(Collections.singletonList(id));
        E e = this.getRepository().selectOneByExampleWithBLOBs(example);
        if (Objects.nonNull(e)) {
            this.process(e, dto, this.preUpdate().andThen(consumer));
            int index = this.getRepository().updateByPrimaryKeyWithBLOBs(e);
            this.postUpdate().accept(e, dto);
            this.postUpdate(index != 0).accept(e, null);
            return e;
        }
        throw new BizException(ResponseCode.DATA_NOT_EXISTS.getCode(), "data not exists. ");
    }

    @Override
    public List<E> list() {
        return this.getRepository().selectByExample(newExample());
    }

    @Override
    public List<E> list(Sort... sorts) {
        Example example = newExample();
        example.sort(sorts);
        return this.getRepository().selectByExample(example);
    }

    @Override
    public List<E> list(String sort) {
        return this.list(Sorts.newInstance().sorts(sort).toArray(new Sort[]{}));
    }

    @Override
    public PageResponse<E> list(Paging paging) {
        Example example = this.newExample(paging);
        List<E> elements = this.getRepository().selectByExample(example);
        long total = this.getRepository().countByExample(example);
        return PageResponse.apply(total, elements);
    }

    @Override
    public List<E> list(Conditional conditional) {
        Example example = this.doConditional(conditional);
        return this.getRepository().selectByExample(example);
    }

    private Example doConditional(Conditional conditional) {
        Example example = this.newExample();
        Condition condition = example.init(conditional);
        if (Objects.nonNull(condition)) {
            this.postCondition(condition);
            example.condition(condition);
        }
        return example;
    }

    @Override
    public PageResponse<E> list(Conditional conditional, Paging paging) {
        Example example = this.doConditional(conditional);
        return this.list(example, paging);
    }

    @Override
    public List<E> list(Conditionals conditionals) {
        Example example = this.doConditional(conditionals);
        return this.getRepository().selectByExample(example);
    }

    private Example doConditional(Conditionals conditionals) {
        Example example = this.newExample();
        List<Condition> conditions = example.init(conditionals);
        if (Lists.iterable(conditions)) {
            conditions.forEach(this::postCondition);
            Operator operator = conditionals.getOperator();
            example.condition(conditions, Objects.nonNull(operator) ? operator : Operator.AND);
        }
        return example;
    }

    @Override
    public PageResponse<E> list(Conditionals conditionals, Paging paging) {
        Example example = this.doConditional(conditionals);
        return this.list(example, paging);
    }

    @Override
    public List<E> list(Condition condition) {
        Example example = this.newExample();
        example.condition(condition);
        return this.getRepository().selectByExample(example);
    }

    @Override
    public PageResponse<E> list(Condition condition, Paging paging) {
        Example example = this.newExample();
        example.condition(condition);
        return this.list(example, paging);
    }

    @Override
    public List<E> list(List<Condition> conditions, Operator operator) {
        Example example = this.newExample();
        example.condition(conditions, operator);
        return this.getRepository().selectByExample(example);
    }

    @Override
    public PageResponse<E> list(List<Condition> conditions, Operator operator, Paging paging) {
        Example example = this.newExample();
        example.condition(conditions, operator);
        return this.list(example, paging);
    }

    @Override
    public PageResponse<E> list(Example example, Paging paging) {
        example.page(paging);
        List<E> elements = this.getRepository().selectByExample(example);
        long total = this.getRepository().countByExample(example);
        return PageResponse.apply(total, elements);
    }

    @Override
    public Long count() {
        Example example = this.newExample();
        return this.getRepository().countByExample(example);
    }

    @Override
    @SuppressWarnings("ALL")
    public Class<E> getDomainClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), BaseMybatisService.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[1];
    }

    @Override
    @SuppressWarnings("ALL")
    public Class<Example> getExampleClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), BaseMybatisService.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<Example>) genericInterfaces.getActualTypeArguments()[4];
    }
}
