/*
 *
 */
package com.cutefool.commons.expression.parse;

import com.cutefool.commons.expression.Variables;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.cutefool.commons.core.DurationLibs;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.expression.variable.VariableDurableBefore;

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
public class VariableDurationBeforeParser implements ExpressionParser<DurationLibs<LocalDateTime, String>, Object>, VariableParser<LocalDateTime> {

    private static VariableDurationBeforeParser instance = new VariableDurationBeforeParser();

    public static VariableDurationBeforeParser newInstance() {
        return instance;
    }

    @Override
    public DurationLibs<LocalDateTime, String> parseExpression(String expression, Object e) {
        // $BEFORE_HOUR$+1，$BEFORE_HOUR$+1 逗号隔开 时间统一为0:0:0
        DurationLibs<LocalDateTime, String> libs = new DurationLibs<>();
        List<String> values = Strings.split(expression);

        if (Lists.iterable(values)) {
            int size = values.size();
            String s = values.get(0);
            // 如果只有一个时间、补充时间范围
            libs.setBefore((LocalDateTime) Variables.parseVariable(s, size == 1));
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
        VariableDurableBefore before = VariableDurableBefore.valueOf(variables.get(0).trim());

        // 如果是等值查询
        if (Objects.nonNull(isOne) && isOne) {
            // 如果只有变量
            if (variables.size() == 1) {
                return before.now();
            }
            if (variables.size() == 2) {
                return before.now(-Long.parseLong(variables.get(1)));
            }
        }

        // 如果只有变量
        if (variables.size() == 1) {
            return before.after();
        }
        if (variables.size() == 2) {
            return before.after(Long.valueOf(variables.get(1)));
        }
        throw new RuntimeException();
    }
}
