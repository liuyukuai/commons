/*
 *
 */
package com.cutefool.commons.orm.mongo;

import com.cutefool.commons.orm.interfaces.IOperations;
import com.cutefool.commons.orm.interfaces.ISpecificationExecutor;
import com.cutefool.commons.orm.mongo.criteria.Restrictions;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 11/11/21 1:47 PM
 */
@SuppressWarnings("unused")
public interface IMongoOperations<DTO, E, ID, R, Q> extends IOperations<DTO, E, ID, R>, ISpecificationExecutor<E, Q> {

    /**
     * 查询数据
     *
     * @param restrictions restrictions
     * @return 数据
     */
    List<E> listByWhere(Restrictions restrictions);


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
     * @param restrictions restrictions
     * @param paging       paging
     * @return 数据
     */
    PageResponse<E> listByWhere(Restrictions restrictions, Paging paging);
}
