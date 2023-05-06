/*
 *
 */
package com.cutefool.commons.orm.influx;

import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.util.ProcessUtils;
import com.cutefool.commons.orm.influx.libs.InfluxLibs;
import com.cutefool.commons.orm.influx.query.InfluxQuery;
import com.cutefool.commons.orm.influx.support.Restrictions;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * influx 操作类
 *
 * @author 271007729@qq.com
 * @date 11/1/21 5:11 PM
 */
@SuppressWarnings("unused")
public interface InfluxOperations<DTO, E extends InfluxLibs, Q extends InfluxQuery> {


    /**
     * 获取条件构造对象
     *
     * @return restrictions
     */
    Restrictions restrictions();


    /**
     * 获取条件构造对象
     *
     * @return restrictions
     */
    String measurement();

    /**
     * 创建数据
     *
     * @param e e
     */
    void create(DTO e);

    /**
     * 创建数据
     *
     * @param e e
     */
    void create(List<DTO> e);


    /**
     * 创建数据
     *
     * @param e        e
     * @param consumer consumer
     */
    void create(DTO e, BiConsumer<E, DTO> consumer);


    /**
     * 创建数据
     *
     * @param e        e
     * @param consumer consumer
     */
    void create(List<DTO> e, BiConsumer<E, DTO> consumer);

    /**
     * 查询数据
     *
     * @param query query
     * @return 数据
     */
    List<E> listByWhere(Q query);

    /**
     * 查询数据
     *
     * @param restrictions restrictions
     * @return 数据
     */
    List<E> list(Restrictions restrictions);


    /**
     * 查询数据
     *
     * @param query query
     * @return 数据
     */
    Long count(Q query);


    /**
     * 查询数据
     *
     * @param restrictions restrictions
     * @return 数据
     */
    Long count(Restrictions restrictions);

    /**
     * 查询数据
     *
     * @param query  query
     * @param paging paging
     * @return 数据
     */
    PageResponse<E> listByWhere(Q query, Paging paging);

    /**
     * 查询数据
     *
     * @param restrictions restrictions
     * @param paging       paging
     * @return 数据
     */
    PageResponse<E> list(Restrictions restrictions, Paging paging);


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
    default Consumer<List<Long>> postDelete() {
        return (dest) -> {
        };
    }

    /**
     * 查询实体类型
     *
     * @return class
     */
    Class<E> getDomainClass();

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
}
