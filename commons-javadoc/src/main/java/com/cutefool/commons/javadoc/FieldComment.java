/*
 *
 */
package com.cutefool.commons.javadoc;

import lombok.Data;

/**
 * 属性注释
 *
 * @author 271007729@qq.com
 * @date 2019-09-06 09:52
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class FieldComment {

    /**
     * 属性名称
     */
    private String name;

    /**
     * 具体注释
     */
    private String comment;
}
