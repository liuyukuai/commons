/*
 *
 */
package com.cutefool.commons.orm.interfaces;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.expression.libs.Conditional;
import com.cutefool.commons.expression.libs.Conditionals;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 11/10/21 5:38 PM
 */
public interface IConditionOperations<E> {

    /**
     * 通过条件查询
     *
     * @param conditional conditional
     * @return list
     */
    default List<E> list(Conditional conditional) {
        throw new BizException("please override this method.");
    }


    ;

    /**
     * 通过条件查询
     *
     * @param conditional conditional
     * @param paging      paging
     * @return list
     */
    default PageResponse<E> list(Conditional conditional, Paging paging) {
        throw new BizException("please override this method.");
    }

    ;

    /**
     * 通过条件查询
     *
     * @param conditionals conditionals
     * @return list
     */
    default List<E> list(Conditionals conditionals) {
        throw new BizException("please override this method.");
    }

    ;

    /**
     * 通过条件查询
     *
     * @param conditionals conditionals
     * @param paging       paging
     * @return list
     */
    default PageResponse<E> list(Conditionals conditionals, Paging paging) {
        throw new BizException("please override this method.");
    }


    /**
     * 通过条件查询
     *
     * @param conditional conditional
     * @return long
     */
    default Long count(Conditional conditional) {
        throw new BizException("please override this method.");
    }

    /**
     * 通过条件查询
     *
     * @param conditionals conditionals
     * @return long
     */
    default Long count(Conditionals conditionals) {
        throw new BizException("please override this method.");
    }
}
