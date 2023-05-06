package com.cutefool.commons.mybatis.dsl;

import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.expression.Conditioning;
import com.cutefool.commons.orm.BasicsDomain;
import com.cutefool.commons.orm.interfaces.IdWorkerOperations;
import com.cutefool.commons.snowflake.IdWorker;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author liuyk
 */
@SuppressWarnings({"unused"})
public abstract class MybatisIdWorkerTemplate<D, E extends BasicsDomain, ID extends Long, R extends MybatisMapper<E, ID>, Query extends Conditioning> extends MybatisTemplate<D, E, ID, R, Query> implements MybatisOperations<D, E, ID, R, Query>, IdWorkerOperations<D, E, ID, R, Query> {

    @Resource
    private IdWorker idWorker;

    @Override
    public BiConsumer<E, D> preCreate() {
        return super.preCreate().andThen((dest, source) -> {
            dest.setId(idWorker.nextId());
            dest.setCreateTime(LocalDateTime.now());
            dest.setReviseTime(LocalDateTime.now());
        });
    }

    @Override
    public BiConsumer<E, D> preUpdate() {
        return super.preUpdate().andThen((dest, source) -> dest.setReviseTime(LocalDateTime.now()));
    }

    @Override
    @SuppressWarnings("ALL")
    public Class<E> getDomainClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), MybatisIdWorkerTemplate.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[1];
    }

    @Override
    public Boolean validateName(ID id, String name) {
        List<E> list = this.getRepository().select(this.newSelect(name));
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        return Objects.equals(id, list.get(0).getId());
    }


    @Override
    @SuppressWarnings({"unchecked"})
    public Map<ID, Map<String, Object>> namesMultiple(Collection<ID> ids) {
        List<E> users = this.getById(ids);
        return Lists.empty(users).stream().collect(Collectors.toMap(e -> (ID) e.getId(), e -> JsonUtils.toMap(e).orElse(Maps.hashMap())));
    }

}
