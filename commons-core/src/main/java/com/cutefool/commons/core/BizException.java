/*
 *
 *
 */
package com.cutefool.commons.core;

import lombok.Getter;
import com.cutefool.commons.core.page.ResponseCode;

/**
 * 业务异常基类
 *
 * @author 271007729@qq.com
 * @date 2019-07-03 09:27
 */
@SuppressWarnings({"unused"})
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 6034307555798502244L;

    /**
     * biz code
     */
    @Getter
    private String code;


    public BizException() {

    }


    public BizException(Throwable e) {
        super(e.getMessage(), e);
    }


    public BizException(String code) {
        this(code, ResponseCode.SYSTEM_ERROR.getMessage());
    }


    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ResponseCode responseCode, Object... variables) {
        super(String.format(responseCode.getMessage(), variables));
        this.code = responseCode.getCode();
    }


    public BizException(ResponseCode responseCode) {
        this(responseCode.getCode(), responseCode.getMessage());
    }


    public BizException(Throwable e, String code, String message) {
        super(message, e);
        this.code = code;
    }


    public BizException(Throwable e, ResponseCode responseCode) {
        this(e, responseCode.getCode(), e.getMessage());

    }

}
