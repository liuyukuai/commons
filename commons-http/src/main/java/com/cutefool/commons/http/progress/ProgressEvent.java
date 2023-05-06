/*
 *
 */
package com.cutefool.commons.http.progress;

/**
 * @author 271007729@qq.com
 * @date 2021/12/20 11:15 PM
 */
public enum ProgressEvent {
    /**
     * 开始
     */
    START,
    /**
     * 进行中
     */
    RUNNING,

    /**
     * 读取错误
     */
    ERROR,

    /**
     * 完成
     */
    END;
}
