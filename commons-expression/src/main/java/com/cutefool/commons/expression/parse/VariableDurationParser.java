/*
 *  
 */
package com.cutefool.commons.expression.parse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.cutefool.commons.core.DurationLibs;
import com.cutefool.commons.expression.variable.VariableDurable;

import java.time.LocalDateTime;

/**
 * 时间区间表达式解析
 *
 * @author 271007729@qq.com
 * @date 9/13/21 12:04 AM
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VariableDurationParser implements ExpressionParser<DurationLibs<LocalDateTime, String>, Object>, VariableParser<DurationLibs<LocalDateTime, String>> {

    private static VariableDurationParser instance = new VariableDurationParser();

    public static VariableDurationParser newInstance() {
        return instance;
    }

    @Override
    public DurationLibs<LocalDateTime, String> parseExpression(String expression, Object object) {
        VariableDurable beforeDurable = VariableDurable.valueOf(expression);
        return beforeDurable.duration();
    }

    @Override
    public DurationLibs<LocalDateTime, String> parseVariable(String variable, Boolean isOne) {
        return this.parseExpression(variable, null);
    }
}
