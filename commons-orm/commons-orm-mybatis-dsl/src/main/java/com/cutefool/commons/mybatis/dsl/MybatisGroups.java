/*
 *
 */
package com.cutefool.commons.mybatis.dsl;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.util.Lists;
import org.mybatis.dynamic.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 271007729@qq.com
 * @date 2022/9/20 4:15 PM
 */
@SuppressWarnings({"all"})
public class MybatisGroups {

    private List<DslCon> dslConList;

    private MybatisGroups() {
        this.dslConList = new ArrayList<>();
    }

    public static MybatisGroups group() {
        return new MybatisGroups();
    }

    public MybatisGroups or(SqlColumn<?> sqlColumn, VisitableCondition condition) {
        if (Objects.nonNull(sqlColumn) && Objects.nonNull(condition)) {
            dslConList.add(new DslCon(sqlColumn, Operator.OR, condition));
        }
        return this;
    }

    public MybatisGroups and(SqlColumn<?> sqlColumn, VisitableCondition condition) {
        if (Objects.nonNull(sqlColumn) && Objects.nonNull(condition)) {
            dslConList.add(new DslCon(sqlColumn, Operator.AND, condition));
        }
        return this;
    }

    public static CriteriaGroup and(List<MybatisGroups> mybatisGroups) {
        return doGroup(mybatisGroups, Operator.AND);
    }

    public static CriteriaGroup or(List<MybatisGroups> mybatisGroups) {
        return doGroup(mybatisGroups, Operator.OR);
    }

    private static CriteriaGroup doGroup(List<MybatisGroups> mybatisGroups, Operator operator) {
        if (Lists.isEmpty(mybatisGroups)) {
            throw new BizException("condition is empty. pleaase confirm it.");
        }
        MybatisGroups first = mybatisGroups.get(0);
        int size = mybatisGroups.size();
        if (size == 1) {
            return first.get();
        }

        AndOrCriteriaGroup[] groups = new AndOrCriteriaGroup[mybatisGroups.size() - 1];
        for (int i = 1; i < mybatisGroups.size(); i++) {
            if (Objects.equals(Operator.AND, operator)) {
                groups[i - 1] = SqlBuilder.and(mybatisGroups.get(i).get());
            } else {
                groups[i - 1] = SqlBuilder.or(mybatisGroups.get(i).get());
            }
        }
        return SqlBuilder.group(first.get(), groups);
    }


    public CriteriaGroup get() {
        if (Lists.isEmpty(dslConList)) {
            throw new BizException("condition is empty. pleaase confirm it.");
        }
        int size = dslConList.size();
        // 取第一个元素
        DslCon first = dslConList.get(0);
        if (size == 1) {
            return SqlBuilder.group(first.getSqlColumn(), first.getVisitableCondition());
        }
        return SqlBuilder.group(first.getSqlColumn(), first.getVisitableCondition(), this.groups());
    }

    private AndOrCriteriaGroup[] groups() {
        AndOrCriteriaGroup[] groups = new AndOrCriteriaGroup[dslConList.size() - 1];
        for (int i = 1; i < dslConList.size(); i++) {
            DslCon dslCon = dslConList.get(i);
            if (Objects.equals(Operator.AND, dslCon.getOperator())) {
                groups[i - 1] = SqlBuilder.and(dslCon.getSqlColumn(), dslCon.getVisitableCondition());
            } else {
                groups[i - 1] = SqlBuilder.or(dslCon.getSqlColumn(), dslCon.getVisitableCondition());
            }
        }
        return groups;
    }


    @Data
    @AllArgsConstructor
    public static class DslCon {
        /**
         * 数据库字段
         */
        private SqlColumn<?> sqlColumn;
        /**
         * 操作类型
         */
        private Operator operator;
        /**
         * 值
         */
        private VisitableCondition visitableCondition;

    }

}
