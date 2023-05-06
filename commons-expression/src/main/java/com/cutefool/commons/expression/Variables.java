/*
 *
 */
package com.cutefool.commons.expression;

import com.cutefool.commons.expression.parse.VariableDurationParser;
import com.cutefool.commons.core.DurationLibs;
import com.cutefool.commons.core.cycle.Cycle;
import com.cutefool.commons.core.cycle.Cycles;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.expression.parse.VariableDurationAfterParser;
import com.cutefool.commons.expression.parse.VariableDurationBeforeParser;
import com.cutefool.commons.expression.parse.VariableSysParser;
import com.cutefool.commons.expression.variable.VariableDurable;
import com.cutefool.commons.expression.variable.VariableDurableAfter;
import com.cutefool.commons.expression.variable.VariableDurableBefore;
import com.cutefool.commons.expression.variable.VariableSys;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 表达式解析
 *
 * @author 271007729@qq.com
 * @date 9/8/21 6:36 PM
 */
public class Variables {

    public static Object parseExpression(String expression) {

        if (VariableDurable.isSelf(expression)) {
            expression = expression.replace("+", "");
            return VariableDurationParser.newInstance().parseExpression(expression, null);
        }

        if (VariableDurableAfter.isSelf(expression)) {
            return VariableDurationAfterParser.newInstance().parseExpression(expression, null);
        }

        if (VariableDurableBefore.isSelf(expression)) {
            return VariableDurationBeforeParser.newInstance().parseExpression(expression, null);
        }

        if (VariableSys.isSelf(expression)) {
            return VariableSysParser.newInstance().parseExpression(expression, null);
        }
        return expression;
    }

    public static Object parseVariable(String variable, Boolean isOne) {
        if (VariableSys.isSelf(variable)) {
            variable = variable.replace("+", "");
            return VariableSysParser.newInstance().parseVariable(variable, isOne);
        }

        if (VariableDurable.isSelf(variable)) {
            return VariableDurationParser.newInstance().parseVariable(variable, isOne);
        }

        if (VariableDurableBefore.isSelf(variable)) {
            return VariableDurationBeforeParser.newInstance().parseVariable(variable, isOne);
        }

        if (VariableDurableAfter.isSelf(variable)) {
            return VariableDurationAfterParser.newInstance().parseVariable(variable, isOne);
        }

        return variable;
    }

    public static DurationLibs<?, ?> parseBetween(String variable, Cycle cycle, Boolean isOne) {
        // 如果不是单值，暂时不考虑周期
        if (Objects.isNull(isOne) || !isOne) {
            return (DurationLibs) parseExpression(variable);
        }
        // 目前只支持时间
        Object value = parseVariable(variable, true);
        if (value instanceof LocalDateTime) {
            LocalDateTime time = (LocalDateTime) value;
            return Cycles.between(cycle, time);
        }

        if (value instanceof DurationLibs) {
            return (DurationLibs) value;
        }

        throw new ExpressionException(ResponseCode.EXPRESSION_ERROR);

    }

    public static void main(String[] args) {

        System.out.println(Variables.parseBetween("AFTER_HOUR+1", Cycle.MINUTES, true));
    }
}
