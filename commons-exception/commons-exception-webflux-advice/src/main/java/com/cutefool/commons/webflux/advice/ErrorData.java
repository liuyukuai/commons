package com.cutefool.commons.webflux.advice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异常数据对象
 *
 * @author 271007729@qq.com
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class ErrorData {
    /**
     * 请求路径
     */
    private String path;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 具体异常信息
     */
    private String message;

}
