/*
 *
 */
package com.cutefool.commons.expression.parse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.cutefool.commons.expression.variable.VariableSys;

import java.util.Map;

/**
 * 系统表达式解析
 *
 * @author 271007729@qq.com
 * @date 9/13/21 12:04 AM
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VariableSysParser implements ExpressionParser<Object, Object>, VariableParser<Object> {

    private static VariableSysParser instance = new VariableSysParser();

    public static VariableSysParser newInstance() {
        return instance;
    }

    @Override
    public Object parseExpression(String expression, Object object) {
        Map<String, Object> values = VariableSys.sysValues();
        VariableSys variableSys = VariableSys.valueOf(expression);
        return values.get(variableSys.name());
    }

    @Override
    public Object parseVariable(String expression, Boolean isOne) {
        return this.parseExpression(expression, null);
    }
}
