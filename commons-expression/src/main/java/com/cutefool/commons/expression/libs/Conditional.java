/*
 *
 */
package com.cutefool.commons.expression.libs;

import lombok.Data;
import com.cutefool.commons.core.Operator;

/**
 * 条件
 *
 * @author 271007729@qq.com
 * @date 9/6/21 6:08 PM
 */
@Data
public class Conditional {
    /**
     * key
     */
    private String key;

    /**
     * 操作
     */
    private Operator operator;

    /**
     * 值
     */
    private String value;

    /**
     * 类型
     */
    private String type;
}
