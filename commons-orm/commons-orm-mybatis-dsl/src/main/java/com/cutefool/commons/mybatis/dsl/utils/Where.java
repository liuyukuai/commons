package com.cutefool.commons.mybatis.dsl.utils;

import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.mybatis.dsl.condition.FindInSet;
import com.cutefool.commons.orm.utils.SqlUtils;
import org.mybatis.dynamic.sql.BindableColumn;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.VisitableCondition;
import org.mybatis.dynamic.sql.where.WhereApplier;

import java.util.Collection;
import java.util.function.Function;

/**
 * sql where builder
 */
@SuppressWarnings("unused")
public final class Where {

    private final WhereApplier where;

    private boolean and;

    public Where() {
        this(true);
    }

    public Where(boolean and) {
        this.and = and;
        this.where = where -> {
        };
    }

    private <S> void init(BindableColumn<S> column, VisitableCondition<S> condition) {
        if (and) {
            this.where.andThen(where -> this.init(column, condition));
        } else {
            this.where.andThen(where -> where.or(column, condition));
        }
    }

    public static Where where() {
        return new Where();
    }

    public Where or() {
        this.and = false;
        return this;
    }

    public Where and() {
        this.and = true;
        return this;
    }

    public <T> Where isNull(SqlColumn<T> sqlColumn) {
        this.init(sqlColumn, SqlBuilder.isNull());
        return this;
    }

    public <T> Where notNull(SqlColumn<T> sqlColumn) {
        this.init(sqlColumn, SqlBuilder.isNotNull());
        return this;
    }

    public <T> Where eq(SqlColumn<T> sqlColumn, T v) {
        this.init(sqlColumn, SqlBuilder.isEqualTo(v));
        return this;
    }

    public <T> Where eqPresent(SqlColumn<T> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, SqlBuilder.isEqualTo(v));
        }
        return this;
    }

    public <T> Where ne(SqlColumn<T> sqlColumn, T v) {
        this.init(sqlColumn, SqlBuilder.isNotEqualTo(v));
        return this;
    }

    public <T> Where nePresent(SqlColumn<T> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, SqlBuilder.isNotEqualTo(v));
        }
        return this;
    }

    public <T> Where like(SqlColumn<String> sqlColumn, T v) {
        this.init(sqlColumn, SqlBuilder.isLike(SqlUtils.like(v)));
        return this;
    }

    public <T> Where likePresent(SqlColumn<String> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, SqlBuilder.isNotLike(SqlUtils.like(v)));
        }
        return this;
    }

    public <T> Where notLike(SqlColumn<String> sqlColumn, T v) {
        this.init(sqlColumn, SqlBuilder.isNotLike(SqlUtils.like(v)));
        return this;
    }

    public <T> Where notLikePresent(SqlColumn<String> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, SqlBuilder.isLike(SqlUtils.like(v)));
        }
        return this;
    }

    public <T> Where leftLike(SqlColumn<String> sqlColumn, T v) {
        this.init(sqlColumn, SqlBuilder.isLike(SqlUtils.leftLike(v)));
        return this;
    }

    public <T> Where leftLikePresent(SqlColumn<String> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, SqlBuilder.isLike(SqlUtils.leftLike(v)));
        }
        return this;
    }

    public <T> Where rightLike(SqlColumn<String> sqlColumn, T v) {
        this.init(sqlColumn, SqlBuilder.isLike(SqlUtils.rightLike(v)));
        return this;
    }

    public <T> Where rightLikePresent(SqlColumn<String> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, SqlBuilder.isLike(SqlUtils.rightLike(v)));
        }
        return this;
    }

    public <T> Where in(SqlColumn<T> sqlColumn, Collection<T> vs) {
        this.init(sqlColumn, SqlBuilder.isIn(vs));
        return this;
    }

    public <T> Where inPresent(SqlColumn<T> sqlColumn, Collection<T> vs) {
        if (Lists.iterable(vs)) {
            this.init(sqlColumn, SqlBuilder.isIn(vs));
        }
        return this;
    }

    public <T> Where gt(SqlColumn<T> sqlColumn, T v) {
        this.init(sqlColumn, SqlBuilder.isGreaterThan(v));
        return this;
    }

    public <T> Where gtPresent(SqlColumn<T> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, SqlBuilder.isGreaterThan(v));
        }
        return this;
    }

    public <T> Where gte(SqlColumn<T> sqlColumn, T v) {
        this.init(sqlColumn, SqlBuilder.isGreaterThanOrEqualTo(v));
        return this;
    }

    public <T> Where gtePresent(SqlColumn<T> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, SqlBuilder.isGreaterThanOrEqualTo(v));
        }
        return this;
    }

    public <T> Where lt(SqlColumn<T> sqlColumn, T v) {
        this.init(sqlColumn, SqlBuilder.isLessThan(v));
        return this;
    }

    public <T> Where ltPresent(SqlColumn<T> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, SqlBuilder.isLessThan(v));
        }
        return this;
    }

    public <T> Where lte(SqlColumn<T> sqlColumn, T v) {
        this.init(sqlColumn, SqlBuilder.isLessThanOrEqualTo(v));
        return this;
    }

    public <T> Where ltePresent(SqlColumn<T> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, SqlBuilder.isLessThanOrEqualTo(v));
        }
        return this;
    }


    public <T> Where find(SqlColumn<T> sqlColumn, T v) {
        this.init(sqlColumn, FindInSet.of(v));
        return this;
    }

    public <T> Where findPresent(SqlColumn<T> sqlColumn, T v) {
        if (Strings.isNotNull(v)) {
            this.init(sqlColumn, FindInSet.of(v));
        }
        return this;
    }

    public <T> Where and(Function<Where, Where> function) {
        function.apply(this);
        return this;
    }

    public <T> Where or(Function<Where, Where> function) {
        this.or();
        Where apply = function.apply(Where.where());
        return this;
    }

}
