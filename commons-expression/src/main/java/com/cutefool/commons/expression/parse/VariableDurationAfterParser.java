/*
 *
 */
package com.cutefool.commons.expression.parse;

import com.cutefool.commons.core.DurationLibs;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.expression.Variables;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.cutefool.commons.expression.variable.VariableDurableAfter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 时间区间表达式解析
 *
 * @author 271007729@qq.com
 * @date 9/13/21 12:04 AM
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VariableDurationAfterParser implements ExpressionParser<DurationLibs<LocalDateTime, String>, Object>, VariableParser<LocalDateTime> {

    private static VariableDurationAfterParser instance = new VariableDurationAfterParser();

    public static VariableDurationAfterParser newInstance() {
        return instance;
    }

    @Override
    public DurationLibs<LocalDateTime, String> parseExpression(String expression, Object object) {
        // $AFTER_HOUR$+1，$AFTER_HOUR$+1 逗号隔开 时间统一为0:0:0
        List<String> values = Strings.split(expression);
        DurationLibs<LocalDateTime, String> libs = new DurationLibs<>();

        if (Lists.iterable(values)) {
            int size = values.size();
            String s = values.get(0);
            libs.setBefore((LocalDateTime) Variables.parseVariable(values.get(0), size == 1));
            if (size == 2) {
                libs.setAfter((LocalDateTime) Variables.parseVariable(values.get(1), false));
            }
        }
        return libs;
    }

    @Override
    public LocalDateTime parseVariable(String variable, Boolean isOne) {
        // 开始时间
        List<String> variables = Strings.split(variable, "+");
        VariableDurableAfter after = VariableDurableAfter.valueOf(variables.get(0).trim());

        // 如果是等值查询
        if (Objects.nonNull(isOne) && isOne) {
            // 如果只有变量
            if (variables.size() == 1) {
                return after.now();
            }
            if (variables.size() == 2) {
                return after.now(-Long.parseLong(variables.get(1)));
            }
        }

        // 如果只有变量
        if (variables.size() == 1) {
            return after.before();
        }
        if (variables.size() == 2) {
            return after.before(Long.valueOf(variables.get(1)));
        }
        throw new RuntimeException();
    }
}
