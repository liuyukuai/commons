package com.cutefool.commons.mybatis;

import com.cutefool.commons.core.DurationLibs;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.cycle.Cycle;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.page.Sort;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.expression.ExpressionException;
import com.cutefool.commons.expression.Variables;
import com.cutefool.commons.expression.libs.Conditional;
import com.cutefool.commons.expression.libs.Conditionals;
import com.cutefool.commons.orm.PageRequest;
import com.cutefool.commons.orm.PagingUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 2019-07-10 09:26
 */

@SuppressWarnings("unused")
public interface IExample<ID> {

    /**
     * 设置排序规则
     *
     * @param orderByClause 排序规则
     */
    void setOrderByClause(String orderByClause);

    /**
     * 设置是否去重
     *
     * @param distinct 是否去重
     */
    void setDistinct(boolean distinct);

    /**
     * 设置查询多少条
     *
     * @param rows 条数
     * @return IExample
     */
    IExample limit(Integer rows);

    /**
     * 设置分页查询
     *
     * @param offset 起始数量
     * @param rows   条数
     * @return IExample
     */

    IExample limit(Integer offset, Integer rows);

    /**
     * 设置分页查询
     *
     * @param page     页数
     * @param pageSize 每页条数
     * @return IExample
     */
    IExample page(Integer page, Integer pageSize);

    /**
     * 通过page对象转换晟IExample对象
     *
     * @param paging paging对象
     * @return IExample
     */
    @SuppressWarnings("unchecked")
    default <E extends IExample> E page(Paging paging) {
        if (!Objects.isNull(paging)) {
            PageRequest pageRequest = PagingUtils.of(paging);
            List<Sort> sorts = pageRequest.getSorts();
            if (Lists.iterable(sorts)) {
                for (Sort sort : sorts) {
                    this.setOrderByClause(sort.getName() + " " + sort.getDirection());
                }
            }
            this.page(pageRequest.getPage(), pageRequest.getSize());
        }
        return (E) this;
    }

    /**
     * 通过sorts对象转换晟IExample对象
     *
     * @param sorts sorts对象
     * @return IExample
     */
    @SuppressWarnings("unchecked")
    default <E extends IExample> E sort(Sort... sorts) {
        if (Lists.iterable(sorts)) {
            for (Sort sort : sorts) {
                this.setOrderByClause(sort.getName() + " " + sort.getDirection());
            }
        }
        return (E) this;
    }

    /**
     * 通过id获取IExample对象
     *
     * @param ids ids
     * @return IExample
     */
    @SuppressWarnings("unchecked")
    default IExample ids(List<ID> ids) {
        throw new IllegalArgumentException(" please override this method");
    }

    /**
     * 通过租户获取IExample对象
     *
     * @param id id
     * @return IExample
     */
    @SuppressWarnings("unchecked")
    default IExample tenantId(Object id) {
        return this;
    }

    /**
     * 初始化
     */
    @SuppressWarnings("unchecked")
    default void init() {
    }

    /**
     * 通过orgCodes获取IExample对象
     *
     * @param orgCodes orgCodes
     * @return IExample
     */
    @SuppressWarnings("unchecked")
    default IExample orgCodes(List<String> orgCodes) {
       return this;
    }

    /**
     * 通过departments获取IExample对象
     *
     * @param departments departments
     * @return IExample
     */
    @SuppressWarnings("unchecked")
    default IExample departments(List<String> departments) {
        return this;
    }

    /**
     * 通过departments获取IExample对象
     *
     * @param orgCodes    orgCodes
     * @param departments departments
     * @return IExample
     */
    @SuppressWarnings("unchecked")
    default IExample privileges(List<String> orgCodes, List<String> departments) {
        return this;
    }

    /**
     * 通过condition获取IExample对象
     *
     * @param conditional conditional
     * @return IExample
     */
    default IExample condition(Conditional conditional) {
        return this;
    }

    /**
     * 通过condition获取IExample对象
     *
     * @param condition condition
     * @return IExample
     */
    default IExample condition(Condition condition) {
        return this;
    }

    /**
     * 通过condition获取IExample对象
     *
     * @param conditions conditions
     * @return IExample
     */
    default IExample condition(List<Condition> conditions) {
        return this.condition(conditions, Operator.AND);
    }

    /**
     * 通过condition获取IExample对象
     *
     * @param conditions conditions
     * @param operator   operator
     * @return IExample
     */
    default IExample condition(List<Condition> conditions, Operator operator) {
        return this;
    }

    /**
     * 通过conditions获取IExample对象
     *
     * @param conditionals conditionals
     * @return IExample
     */
    default IExample condition(Conditionals conditionals) {
        return this;
    }

    /**
     * 获取数据库字段
     *
     * @param name name
     * @return column
     */
    default String getColumn(String name) {
        throw new RuntimeException("please override this method.");
    }

    /**
     * 解析条件
     *
     * @param conditional conditional
     * @return condition
     */

    default Condition init(Conditional conditional) {
        return this.init(conditional, null);
    }

    /**
     * 解析条件
     *
     * @param conditional conditional
     * @return condition
     */

    default Condition init(Conditional conditional, Cycle cycle) {
        Operator operator = conditional.getOperator();
        switch (operator) {
            case EQ:
                return Condition.eq(this.getColumn(conditional.getKey()), this.doValue(conditional));
            case NE:
                return Condition.notEq(this.getColumn(conditional.getKey()), this.doValue(conditional));
            case IN:
                return Condition.in(this.getColumn(conditional.getKey()), this.doValue(conditional));
            case NOT_IN:
                return Condition.notIn(this.getColumn(conditional.getKey()), this.doValue(conditional));
            case LIKE:
                return Condition.like(this.getColumn(conditional.getKey()), String.valueOf(this.doValue(conditional)));
            case NOT_LIKE:
                return Condition.notLike(this.getColumn(conditional.getKey()), String.valueOf(this.doValue(conditional)));
            case BETWEEN:
                int betweenSize = 2;
                // 如果是between，使用逗号分隔
                List<String> values = Strings.split(conditional.getValue());
                // 如果值为空
                if (Lists.isEmpty(values)) {
                    throw new ExpressionException(ResponseCode.EXPRESSION_ERROR);
                }
                // 如果值只有一个
                if (values.size() < betweenSize) {
                    DurationLibs<?, ?> durationLibs = Variables.parseBetween(conditional.getValue(), cycle, true);
                    return Condition.between(this.getColumn(conditional.getKey()), durationLibs.getBefore(), durationLibs.getAfter());
                }
                Object before = Variables.parseVariable(values.get(0), false);
                Object after = Variables.parseVariable(values.get(1), false);
                return Condition.between(this.getColumn(conditional.getKey()), before, after);
            default:
                throw new RuntimeException();
        }
    }

    /**
     * 处理value值
     *
     * @param conditional conditional
     * @return value
     */
    default Object doValue(Conditional conditional) {
        return Variables.parseVariable(conditional.getValue(), null);
    }

    /**
     * 解析条件
     *
     * @param conditional conditional
     * @return condition
     */
    default List<Condition> init(Conditionals conditional) {
        List<Conditional> conditionals = conditional.getConditionals();
        return conditionals.stream().map(e -> this.init(e, conditional.getCycle())).collect(Collectors.toList());
    }
}
