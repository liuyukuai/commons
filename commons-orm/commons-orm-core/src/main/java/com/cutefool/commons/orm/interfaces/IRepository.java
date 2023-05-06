/*
 *
 */
package com.cutefool.commons.orm.interfaces;

import java.util.Collection;
import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 11/10/21 5:38 PM
 */
@SuppressWarnings("unused")
public interface IRepository<E, ID> {

    /**
     * 创建对象
     *
     * @param e e
     * @return id
     */
    E nativeCreate(E e);


    /**
     * 批量创建对象
     *
     * @param es es
     * @return true
     */
    int nativeCreate(List<E> es);

    /**
     * 更新对象
     *
     * @param e 实体数据
     * @return 更新后的对象
     */
    int nativeUpdate(E e);


    /**
     * 通过id删除对象
     *
     * @param id id
     */
    void nativeDelete(ID id);

    /**
     * 通过id删除对象（批量）
     *
     * @param ids ids
     */
    void nativeDelete(List<ID> ids);

    /**
     * 通过id查询对象
     *
     * @param id id
     * @return e
     */
    E nativeById(ID id);

    /**
     * 通过id查询对象（批量）
     *
     * @param ids ids
     * @return list
     */
    List<E> nativeById(Collection<ID> ids);
}
