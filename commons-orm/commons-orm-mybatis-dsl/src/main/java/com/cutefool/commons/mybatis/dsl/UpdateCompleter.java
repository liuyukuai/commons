package com.cutefool.commons.mybatis.dsl;

import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.Buildable;
import org.mybatis.dynamic.sql.where.AbstractWhereDSL;

public abstract class UpdateCompleter implements UpdateDSLCompleter, MybatisWhere {

    UpdateCompleter() {
    }

    @Override
    public Buildable<UpdateModel> apply(UpdateDSL<UpdateModel> dsl) {
        AbstractWhereDSL<?> where = newSelect(dsl);
        this.where(dsl, where);
        return dsl;
    }

    protected abstract void where(UpdateDSL<UpdateModel> dsl, AbstractWhereDSL<?> where);

}
