/*
 *  
 */
package com.cutefool.commons.orm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.cutefool.commons.core.Operator;

/**
 * 转换对象
 *
 * @author 271007729@qq.com
 * @date 11/2/21 5:06 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class Transformation {
    /**
     * 属性值
     */
    private String[] field;

    /**
     * 具体的值
     */
    private Object value;

    /**
     * 操作
     */
    private Operator operator;

    /**
     * 多条件之间的关系
     */
    private Operator relation;

    /**
     * 是否忽略空值
     */
    private boolean ignoreEmpty;
}
