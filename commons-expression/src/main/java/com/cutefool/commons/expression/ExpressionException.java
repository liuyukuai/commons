/*
 *  
 */
package com.cutefool.commons.expression;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.ResponseCode;

/**
 * @author 271007729@qq.com
 * @date 10/8/21 9:42 AM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ExpressionException extends BizException {

    public ExpressionException(Throwable e) {
        super(e);
    }

    public ExpressionException(ResponseCode responseCode) {
        this(responseCode.getCode(), ResponseCode.EXPRESSION_ERROR.getMessage());
    }

    public ExpressionException(String code, String message) {
        super(code, message);
    }

}
