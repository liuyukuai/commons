/*
 *  
 */
package com.cutefool.commons.orm.jpa;

import com.cutefool.commons.orm.interfaces.IOperations;
import com.cutefool.commons.orm.interfaces.ISpecificationExecutor;

/**
 * @author 271007729@qq.com
 * @date 2022/7/27 5:47 PM
 */
public interface IBasicJpaOperations<DTO, E, ID, R> extends IOperations<DTO, E, ID, R>, ISpecificationExecutor<E, Object> {
}
