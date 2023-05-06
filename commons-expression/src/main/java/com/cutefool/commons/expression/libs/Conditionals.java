/*
 *
 */
package com.cutefool.commons.expression.libs;

import lombok.Data;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.cycle.Cycle;

import java.util.List;

/**
 * 条件
 *
 * @author 271007729@qq.com
 * @date 9/6/21 6:08 PM
 */
@Data
@SuppressWarnings("unused")
public class Conditionals {

    private Operator operator;

    private Cycle cycle;

    private List<Conditional> conditionals;

    public Conditionals() {
    }

    public Conditionals(Operator operator, List<Conditional> conditionals) {
        this.operator = operator;
        this.conditionals = conditionals;
    }

    public Conditionals(Operator operator, Cycle cycle, List<Conditional> conditionals) {
        this.operator = operator;
        this.cycle = cycle;
        this.conditionals = conditionals;
    }

    public static Conditionals of(List<Conditional> conditionals) {
        return new Conditionals(Operator.AND, conditionals);
    }

    public static Conditionals of(Operator operator, List<Conditional> conditionals) {
        return new Conditionals(operator, conditionals);
    }
}
