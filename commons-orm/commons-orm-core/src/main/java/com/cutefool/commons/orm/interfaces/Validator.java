package com.cutefool.commons.orm.interfaces;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.ResponseCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author 271007729@qq.com
 * @date 2019-07-10 01:33
 */
@SuppressWarnings("unused")
public interface Validator<E, ID> {
    /**
     * 校验方法
     *
     * @param e 校验的元素
     */
    default void valid(E e) {

    }

    /**
     * 校验方法
     *
     * @param id id
     * @param e  校验的对象
     */
    default void valid(ID id, E e) {

    }

    /**
     * 校验id方法
     *
     * @param id id
     */
    default void idValid(ID id) {
        if (Objects.isNull(id)) {
            throw new BizException(ResponseCode.API_PARAM_ERROR.getCode(), "id不能为空");
        }
        boolean validate = id instanceof String && StringUtils.isBlank((String) id);
        if (validate) {
            throw new BizException(ResponseCode.API_PARAM_ERROR.getCode(), "id不能为空");
        }
    }
}
