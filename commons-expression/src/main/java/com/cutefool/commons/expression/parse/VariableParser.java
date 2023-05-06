/*
 *
 */
package com.cutefool.commons.expression.parse;

/**
 * 表达式解析
 *
 * @author 271007729@qq.com
 * @date 9/13/21 12:04 AM
 */
public interface VariableParser<T> {

    /**
     * 解析表达式
     *
     * @param expression parse
     * @param e          e
     * @return object
     */
    T parseVariable(String expression, Boolean isOne);
}
