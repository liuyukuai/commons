package com.cutefool.commons.orm.jpa.criteria;


import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.Operator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class LogicalCriterion implements Criterion {

    /**
     * 逻辑表达式中包含的表达式
     */
    private List<Criterion> criterion;
    /**
     * 计算符
     */
    private Operator operator;

    public LogicalCriterion(List<Criterion> criterion, Operator operator) {
        this.criterion = criterion;
        this.operator = operator;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        List<Predicate> predicates = criterion.stream().map(c -> c.toPredicate(root, query, builder)).collect(Collectors.toList());
        switch (operator) {
            case OR:
                return builder.or(predicates.toArray(new Predicate[0]));
            case AND:
                return builder.and(predicates.toArray(new Predicate[0]));
            default:
                throw new BizException("operator not support.");
        }
    }
}
