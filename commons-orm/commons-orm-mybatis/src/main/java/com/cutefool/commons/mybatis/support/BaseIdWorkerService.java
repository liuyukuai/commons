package com.cutefool.commons.mybatis.support;

import com.cutefool.commons.mybatis.IBaseMapper;
import com.cutefool.commons.mybatis.IExample;
import com.cutefool.commons.mybatis.IdWorkerService;
import com.cutefool.commons.orm.BasicsDomain;
import com.cutefool.commons.snowflake.IdWorker;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author liuyk
 */
@SuppressWarnings({"unused"})
public abstract class BaseIdWorkerService<DTO, E extends BasicsDomain, ID extends Long, Mapper extends IBaseMapper<E, ID, Example>, Example extends IExample<ID>, Query> extends BaseSpecificationMybatisService<DTO, E, ID, Mapper, Example, Query> implements IdWorkerService<DTO, E, ID, Mapper, Example, Query> {

    @Resource
    private IdWorker idWorker;

    @Override
    public BiConsumer<E, DTO> preCreate() {
        return super.preCreate().andThen((dest, source) -> {
            dest.setId(idWorker.nextId());
            dest.setCreateTime(LocalDateTime.now());
        });
    }

    @Override
    public BiConsumer<E, DTO> preUpdate() {
        return super.preUpdate().andThen((dest, source) -> dest.setReviseTime(LocalDateTime.now()));
    }

    @Override
    @SuppressWarnings("ALL")
    public Boolean validateName(ID id, String name) {
        Example example = this.newExample(id, name);
        List<E> list = this.getRepository().selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        return Objects.equals(id, list.get(0).getId());
    }


    @Override
    @SuppressWarnings("ALL")
    public Class<E> getDomainClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), BaseIdWorkerService.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[1];
    }


    @Override
    @SuppressWarnings("ALL")
    public Class<Example> getExampleClass() {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), BaseIdWorkerService.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<Example>) genericInterfaces.getActualTypeArguments()[4];
    }
}
