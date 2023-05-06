package com.cutefool.commons.mybatis.dsl.utils;

import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.expression.Conditioning;
import com.cutefool.commons.expression.libs.Conditional;
import com.cutefool.commons.expression.libs.Conditionals;
import com.cutefool.commons.orm.Transformation;
import com.cutefool.commons.orm.Transforms;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.AndOrCriteriaGroup;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.VisitableCondition;
import org.mybatis.dynamic.sql.where.AbstractWhereDSL;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author : liuyk
 */
@SuppressWarnings({"all"})
public class Restrictions<T> {

    private AbstractWhereDSL builder;

    private final Map<String, SqlColumn<?>> columnMap;

    private Restrictions(AbstractWhereDSL builder, Map<String, SqlColumn<?>> columnMap) {
        this.columnMap = columnMap;
        this.builder = builder;
    }

    public static <T> Restrictions<T> of(AbstractWhereDSL builder, Map<String, SqlColumn<?>> columnMap) {
        return new Restrictions<>(builder, columnMap);
    }

    public <E> Restrictions<T> where(Conditionals conditionals) {
        if (Objects.isNull(conditionals)) {
            return this;
        }

        Operator operator = conditionals.getOperator();
        // 过滤只保留属性存在的条件
        List<Conditional> conditionalList = Lists.empty(conditionals.getConditionals())
                .stream()
                .filter(e -> this.columnMap.containsKey(e.getKey()))
                .collect(Collectors.toList());
        List<AndOrCriteriaGroup> groups = Lists.newArrayList();
        if (Lists.isEmpty(conditionalList)) {
            return this;
        }
        Conditional firstConditional = conditionalList.get(0);
        SqlColumn<?> firstSqlColumn = this.columnMap.get(firstConditional.getKey());
        VisitableCondition firstCondition = MybatisUtils.initCondition(firstConditional.getOperator(), firstConditional.getValue());

        // 如果是一个
        if (conditionalList.size() == 1) {
            this.builder.and(firstSqlColumn, firstCondition);
            return this;
        }

        switch (operator) {
            case AND:
                for (int i = 1; i < conditionalList.size(); i++) {
                    Conditional conditional = conditionalList.get(i);
                    SqlColumn<?> sqlColumn = this.columnMap.get(conditional.getKey());
                    AndOrCriteriaGroup and = and(sqlColumn, MybatisUtils.initCondition(conditional.getOperator(), conditional.getValue(), conditionals.getCycle()));
                    groups.add(and);
                }
                this.builder.and(group(firstSqlColumn, firstCondition, groups.toArray(new AndOrCriteriaGroup[]{})));
                return this;

            case OR:
                for (int i = 1; i < conditionalList.size(); i++) {
                    Conditional conditional = conditionalList.get(i);
                    SqlColumn<?> sqlColumn = this.columnMap.get(conditional.getKey());
                    AndOrCriteriaGroup and = or(sqlColumn, MybatisUtils.initCondition(conditional.getOperator(), conditional.getValue(), conditionals.getCycle()));
                    groups.add(and);
                }
                this.builder.and(group(firstSqlColumn, firstCondition, groups.toArray(new AndOrCriteriaGroup[]{})));
                return this;
            default:
                return this;
        }
    }

    public <E> Restrictions<T> where(Conditional conditional) {
        this.setCondition(conditional.getKey(), conditional.getOperator(), conditional.getValue());
        return this;
    }

    public <E> void setCondition(String name, Operator operator, Object value) {
        SqlColumn<?> sqlColumn = this.columnMap.get(name);
        if (Objects.nonNull(sqlColumn)) {
            VisitableCondition firstCondition = MybatisUtils.initCondition(operator, value);
            this.builder.and(sqlColumn, firstCondition);
        }
    }


    public <E> Restrictions<T> where(List<Conditional> conditionals) {
        Lists.empty(conditionals).forEach(this::where);
        return this;
    }

