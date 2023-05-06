package com.cutefool.commons.orm.jpa;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.Sort;
import com.cutefool.commons.core.page.Sorts;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.orm.jpa.criteria.Restrictions;
import com.cutefool.commons.orm.jpa.utils.JpaUtils;
import com.cutefool.commons.orm.support.BasicOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author : liuyk
 */

@SuppressWarnings({"all"})
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class BasicJpaOperations<DTO, E, ID extends Serializable, JPA extends JpaRepository<E, ID> & JpaSpecificationExecutor> extends BasicOperations<DTO, E, ID, JPA> implements IBasicJpaOperations<DTO, E, ID, JPA> {

    @Autowired
    private JPA repository;

    @Override
    public JPA getRepository() {
        return repository;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public E nativeCreate(E e) {
        return this.repository.saveAndFlush(e);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int nativeCreate(List<E> es) {
        List<E> entities = this.repository.saveAll(es);
        return Lists.empty(entities).size();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int nativeUpdate(E e) {
        this.repository.saveAndFlush(e);
        return Objects.nonNull(e) ? 1 : 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void nativeDelete(ID id) {
        this.repository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void nativeDelete(List<ID> ids) {
        this.repository.deleteAllById(ids);
    }

    @Override
    public E nativeById(ID id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public List<E> nativeById(Collection<ID> ids) {
        if (Lists.iterable(ids)) {
            return this.repository.findAllById(ids);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<E> list() {
        return this.repository.findAll();
    }

    @Override
    public List<E> list(Sort... sorts) {
        return this.repository.findAll(JpaUtils.of(sorts));
    }

    @Override
    public PageResponse<E> list(Paging paging) {
        Page<E> page = this.repository.findAll(JpaUtils.of(paging));
        return JpaUtils.of(page);
    }

    @Override
    public Long count() {
        return this.repository.count();
    }

    @Override
    public Long count(Object query) {
        return this.repository.count(Restrictions.of().where(query).get());
    }

    @Override
    public List<E> listByWhere(Object query) {
        return this.repository.findAll(Restrictions.of().where(query).get());
    }

    @Override
    public List<E> listByWhere(Object query, Sort... sorts) {
        return this.repository.findAll(Restrictions.of().where(query).get(), JpaUtils.of(sorts));
    }

    @Override
    public List<E> listByWhere(Object query, Collection<Sort> sorts) {
        return this.repository.findAll(Restrictions.of().where(query).get(), JpaUtils.of(sorts));
    }

    @Override
    public List<E> listByWhere(Object query, String sort) {
        return this.listByWhere(query, Sorts.newInstance().sorts(sort).toArray(new Sort[]{}));
    }

    @Override
    public PageResponse<E> listByWhere(Object query, Paging paging) {
        Page<E> page = this.repository.findAll(Restrictions.of().where(query).get(), JpaUtils.of(paging));
        return JpaUtils.of(page);
    }

    @Override
    public List<E> listByWithoutCnt(Paging paging) {
        throw new BizException("not support.");
    }

    @Override
    public List<E> listByWithoutCnt(Object o, Paging paging) {
        throw new BizException("not support.");
    }

    @Override
    public Class<E> getDomainClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), BasicJpaOperations.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[1];
    }
}
