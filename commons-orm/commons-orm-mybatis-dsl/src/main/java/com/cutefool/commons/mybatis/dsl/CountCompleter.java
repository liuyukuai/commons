package com.cutefool.commons.mybatis.dsl;

import org.mybatis.dynamic.sql.select.CountDSL;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.util.Buildable;
import org.mybatis.dynamic.sql.where.AbstractWhereDSL;


public abstract class CountCompleter implements CountDSLCompleter, MybatisWhere {

    CountCompleter() {
    }

    @Override
    public Buildable<SelectModel> apply(CountDSL<SelectModel> dsl) {
        AbstractWhereDSL<?> where = newSelect(dsl);
        this.where(dsl, where);
        return dsl;
    }

    protected abstract void where(CountDSL<SelectModel> dsl, AbstractWhereDSL<?> where);

}
