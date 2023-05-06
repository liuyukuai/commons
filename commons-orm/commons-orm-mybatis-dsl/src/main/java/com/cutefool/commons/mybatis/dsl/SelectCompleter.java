package com.cutefool.commons.mybatis.dsl;

import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.util.Buildable;
import org.mybatis.dynamic.sql.where.AbstractWhereDSL;

public abstract class SelectCompleter implements SelectDSLCompleter, MybatisWhere {

    SelectCompleter() {
    }

    @Override
    public Buildable<SelectModel> apply(QueryExpressionDSL<SelectModel> dsl) {
        AbstractWhereDSL<?> where = this.newSelect(dsl);
        this.where(dsl, where);
        return dsl;
    }

    protected abstract void where(QueryExpressionDSL<SelectModel> dsl, AbstractWhereDSL<?> where);

}
