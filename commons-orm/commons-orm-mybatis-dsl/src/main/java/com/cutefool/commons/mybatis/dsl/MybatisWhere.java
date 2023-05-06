package com.cutefool.commons.mybatis.dsl;

import org.mybatis.dynamic.sql.where.AbstractWhereDSL;
import org.mybatis.dynamic.sql.where.AbstractWhereSupport;

public interface MybatisWhere {

    default AbstractWhereDSL<?> newSelect(AbstractWhereSupport<?> c) {
        return c.where();
    }

}
