/*
 *
 */
package com.cutefool.commons.expression.variable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 系统变量
 *
 * @author 271007729@qq.com
 * @date 9/8/21 6:36 PM
 */
public enum VariableSys implements Variably {

    /**
     * 当前时间
     */
    CURRENT_TIME("${CURRENT_TIME}", "当前时间");


    private String name;

    private String expression;

    VariableSys(String expression, String name) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public String expression() {
        return expression;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    public static Map<String, Object> sysValues() {
        Map<String, Object> values = new HashMap<>();
        values.put(CURRENT_TIME.name(), LocalDateTime.now());
        return values;
    }

    public static boolean isSelf(String expression) {
        return Stream.of(VariableSys.values()).anyMatch(e -> expression.contains(e.name()));
    }
}
