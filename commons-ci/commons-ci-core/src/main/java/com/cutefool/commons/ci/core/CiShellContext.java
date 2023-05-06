/*
 *
 */
package com.cutefool.commons.ci.core;

import lombok.Getter;

/**
 * @author 271007729@qq.com
 * @date 8/14/21 4:26 PM
 */
@Getter
@SuppressWarnings({"unused", "unchecked"})
public class CiShellContext<T extends CiShellContext<T>> extends CiContext<T> {

    /**
     * 自定义执行命令（shell生效）
     */
    private String execute;

    public T execute(String execute) {
        this.execute = execute;
        return (T) this;
    }
}
