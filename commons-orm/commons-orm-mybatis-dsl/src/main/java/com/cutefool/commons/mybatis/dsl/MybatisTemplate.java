/*
 *  
 */
package com.cutefool.commons.mybatis.dsl;

import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.Sort;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.expression.Conditioning;
import com.cutefool.commons.expression.libs.Conditional;
import com.cutefool.commons.expression.libs.Conditionals;
import com.cutefool.commons.mybatis.dsl.utils.MybatisUtils;
import com.cutefool.commons.orm.PageRequest;
import com.cutefool.commons.orm.rds.cache.context.RdsContext;
import com.cutefool.commons.orm.support.BasicOperations;
import org.mybatis.dynamic.sql.where.WhereApplier;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author 271007729@qq.com
 * @date 2022/9/20 4:15 PM
 */
@SuppressWarnings("unused")
public abstract class MybatisTemplate<DTO, E, ID, R extends MybatisMapper<E, ID>, Query extends Conditioning> extends BasicOperations<DTO, E, ID, R> implements MybatisOperations<DTO, E, ID, R, Query> {

    @Autowired
    private RdsContext rdsContext;

    @Override
    public List<E> list() {
        return this.getRepository().select(this.newSelect());
    }

    @Override
    public List<E> list(Sort... sorts) {
        return this.getRepository().select(this.newSelect(sorts));
    }

    @Override
    public PageResponse<E> list(Paging paging) {
        return this.doPage(MybatisUtils.of(paging, defaultSorts()), this::list);
    }

    @Override
    public Long count() {
        return this.getRepository().count(this.newCount());
    }

    @Override
    public E nativeCreate(E e) {
        Map<String, String> extensibleFields = this.extensibleFields();
        if (Maps.isEmpty(extensibleFields)) {
            this.getRepository().insert(e);
            return e;
        }
        this.getRepository().insert(e, insertDSL -> this.doExtensible(insertDSL, extensibleFields));
        return e;
    }

    @Override
    public int nativeCreate(List<E> es) {
        Map<String, String> extensibleFields = this.extensibleFields();
        if (Maps.isEmpty(extensibleFields)) {
            return this.getRepository().insertMultiple(es);
        }
        return this.getRepository().insertMultiple(es, insertDSL -> this.doExtensible(insertDSL, extensibleFields));
    }

    @Override
    public int nativeUpdate(E e) {
        if (Objects.nonNull(e)) {
            Map<String, String> extensibleFields = this.extensibleFields();
            if (Maps.isEmpty(extensibleFields)) {
                return this.getRepository().updateByPrimaryKey(e);
            }
            return this.getRepository().updateByPrimaryKey(e, insertDSL -> this.doExtensible(e, insertDSL, extensibleFields));
        }
        return 0;
    }

    @Override
    public void nativeDelete(ID i) {
        if (Objects.nonNull(i)) {
            this.getRepository().deleteByPrimaryKey(i);
        }
    }

    @Override
    public void nativeDelete(List<ID> is) {
        if (Lists.iterable(is)) {
            this.getRepository().deleteByPrimaryKey(is);
        }
    }

    @Override
    public E nativeById(ID i) {
        return this.getRepository().selectByPrimaryKey(i).orElse(null);
    }

    @Override
    public List<E> nativeById(Collection<ID> is) {
        return this.getRepository().selectByPrimaryKey(is);
    }

    @Override
    public Long count(Query o) {
        return this.getRepository().count(this.newCount(o));
    }

    @Override
    public List<E> listByWhere(Query o) {
        return this.getRepository().select(this.newSelect(o, this.findSorts(o)), o.getSelects());
    }

    @Override
    public List<E> listByWhere(Query o, Sort... sorts) {
        return this.getRepository().select(this.newSelect(o, sorts), o.getSelects());
    }

    @Override
    public List<E> listByWhere(Query o, Collection<Sort> sorts) {
        return this.getRepository().select(this.newSelect(o, sorts), o.getSelects());
    }

    @Override
    public List<E> listByWhere(Query o, String sort) {
        return this.getRepository().select(this.newSelect(o, sort), o.getSelects());
    }

    @Override
    public PageResponse<E> listByWhere(Query o, Paging paging) {
        PageRequest of = MybatisUtils.of(paging, defaultSorts());
        return this.doPage(of, () -> this.getRepository().select(this.newSelect(o, of.getSorts()), o.getSelects()));
    }

    @Override
    public PageResponse<E> listByWhere(WhereApplier whereApplier, Paging paging) {
        PageRequest of = MybatisUtils.of(paging, defaultSorts());
        return this.doPage(of, () -> this.listBy(whereApplier));
    }

    @Override
    @SuppressWarnings("ALL")
    public Class<E> getDomainClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), MybatisTemplate.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[1];
    }

    @Override
    public List<E> list(Conditional conditional) {
        return this.getRepository().select(this.newSelect(conditional, defaultSorts()));
    }

    @Override
    public PageResponse<E> list(Conditional conditional, Paging paging) {
        return this.doPage(MybatisUtils.of(paging, defaultSorts()), () -> this.getRepository().select(this.newSelect(conditional)));
    }

    @Override
    public List<E> list(Conditionals conditionals) {
        return this.getRepository().select(this.newSelect(conditionals, defaultSorts()));
    }

    @Override
    public PageResponse<E> list(Conditionals conditionals, Paging paging) {
        return this.doPage(MybatisUtils.of(paging, defaultSorts()), () -> this.getRepository().select(this.newSelect(conditionals)));
    }

    @Override
    public Long count(Conditional conditional) {
        return this.getRepository().count(this.newCount(conditional));
    }

    @Override
    public Long count(Conditionals conditionals) {
        return this.getRepository().count(this.newCount(conditionals));
    }

    @Override
    public Optional<E> getByName(String name) {
        return this.getRepository().selectOne(this.newSelect(name));
    }


    @Override
    public List<E> listByWithoutCnt(Paging paging) {
        PageRequest of = MybatisUtils.of(paging, defaultSorts());
        SelectCompleter selectCompleter = this.newSelect((dsl, whereDSL) -> {
            MybatisUtils.apply(dsl, of);
            this.newSelect(dsl, of.getSorts());
        });
        return this.getRepository().select(selectCompleter);
    }

    @Override
    public List<E> listByWithoutCnt(Query o, Paging paging) {
        PageRequest of = MybatisUtils.of(paging, defaultSorts());
        SelectCompleter selectCompleter = this.newSelect(o, (dsl, where) -> {
            MybatisUtils.apply(dsl, of);
            this.newSelect(dsl, of.getSorts());
        });
        return this.getRepository().select(selectCompleter);
    }

    @Override
    public RdsContext rdsContext() {
        return rdsContext;
    }
}
