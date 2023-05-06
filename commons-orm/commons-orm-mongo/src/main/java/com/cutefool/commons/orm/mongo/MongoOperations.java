/*
 *
 */
package com.cutefool.commons.orm.mongo;

import com.cutefool.commons.orm.PagingUtils;
import com.cutefool.commons.orm.mongo.criteria.Restrictions;
import com.cutefool.commons.orm.support.BasicOperations;
import lombok.Getter;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.Sorts;
import com.cutefool.commons.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 271007729@qq.com
 * @date 11/10/21 4:53 PM
 */
@SuppressWarnings("unused")
public class MongoOperations<DTO, E, ID, R extends MongoRepository<E, ID>, Q> extends BasicOperations<DTO, E, ID, R> implements IMongoOperations<DTO, E, ID, R, Q> {


    @Getter
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<E> list() {
        return this.getRepository().findAll();
    }

    @Override
    public List<E> list(com.cutefool.commons.core.page.Sort... sorts) {
        return this.getRepository().findAll(this.toSorts(sorts));
    }

    @Override
    public PageResponse<E> list(Paging paging) {
        Page<E> page = this.getRepository().findAll(this.toPage(PagingUtils.of(paging)));
        return PageResponse.apply(page.getTotalElements(), page.getContent());
    }

    @Override
    public Long count() {
        return this.getRepository().count();
    }

    @Override
    public E nativeCreate(E e) {
        this.getRepository().save(e);
        return e;
    }

    @Override
    public int nativeCreate(List<E> es) {
        Lists.empty(es).forEach(e -> this.getRepository().insert(e));
        return Lists.empty(es).size();
    }

    @Override
    public int nativeUpdate(E e) {
        this.getRepository().save(e);
        return 1;
    }

    @Override
    public void nativeDelete(ID id) {
        this.getRepository().deleteById(id);
    }

    @Override
    public void nativeDelete(List<ID> ids) {
        Iterable<E> allById = this.getRepository().findAllById(ids);
        this.getRepository().deleteAll(allById);
    }

    @Override
    public E nativeById(ID id) {
        return this.getRepository().findById(id).orElse(null);
    }

    @Override
    public List<E> nativeById(Collection<ID> ids) {
        return (List<E>) this.getRepository().findAllById(ids);
    }

    @Override
    @SuppressWarnings("ALL")
    public Class<E> getDomainClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), MongoOperations.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[1];
    }

    @Override
    public Long count(Q q) {
        Query query = Restrictions.of().where(q).get();
        return this.mongoTemplate.count(query, this.getDomainClass());
    }

    @Override
    public List<E> listByWhere(Q q) {
        Query query = Restrictions.of().where(q).get();
        return this.mongoTemplate.find(query, this.getDomainClass());
    }

    @Override
    public List<E> listByWhere(Q q, com.cutefool.commons.core.page.Sort... sorts) {
        Query query = Restrictions.of().where(q).get();
        return this.mongoTemplate.find(query.with(this.toSorts(sorts)), this.getDomainClass());
    }

    @Override
    public List<E> listByWhere(Q q, Collection<com.cutefool.commons.core.page.Sort> sorts) {
        Query query = Restrictions.of().where(q).get();
        return this.mongoTemplate.find(query.with(this.toSorts(sorts)), this.getDomainClass());
    }

    @Override
    public List<E> listByWhere(Q q, String sort) {
        return this.listByWhere(q, Sorts.newInstance().sorts(sort).toArray(new com.cutefool.commons.core.page.Sort[]{}));
    }

    @Override
    public PageResponse<E> listByWhere(Q q, Paging paging) {
        return this.listByWhere(Restrictions.of().where(q), paging);
    }

    @Override
    public List<E> listByWithoutCnt(Paging paging) {
        com.cutefool.commons.orm.PageRequest pageRequest = PagingUtils.of(paging);
        Query query = new Query().limit(pageRequest.getSize()).skip(pageRequest.getPage() * pageRequest.getSize()).with(this.toSorts(pageRequest.getSorts()));
        return this.mongoTemplate.find(query, this.getDomainClass());
    }

    @Override
    public List<E> listByWithoutCnt(Q q, Paging paging) {
        Query query = Restrictions.of().where(q).get();
        com.cutefool.commons.orm.PageRequest pageRequest = PagingUtils.of(paging);
        query = query.limit(pageRequest.getSize()).skip(pageRequest.getPage() * pageRequest.getSize()).with(this.toSorts(pageRequest.getSorts()));
        return this.mongoTemplate.find(query, this.getDomainClass());
    }

    @Override
    public List<E> listByWhere(Restrictions restrictions) {
        return this.mongoTemplate.find(restrictions.get(), this.getDomainClass());
    }

    @Override
    public Long count(Restrictions restrictions) {
        return this.mongoTemplate.count(restrictions.get(), this.getDomainClass());
    }

    @Override
    public PageResponse<E> listByWhere(Restrictions restrictions, Paging paging) {
        Query query = restrictions.get();
        Long count = this.count(restrictions);
        com.cutefool.commons.orm.PageRequest pageRequest = PagingUtils.of(paging);
        query = query.limit(pageRequest.getSize()).skip(pageRequest.getPage() * pageRequest.getSize()).with(this.toSorts(pageRequest.getSorts()));
        List<E> es = this.mongoTemplate.find(query, this.getDomainClass());
        return PageResponse.apply(count, es);
    }

    private PageRequest toPage(com.cutefool.commons.orm.PageRequest request) {
        List<com.cutefool.commons.core.page.Sort> sorts = request.getSorts();
        if (Lists.isEmpty(sorts)) {
            return PageRequest.of(request.getPage(), request.getSize(), Sort.unsorted());

        }
        List<Sort.Order> orders = sorts.stream().map(sort -> new Sort.Order(Sort.Direction.fromString(sort.getDirection()), sort.getName())).collect(Collectors.toList());
        return PageRequest.of(request.getPage(), request.getSize(), Sort.by(orders));
    }

    private Sort toSorts(com.cutefool.commons.core.page.Sort... sorts) {
        if (Lists.isEmpty(sorts)) {
            return Sort.unsorted();

        }
        List<Sort.Order> orders = Stream.of(sorts).map(sort -> new Sort.Order(Sort.Direction.fromString(sort.getDirection()), sort.getName())).collect(Collectors.toList());
        return Sort.by(orders);
    }

    private Sort toSorts(Collection<com.cutefool.commons.core.page.Sort> sorts) {
        if (Lists.isEmpty(sorts)) {
            return Sort.unsorted();

        }
        List<Sort.Order> orders = sorts.stream().map(sort -> new Sort.Order(Sort.Direction.fromString(sort.getDirection()), sort.getName())).collect(Collectors.toList());
        return Sort.by(orders);
    }
}
