/*
 *  
 */
package com.cutefool.commons.expression.libs;
import com.cutefool.commons.core.Operator;

/**
 * 操作条件
 *
 * @author 271007729@qq.com
 * @date 9/6/21 6:12 PM
 */
public enum Operators {

    /**
     * 等于
     */
    EQ(Operator.EQ, "等于"),
    /**
     * 不等于
     */
    NE(Operator.NE, "不等于"),
    /**
     * 模糊
     */
    LIKE(Operator.LIKE, "包含"),
    /**
     * 模糊
     */
    NOT_LIKE(Operator.NOT_LIKE, "不包含"),
    /**
     * 大于
     */
    GT(Operator.GT, "大于"),
    /**
     * 小于
     */
    LT(Operator.LT, "小于"),
    /**
     * 大于等于
     */
    GTE(Operator.GTE, "大于等于"),
    /**
     * 小于等于
     */
    LTE(Operator.LTE, "小于等于"),
    /**
     * 并且
     */
    AND(Operator.AND, "并"),
    /**
     * 或者
     */
    OR(Operator.OR, "或"),

    /**
     * 在之间
     */
    BETWEEN(Operator.BETWEEN, "在"),
    /**
     * in
     */
    IN(Operator.IN, "IN"),
    /**
     * not in
     */
    NOT_IN(Operator.NOT_IN, "NOT_IN");


    private Operator operator;

    private String name;

    Operators(Operator operator, String name) {
        this.operator = operator;
        this.name = name;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getName() {
        return name;
    }



}