    public <E> Restrictions<T> where(E query) {
        Map<String, Transformation> fields = Transforms.fields(query);
        fields.entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> {
                    Transformation value = e.getValue();
                    if (Objects.equals(value.getOperator(), Operator.EQ)) {
                        return 1;
                    }
                    if (Objects.equals(value.getOperator(), Operator.IN)) {
                        return 2;
                    }
                    return 3;
                }))
                .forEach((e) -> parse(e.getValue()));
        // 处理conditing条件
        if (query instanceof Conditioning) {
//            this.where(((Conditioning) query).getConditionals());
//            this.where(((Conditioning) query).getConditions());
            // 处理前端指定参数
            this.where(Conditioning.apply(((Conditioning) query).getQ()));
        }
        return this;
    }

    public Restrictions<T> eq(String name, Object value) {
        parse(name, value, true, Operator.EQ);
        return this;
    }

    public Restrictions<T> eq(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.EQ);
        return this;
    }

    public Restrictions<T> ne(String name, Object value) {
        parse(name, value, true, Operator.NE);
        return this;
    }

    public Restrictions<T> ne(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.NE);
        return this;
    }

    public Restrictions<T> like(String name, String value) {
        parse(name, value, true, Operator.LIKE);
        return this;
    }

    public Restrictions<T> like(String name, String value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.LIKE);
        return this;
    }

    public Restrictions<T> gt(String name, Object value) {
        parse(name, value, true, Operator.GT);
        return this;
    }

    public Restrictions<T> gt(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.GT);
        return this;
    }

    public Restrictions<T> lt(String name, Object value) {
        parse(name, value, true, Operator.LT);
        return this;
    }

    public Restrictions<T> lt(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.LT);
        return this;
    }

    public Restrictions<T> lte(String name, Object value) {
        parse(name, value, true, Operator.GTE);
        return this;
    }

    public Restrictions<T> lte(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.GTE);
        return this;
    }

    public Restrictions<T> gte(String name, Object value) {
        parse(name, value, true, Operator.LTE);
        return this;
    }

    public Restrictions<T> gte(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.LTE);
        return this;
    }

    public Restrictions<T> in(String name, Collection<?> value) {
        parse(name, value, true, Operator.IN);
        return this;

    }

    public Restrictions<T> in(String name, Collection<?> value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.IN);
        return this;

    }

    public Restrictions<T> notIn(String name, Collection<?> value) {
        parse(name, value, true, Operator.NOT_IN);
        return this;

    }

    public Restrictions<T> notIn(String name, Collection<?> value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.NOT_IN);
        return this;

    }

    private static boolean ignore(Object value, boolean ignoreEmpty) {
        if (Objects.isNull(value) && ignoreEmpty) {
            return true;
        }
        if (value instanceof String) {
            String v = (String) value;
            return StringUtils.isBlank(v) && ignoreEmpty;
        }

        if (value instanceof Collection) {
            Collection<?> v = (Collection<?>) value;
            return v.isEmpty() && ignoreEmpty;
        }
        return false;
    }

    private void parse(String name, Object value, boolean ignoreEmpty, Operator operator) {
        if (ignore(value, ignoreEmpty)) {
            return;
        }
        this.setCondition(name, operator, value);
    }

    private void parse(Transformation transformation) {
        if (ignore(transformation.getValue(), transformation.isIgnoreEmpty())) {
            return;
        }
        String[] fields = transformation.getField();
        if (Array.getLength(fields) == 1) {
            this.parse(fields[0], transformation.getValue(), transformation.isIgnoreEmpty(), transformation.getOperator());
            return;
        }
        this.groups(transformation);
    }

    private void groups(Transformation transformation) {
        String[] fields = transformation.getField();
        List<AndOrCriteriaGroup> groups = Lists.newArrayList();
        VisitableCondition visitableCondition = MybatisUtils.initCondition(transformation.getOperator(), transformation.getValue());
        // 排除第一个元素
        String field = fields[0];
        for (int i = 1; i < fields.length; i++) {
            String name = fields[i];

            SqlColumn sqlColumn = this.columnMap.get(name);
            if (Operator.OR == transformation.getRelation() && Objects.nonNull(sqlColumn)) {
                AndOrCriteriaGroup group = or(sqlColumn, visitableCondition);
                groups.add(group);
            }

            if (Operator.AND == transformation.getRelation() && Objects.nonNull(sqlColumn)) {
                AndOrCriteriaGroup group = and(sqlColumn, visitableCondition);
                groups.add(group);
            }
        }

        SqlColumn<?> sqlColumn = this.columnMap.get(field);
        if (Objects.nonNull(sqlColumn)) {
            this.builder.and(group(this.columnMap.get(field), visitableCondition, groups.toArray(new AndOrCriteriaGroup[]{})));
        }
    }
}
