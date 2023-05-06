/*
 *
 */
package com.cutefool.commons.ci.core;

import lombok.Getter;

/**
 * CiContext
 *
 * @author 271007729@qq.com
 * @date 7/11/21 4:52 PM
 */
@Getter
@SuppressWarnings({"unused", "unchecked"})
public class CiDeployContext<T extends CiDeployContext<T>> extends CiShellContext<T> {

    /**
     * 代码库id
     */
    private Long codeId;

    public T codeId(Long codeId) {
        this.codeId = codeId;
        return (T) this;
    }
}
