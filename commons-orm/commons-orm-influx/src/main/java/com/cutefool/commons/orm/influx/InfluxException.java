/*
 *
 */
package com.cutefool.commons.orm.influx;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.ResponseCode;

/**
 * @author 271007729@qq.com
 * @date 11/2/21 4:02 PM
 */
public class InfluxException extends BizException {

    public InfluxException(String message) {
        super(ResponseCode.INFLUX_ERROR, message);
    }
}
