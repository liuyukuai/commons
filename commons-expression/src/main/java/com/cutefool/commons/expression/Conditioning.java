
/*
 *  
 */
package com.cutefool.commons.expression;

import lombok.Data;
import com.cutefool.commons.core.Exclude;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.expression.libs.Conditional;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@SuppressWarnings("unused")
public class Conditioning {

    /**
     * 前端使用自定义条件查询条件
     * 如：?q=id-498711392967196672;name-like-模糊名称;
     * 如：&q=id-498711392967196672;name-like-模糊名称;
     */
    @Exclude
    private String q;

    @Exclude
    private String sort;

    @Exclude
    private String selects;

    public void doSelects(String... selects) {
        if (Lists.iterable(selects)) {
            this.selects = String.join(Strings.COMMA, selects);
        }
    }


    public static List<Conditional> apply(String q) {
        if (StringUtils.isBlank(q)) {
            return Lists.newArrayList();
        }
        // 解析参数
        return Lists.empty(Strings.split(q, Strings.SEMICOLON))
                .stream()
                .map(e -> {
                    // id-1,name-like-5
                    List<String> values = Lists.empty(Strings.split(e, Strings.LINE_THROUGH));
                    // 如果 只有1个-说明条件不规范，跳过
                    if (values.size() < 2) {
                        return null;
                    }
                    // 属性名称
                    String field = values.get(0);

                    if (StringUtils.isBlank(field)) {
                        return null;
                    }

                    // 如果按照规范写的
                    if (values.size() >= 3) {
                        // 具体的值
                        String v = values.get(2);
                        // 条件
                        String operator = values.get(1);
                        // 如果值为空，默认忽略
                        if (StringUtils.isBlank(v)) {
                            return null;
                        }
                        Conditional conditional = new Conditional();
                        conditional.setKey(field);
                        conditional.setValue(v);
                        conditional.setOperator(Operator.apply(operator));
                        return conditional;
                    }
                    // 如果参数有缺失
                    String s = values.get(1);
                    // 判断是否为条件（如果是条件忽略）
                    boolean operator = Operator.isOperator(s);
                    if (operator || StringUtils.isBlank(s)) {
                        return null;
                    }
                    Conditional conditional = new Conditional();
                    conditional.setKey(field);
                    conditional.setValue(s);
                    conditional.setOperator(Operator.EQ);
                    return conditional;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }
}
