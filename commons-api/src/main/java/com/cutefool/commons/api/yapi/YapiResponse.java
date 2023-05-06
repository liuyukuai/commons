/*
 *
 */
package com.cutefool.commons.api.yapi;

import lombok.Data;

/**
 * @author 271007729@qq.com
 * @date 2019/9/25 5:05 PM
 */
@Data
@SuppressWarnings("WeakerAccess")
public class YapiResponse<T> {

    private int errcode;

    /**
     * 错误消息
     */
    private String errmsg;

    /**
     * 数据
     */
    private T data;
}
