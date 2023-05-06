/*
 *
 */
package com.cutefool.commons.core;

import org.apache.commons.lang3.StringUtils;

/**
 * 操作条件
 *
 * @author 271007729@qq.com
 * @date 9/6/21 6:12 PM
 */
public enum Operator {
    /**
     * 等于
     */
    EQ,
    /**
     * 不等于
     */
    NE,
    /**
     * 模糊
     */
    LIKE,
    /**
     * 模糊
     */
    LEFT_LIKE,
    /**
     * 模糊
     */
    RIGHT_LIKE,
    /**
     * 模糊
     */
    NOT_LIKE,
    /**
     * 大于
     */
    GT,
    /**
     * 小于
     */
    LT,
    /**
     * 大于等于
     */
    GTE,
    /**
     * 小于等于
     */
    LTE,
    /**
     * 并且
     */
    AND,
    /**
     * 或者
     */
    OR,

    /**
     * 在之间
     */
    BETWEEN,
    /**
     * in
     */
    IN,
    /**
     * not in
     */
    NOT_IN,

    /**
     * find_in_set
     */
    FIND_IN_SET,

    /**
     * 全文搜索（布尔查找）
     */
    MATCH_BOOL,

    /**
     * 全文搜索（自然语言搜索方式）
     */
    MATCH_NATURAL;


    public static Operator apply(String operator) {
        if (StringUtils.isBlank(operator)) {
            return Operator.EQ;
        }
        try {
            return Operator.valueOf(operator.toUpperCase());
        } catch (Exception e) {
            return Operator.EQ;
        }
    }

    public static boolean isOperator(String operator) {
        if (StringUtils.isBlank(operator)) {
            return false;
        }
        try {
            Operator.valueOf(operator.toUpperCase());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
