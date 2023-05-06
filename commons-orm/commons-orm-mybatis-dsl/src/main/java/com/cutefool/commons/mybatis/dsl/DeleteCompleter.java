package com.cutefool.commons.mybatis.dsl;

import org.mybatis.dynamic.sql.delete.DeleteDSL;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.DeleteModel;
import org.mybatis.dynamic.sql.util.Buildable;
import org.mybatis.dynamic.sql.where.AbstractWhereDSL;

public abstract class DeleteCompleter implements DeleteDSLCompleter, MybatisWhere {

    DeleteCompleter() {
    }

    @Override
    public Buildable<DeleteModel> apply(DeleteDSL<DeleteModel> dsl) {
        AbstractWhereDSL<?> where = newSelect(dsl);
        this.where(dsl, where);
        return dsl;
    }

    protected abstract void where(DeleteDSL<DeleteModel> dsl, AbstractWhereDSL<?> where);

}
