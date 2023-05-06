/*
 *
 */
package com.cutefool.commons.javadoc;

import lombok.Data;

/**
 * 类注释对象
 *
 * @author 271007729@qq.com
 * @date 2019-09-06 09:52
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class MethodComment {

    /**
     * 方法唯一标识
     */
    private String id;

    /**
     * api ID
     */
    private Long apiId;

    /**
     * 方法名称
     */
    private String name;

    /**
     * 具体注释
     */
    private String comment;

    /**
     * 全注解
     */
    private String rawComment;
}
