package com.cutefool.commons.mybatis;

import com.cutefool.commons.orm.interfaces.ISpecificationExecutor;

/**
 * @author 271007729@qq.com
 * @date 2019-07-08 11:32
 */

@SuppressWarnings("unused")
public interface IBaseSpecificationService<DTO, E, ID, Mapper extends IBaseMapper<E, ID, Example>, Example extends IExample<ID>, Query> extends IBaseService<DTO, E, ID, Mapper, Example>, ISpecificationExecutor<E, Query>, IExampleSpecificationGetter<ID, Example, Query> {

}
