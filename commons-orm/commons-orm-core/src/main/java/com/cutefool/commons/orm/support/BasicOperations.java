package com.cutefool.commons.orm.support;

import com.cutefool.commons.core.page.Sort;
import com.cutefool.commons.core.page.Sorts;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.ProcessUtils;
import com.cutefool.commons.orm.interfaces.IOperations;
import org.springframework.beans.factory.annotation.Autowired;
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
public abstract class BasicOperations<DTO, E, ID, R> implements IOperations<DTO, E, ID, R> {


    @Autowired
    @SuppressWarnings("ALL")
    private R repository;

    @Override
    public R getRepository() {
        return this.repository;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E create(DTO dto) {
        return this.create(dto, (dest, source) -> {
        });
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean create(List<DTO> dtoList) {
        return this.create(dtoList, (d, s) -> {
        });
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean create(List<DTO> dtoList, BiConsumer<E, DTO> consumer) {
        if (Lists.iterable(dtoList)) {
            BiConsumer<E, DTO> biConsumer = this.preCreate();
            if (Objects.nonNull(consumer)) {
                biConsumer = biConsumer.andThen(consumer);
            }
            List<E> es = ProcessUtils.processList(this.getDomainClass(), dtoList, biConsumer);
            int i = this.nativeCreate(es);
            for (int j = 0; j < es.size(); j++) {
                this.postCreate().accept(es.get(j), dtoList.get(j));
            }
            return i == dtoList.size();
        }
        return Boolean.FALSE;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E create(DTO dto, BiConsumer<E, DTO> consumer) {
        this.valid(dto);
        E process = process(dto, this.preCreate().andThen(consumer));
        this.nativeCreate(process);
        this.postCreate().accept(process, dto);
        return process;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E update(ID id, DTO dto) {
        return this.update(id, dto, (dest, source) -> {
        });
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E update(ID id, Consumer<E> consumer) {
        this.idValid(id);
        E e = this.nativeById(id);
        if (Objects.nonNull(e)) {
            consumer.accept(e);
            int index = this.nativeUpdate(e);
            this.postUpdate().accept(e, null);
            this.postUpdate(index != 0).accept(e, null);
        }
        return e;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E update(E e) {
        if (Objects.nonNull(e)) {
            int index = this.nativeUpdate(e);
            this.postUpdate().accept(e, null);
            this.postUpdate(index != 0).accept(e, null);
        }
        return e;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E update(ID id, DTO dto, BiConsumer<E, DTO> consumer) {
        this.idValid(id);
        this.valid(id, dto);
        E e = this.nativeById(id);
        if (Objects.nonNull(e)) {
            consumer = Objects.nonNull(consumer) ? this.preUpdate().andThen(consumer) : this.preUpdate();
            this.process(e, dto, consumer);
            int index = this.nativeUpdate(e);
            this.postUpdate().accept(e, dto);
            this.postUpdate(index != 0).accept(e, null);
        }
        return e;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(ID id) {
        this.idValid(id);
        this.nativeDelete(id);
        this.postDelete().accept(Collections.singletonList(id));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(List<ID> ids) {
        if (Lists.iterable(ids)) {
            this.nativeDelete(ids);
            this.postDelete().accept(ids);
        }
    }

    @Override
    public Optional<E> getById(ID id) {
        this.idValid(id);
        E e = this.nativeById(id);
        return Optional.ofNullable(e);
    }

    @Override
    public List<E> getById(Collection<ID> ids) {
        if (Lists.iterable(ids)) {
            return this.nativeById(ids);
        }
        return Collections.emptyList();
    }

    @Override
    public List<E> list(String sort) {
        return this.list(Sorts.newInstance().sorts(sort).toArray(new Sort[]{}));
    }

    @Override
    @SuppressWarnings("ALL")
    public Class<E> getDomainClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), BasicOperations.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[1];
    }
}
