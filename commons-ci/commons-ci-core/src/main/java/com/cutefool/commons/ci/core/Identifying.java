/*
 *
 */
package com.cutefool.commons.ci.core;

/**
 * @author 271007729@qq.com
 * @date 7/22/21 4:43 PM
 */
public interface Identifying {

    /**
     * 获取项目唯一标识
     *
     * @param context context
     * @return Identifier
     */
    <T extends CiContext<T>> Identifier identifier(T context);
}
