/*
 *
 */
package com.cutefool.commons.javadoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 类注释对象
 *
 * @author 271007729@qq.com
 * @date 2019-09-06 09:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class ClsComment {

    /**
     * 作者
     */
    private String author;

    /**
     * 类名称
     */
    private String name;

    /**
     * 具体注释
     */
    private String comment;

    /**
     * 方法注释对象
     */
    private List<MethodComment> methodComments;

    /**
     * 属性注释
     */
    private List<FieldComment> fieldComments;
}
