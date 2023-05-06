/*
 *
 */
package com.cutefool.commons.mybatis.dsl;

import com.cutefool.commons.core.Naming;
import com.cutefool.commons.core.Status;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.expression.Conditioning;
import com.cutefool.commons.mybatis.dsl.utils.MybatisUtils;
import com.cutefool.commons.orm.RootDomain;
import com.cutefool.commons.orm.interfaces.IOperations;
import com.cutefool.commons.orm.interfaces.ISpecificationExecutor;
import com.cutefool.commons.orm.interfaces.RdsOperations;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSL;
import org.mybatis.dynamic.sql.delete.DeleteModel;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.CountDSL;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.where.AbstractWhereDSL;
import org.mybatis.dynamic.sql.where.WhereApplier;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author 271007729@qq.com
 * @date 2022/9/20 4:15 PM
 */
@SuppressWarnings({"all"})
public interface MybatisFieldsOperations<D, E, ID, R extends MybatisMapper<E, ID>, Query extends Conditioning> extends IOperations<D, E, ID, R>, ISpecificationExecutor<E, Query>, MybatisCompleter<E, ID>, RdsOperations, Naming<ID> {

    /**
     * 初始化条件
     *
     * @param where where
     */
    default void init(AbstractWhereDSL<?> where) {
    }

    /**
     * 通过属性名称查询sqlColumn
     *
     * @param fields 属性名称
     * @param <T>    类型
     * @return SqlColumn
     */
    default <T> SqlColumn<T> sqlColumn(String fields) {
        Map<String, SqlColumn<?>> columnMap = this.getRepository().columnsMap();
        return (SqlColumn<T>) MybatisUtils.findByName(fields, columnMap);
    }

    /**
     * 获取主键（只支持单个主键）
     *
     * @return 获取表
     */
    default SqlColumn<ID> idColumn() {
        return this.getRepository().idColumn();
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listBy(BiConsumer<QueryExpressionDSL<SelectModel>, AbstractWhereDSL<?>> consumer) {
        return this.getRepository().select(this.newSelect(consumer));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listBy(WhereApplier whereApplier) {
        if (Objects.isNull(whereApplier)) {
            return Lists.newArrayList();
        }
        return this.getRepository().select(this.newSelect((dsl, where) -> {
            if (Objects.nonNull(whereApplier)) {
                where.applyWhere(whereApplier);
            }
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFields(String fields, T v) {
        return this.listByFields(this.sqlColumn(fields), v);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getBy(BiConsumer<QueryExpressionDSL<SelectModel>, AbstractWhereDSL<?>> consumer) {
        List<E> selects = this.getRepository().select(this.newSelect(consumer));
        return Lists.iterable(selects) ? Optional.ofNullable(selects.get(0)) : Optional.empty();
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getBy(WhereApplier whereApplier) {
        if (Objects.isNull(whereApplier)) {
            return Optional.empty();
        }


        List<E> selects = this.getRepository().select(this.newSelect((dsl, where) -> {
            where.applyWhere(whereApplier);
        }));
        return Lists.iterable(selects) ? Optional.ofNullable(selects.get(0)) : Optional.empty();
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByFields(String fields, T v) {
        SqlColumn<T> sqlColumn = this.sqlColumn(fields);
        return this.getByFields(sqlColumn, v);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFields(String fields, T v, String... selects) {
        return this.listByFields(this.sqlColumn(fields), v, selects);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByFields(String fields, T v, String... selects) {
        return this.getByFields(this.sqlColumn(fields), v, selects);
    }


    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFields(String fields, Collection<T> vs) {
        SqlColumn<T> objectSqlColumn = this.sqlColumn(fields);
        return this.listByFields(objectSqlColumn, vs);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFields(String fields, Collection<T> vs, String... selects) {
        SqlColumn<T> sqlColumn = this.sqlColumn(fields);
        return this.listByFields(sqlColumn, vs, selects);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFields(SqlColumn<T> sqlColumn, Collection<T> vs) {
        if (Lists.isEmpty(vs) || Objects.isNull(sqlColumn)) {
            return Lists.newArrayList();
        }
        return this.getRepository().select(this.newSelect((dsl, where) -> {
            where.and(sqlColumn, SqlBuilder.isIn(vs));
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFields(SqlColumn<T> sqlColumn, T v) {
        if (Objects.isNull(sqlColumn)) {
            return Lists.newArrayList();
        }
        return this.getRepository().select(this.newSelect((dsl, where) -> {
            where.and(sqlColumn, SqlBuilder.isEqualTo(v));
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByFields(SqlColumn<T> sqlColumn, T v) {
        if (Objects.isNull(sqlColumn)) {
            return Optional.empty();
        }
        List<E> selects = this.getRepository().select(this.newSelect((dsl, where) -> {
            where.and(sqlColumn, SqlBuilder.isEqualTo(v));
        }));
        return Lists.iterable(selects) ? Optional.ofNullable(selects.get(0)) : Optional.empty();
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFields(SqlColumn<T> sqlColumn, T v, String... selects) {
        if (Objects.isNull(sqlColumn)) {
            return Lists.newArrayList();
        }
        return listByFields(sqlColumn, v, this.getRepository().doSelects(selects));
    }


    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByFields(SqlColumn<T> sqlColumn, T v, String... selects) {
        if (Objects.isNull(sqlColumn)) {
            return Optional.empty();
        }
        return getByFields(sqlColumn, v, this.getRepository().doSelects(selects));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFields(SqlColumn<T> sqlColumn, T v, SqlColumn... selects) {
        if (Objects.isNull(sqlColumn)) {
            return Lists.newArrayList();
        }
        QueryExpressionDSL<SelectModel>.QueryExpressionWhereBuilder where = SqlBuilder.select(selects)
                .from(this.getRepository().sqlTable())
                .where(sqlColumn, SqlBuilder.isEqualTo(v));
        this.init(where);
        SelectStatementProvider selectStatement = where
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return getRepository().selectMany(selectStatement);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByFields(SqlColumn<T> sqlColumn, T v, SqlColumn... selects) {
        if (Objects.isNull(sqlColumn)) {
            return Optional.empty();
        }
        QueryExpressionDSL<SelectModel>.QueryExpressionWhereBuilder where = SqlBuilder.select(selects)
                .from(this.getRepository().sqlTable())
                .where(sqlColumn, SqlBuilder.isEqualTo(v));
        this.init(where);
        SelectStatementProvider selectStatement = where
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<E> es = getRepository().selectMany(selectStatement);
        return Lists.iterable(es) ? Optional.ofNullable(es.get(0)) : Optional.empty();
    }


    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFields(SqlColumn<T> sqlColumn, Collection<T> vs, String... selects) {
        if (Lists.isEmpty(vs) || Objects.isNull(sqlColumn)) {
            return Lists.newArrayList();
        }
        return this.listByFields(sqlColumn, vs, this.getRepository().doSelects(selects));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFields(SqlColumn<T> sqlColumn, Collection<T> vs, SqlColumn... selects) {
        if (Lists.isEmpty(vs) || Objects.isNull(sqlColumn)) {
            return Lists.newArrayList();
        }
        QueryExpressionDSL<SelectModel>.QueryExpressionWhereBuilder where = SqlBuilder.select(selects)
                .from(this.getRepository().sqlTable())
                .where(sqlColumn, SqlBuilder.isIn(vs));
        this.init(where);
        SelectStatementProvider selectStatement = where
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return getRepository().selectMany(selectStatement);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByWithoutDeleted(BiConsumer<QueryExpressionDSL<SelectModel>, AbstractWhereDSL<?>> consumer) {
        return this.getRepository().select(this.newSelect((dsl, where) -> {
            SqlColumn<Object> sqlColumn = this.sqlColumn(RootDomain.FIELD_DELETE_STATUS);
            if (Objects.nonNull(sqlColumn)) {
                where.and(sqlColumn, SqlBuilder.isEqualTo(Status.STATUS_FALSE));
            }
            consumer.accept(dsl, where);
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByWithoutDeleted() {
        return this.listByWithoutDeleted(where -> {
        });
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByWithoutDeleted(WhereApplier whereApplier) {
        if (Objects.isNull(whereApplier)) {
            return Lists.newArrayList();
        }
        return this.listByWithoutDeleted((dsl, where) -> {
            where.applyWhere(whereApplier);
        });
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFieldsWithoutDeleted(String fields, T v) {
        return this.listByFieldsWithoutDeleted(this.sqlColumn(fields), v);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByFieldsWithoutDeleted(String fields, T v) {
        SqlColumn<T> sqlColumn = this.sqlColumn(fields);
        return this.getByFieldsWithoutDeleted(sqlColumn, v);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByWithoutDeleted(BiConsumer<QueryExpressionDSL<SelectModel>, AbstractWhereDSL<?>> consumer) {
        List<E> selects = this.getRepository().select(this.newSelect((dsl, where) -> {
            SqlColumn<Object> sqlColumn = this.sqlColumn(RootDomain.FIELD_DELETE_STATUS);
            if (Objects.nonNull(sqlColumn)) {
                where.and(sqlColumn, SqlBuilder.isEqualTo(Status.STATUS_FALSE));
            }
            consumer.accept(dsl, where);
        }));
        return Lists.iterable(selects) ? Optional.ofNullable(selects.get(0)) : Optional.empty();
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFieldsWithoutDeleted(String fields, T v, String... selects) {
        return this.listByFieldsWithoutDeleted(this.sqlColumn(fields), v, selects);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByFieldsWithoutDeleted(String fields, T v, String... selects) {
        return this.getByFieldsWithoutDeleted(this.sqlColumn(fields), v, selects);
    }


    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFieldsWithoutDeleted(String fields, Collection<T> vs) {
        SqlColumn<T> objectSqlColumn = this.sqlColumn(fields);
        return this.listByFieldsWithoutDeleted(objectSqlColumn, vs);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFieldsWithoutDeleted(String fields, Collection<T> vs, String... selects) {
        SqlColumn<T> sqlColumn = this.sqlColumn(fields);
        return this.listByFieldsWithoutDeleted(sqlColumn, vs, selects);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, Collection<T> vs) {
        if (Lists.isEmpty(vs) || Objects.isNull(sqlColumn)) {
            return Lists.newArrayList();
        }
        return this.getRepository().select(this.newSelect((dsl, where) -> {
            where.and(sqlColumn, SqlBuilder.isIn(vs));
            SqlColumn<Object> delete = this.sqlColumn(RootDomain.FIELD_DELETE_STATUS);
            if (Objects.nonNull(delete)) {
                where.and(delete, SqlBuilder.isEqualTo(Status.STATUS_FALSE));
            }
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, T v) {
        if (Objects.isNull(sqlColumn) || Objects.isNull(v)) {
            return Lists.newArrayList();
        }
        return this.getRepository().select(this.newSelect((dsl, where) -> {
            where.and(sqlColumn, SqlBuilder.isEqualTo(v));
            SqlColumn<Object> delete = this.sqlColumn(RootDomain.FIELD_DELETE_STATUS);
            if (Objects.nonNull(delete)) {
                where.and(delete, SqlBuilder.isEqualTo(Status.STATUS_FALSE));
            }
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, T v) {
        if (Objects.isNull(sqlColumn) || Objects.isNull(v)) {
            return Optional.empty();
        }
        List<E> selects = this.getRepository().select(this.newSelect((dsl, where) -> {
            where.and(sqlColumn, SqlBuilder.isEqualTo(v));
            SqlColumn<Object> delete = this.sqlColumn(RootDomain.FIELD_DELETE_STATUS);
            if (Objects.nonNull(delete)) {
                where.and(delete, SqlBuilder.isEqualTo(Status.STATUS_FALSE));
            }
        }));
        return Lists.iterable(selects) ? Optional.ofNullable(selects.get(0)) : Optional.empty();
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, T v, String... selects) {
        if (Objects.isNull(sqlColumn) || Objects.isNull(v)) {
            return Lists.newArrayList();
        }
        return listByFieldsWithoutDeleted(sqlColumn, v, this.getRepository().doSelects(selects));
    }


    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, T v, String... selects) {
        if (Objects.isNull(sqlColumn) || Objects.isNull(v)) {
            return Optional.empty();
        }
        return getByFieldsWithoutDeleted(sqlColumn, v, this.getRepository().doSelects(selects));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, T v, SqlColumn... selects) {
        if (Objects.isNull(sqlColumn) || Objects.isNull(v)) {
            return Lists.newArrayList();
        }
        QueryExpressionDSL<SelectModel>.QueryExpressionWhereBuilder where = SqlBuilder.select(selects)
                .from(this.getRepository().sqlTable())
                .where(sqlColumn, SqlBuilder.isEqualTo(v));
        this.init(where);
        SqlColumn<Object> delete = this.sqlColumn(RootDomain.FIELD_DELETE_STATUS);
        if (Objects.nonNull(delete)) {
            where.and(delete, SqlBuilder.isEqualTo(Status.STATUS_FALSE));
        }
        SelectStatementProvider selectStatement = where
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return getRepository().selectMany(selectStatement);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Optional<E> getByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, T v, SqlColumn... selects) {
        if (Objects.isNull(sqlColumn) || Objects.isNull(v)) {
            return Optional.empty();
        }
        QueryExpressionDSL<SelectModel>.QueryExpressionWhereBuilder where = SqlBuilder.select(selects)
                .from(this.getRepository().sqlTable())
                .where(sqlColumn, SqlBuilder.isEqualTo(v));
        this.init(where);
        SqlColumn<Object> delete = this.sqlColumn(RootDomain.FIELD_DELETE_STATUS);
        if (Objects.nonNull(delete)) {
            where.and(delete, SqlBuilder.isEqualTo(Status.STATUS_FALSE));
        }
        SelectStatementProvider selectStatement = where
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<E> es = getRepository().selectMany(selectStatement);
        return Lists.iterable(es) ? Optional.ofNullable(es.get(0)) : Optional.empty();
    }


    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, Collection<T> vs, String... selects) {
        if (Lists.isEmpty(vs) || Objects.isNull(sqlColumn)) {
            return Lists.newArrayList();
        }
        return this.listByFieldsWithoutDeleted(sqlColumn, vs, this.getRepository().doSelects(selects));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> List<E> listByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, Collection<T> vs, SqlColumn... selects) {
        if (Lists.isEmpty(vs) || Objects.isNull(sqlColumn)) {
            return Lists.newArrayList();
        }
        QueryExpressionDSL<SelectModel>.QueryExpressionWhereBuilder where = SqlBuilder.select(selects)
                .from(this.getRepository().sqlTable())
                .where(sqlColumn, SqlBuilder.isIn(vs));
        this.init(where);
        SqlColumn<Object> delete = this.sqlColumn(RootDomain.FIELD_DELETE_STATUS);
        if (Objects.nonNull(delete)) {
            where.and(delete, SqlBuilder.isEqualTo(Status.STATUS_FALSE));
        }
        SelectStatementProvider selectStatement = where
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return getRepository().selectMany(selectStatement);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateByFields(String identity, S s, String sqlColumn, T vs) {
        SqlColumn<S> s1 = this.sqlColumn(identity);
        SqlColumn<T> s2 = this.sqlColumn(sqlColumn);
        return this.updateByFields(s1, s, s2, vs);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateByFields(String identity, Collection<S> ss, String sqlColumn, T vs) {
        SqlColumn<S> s1 = this.sqlColumn(identity);
        SqlColumn<T> s2 = this.sqlColumn(sqlColumn);
        return this.updateByFields(s1, ss, s2, vs);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateByFields(String identity, S s, SqlColumn<T> sqlColumn, T vs) {
        SqlColumn<S> s1 = this.sqlColumn(identity);
        return this.updateByFields(s1, s, sqlColumn, vs);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateByFields(String identity, Collection<S> ss, SqlColumn<T> sqlColumn, T vs) {
        SqlColumn<S> s1 = this.sqlColumn(identity);
        return this.updateByFields(s1, ss, sqlColumn, vs);
    }


    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateByFields(SqlColumn<S> identity, S s, String sqlColumn, T vs) {
        SqlColumn<T> s2 = this.sqlColumn(sqlColumn);
        return this.updateByFields(identity, s, s2, vs);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateByFields(SqlColumn<S> identity, Collection<S> ss, String sqlColumn, T vs) {
        SqlColumn<T> s2 = this.sqlColumn(sqlColumn);
        return this.updateByFields(identity, ss, s2, vs);
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateByFields(SqlColumn<S> identity, S s, SqlColumn<T> sqlColumn, T vs) {
        if (Objects.isNull(identity) || Objects.isNull(sqlColumn)) {
            return 0;
        }

        return this.getRepository().update(this.newUpdate((dsl, where) -> {
            dsl.set(sqlColumn).equalTo(vs);
            where.and(identity, SqlBuilder.isEqualTo(s));
        }));
    }


    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateByFields(SqlColumn<S> identity, Collection<S> ss, SqlColumn<T> sqlColumn, T vs) {
        if (Objects.isNull(identity) || Objects.isNull(sqlColumn) || Lists.isEmpty(ss)) {
            return 0;
        }
        return this.getRepository().update(this.newUpdate((dsl, where) -> {
            dsl.set(sqlColumn).equalTo(vs);
            where.and(identity, SqlBuilder.isIn(ss));
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param consumer consumer
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateBy(BiConsumer<UpdateDSL<UpdateModel>, AbstractWhereDSL<?>> consumer) {
        return this.getRepository().update(this.newUpdate((dsl, whereDSL) -> {
            consumer.accept(dsl, whereDSL);
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param consumer consumer
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateBy(Consumer<UpdateDSL<UpdateModel>> consumer, WhereApplier whereApplier) {
        if (Objects.isNull(whereApplier)) {
            return 0;
        }
        return this.getRepository().update(this.newUpdate((dsl, where) -> {
            consumer.accept(dsl);

            where.applyWhere(whereApplier);
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param consumer consumer
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateById(ID s, Consumer<UpdateDSL<UpdateModel>> consumer) {
        return this.getRepository().update(this.newIdUpdate(s, (dsl) -> {
            consumer.accept(dsl);
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param consumer consumer
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateById(Collection<ID> s, Consumer<UpdateDSL<UpdateModel>> consumer) {
        return this.getRepository().update(this.newIdUpdate(s, (dsl) -> {
            consumer.accept(dsl);
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateById(ID s, String sqlColumn, T vs) {
        SqlColumn<T> column = this.sqlColumn(sqlColumn);
        if (Objects.isNull(column)) {
            return 0;
        }
        return this.updateById(s, (dsl) -> {
            dsl.set(column).equalTo(vs);
        });
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateById(Collection<ID> ss, String sqlColumn, T vs) {
        SqlColumn<T> column = this.sqlColumn(sqlColumn);
        if (Objects.isNull(column)) {
            return 0;
        }
        return this.updateById(ss, (dsl) -> {
            dsl.set(column).equalTo(vs);
        });
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateById(ID s, SqlColumn<T> sqlColumn, T vs) {
        if (Objects.isNull(sqlColumn)) {
            return 0;
        }
        return this.updateById(s, (dsl) -> {
            dsl.set(sqlColumn).equalTo(vs);
        });
    }

    /**
     * 通过属性查询数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int updateById(Collection<ID> ss, SqlColumn<T> sqlColumn, T vs) {
        if (Objects.isNull(sqlColumn)) {
            return 0;
        }
        return this.updateById(ss, (dsl) -> {
            dsl.set(sqlColumn).equalTo(vs);
        });
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteByFields(String identity, S s) {
        SqlColumn<S> sqlColumn = this.sqlColumn(identity);
        return this.deleteByFields(sqlColumn, s);
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteByFields(String identity, Collection<S> ss) {
        SqlColumn<S> sqlColumn = this.sqlColumn(identity);
        return this.deleteByFields(sqlColumn, ss);
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteByFields(SqlColumn<S> identity, S s) {
        if (Objects.isNull(identity) || Objects.isNull(s)) {
            return 0;
        }

        return this.getRepository().delete(this.newDelete((dsl, where) -> {
            where.and(identity, SqlBuilder.isEqualTo(s));
        }));
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteByFields(SqlColumn<S> identity, Collection<S> ss) {
        if (Objects.isNull(identity) || Lists.isEmpty(ss)) {
            return 0;
        }
        return this.getRepository().delete(this.newDelete((dsl, where) -> {
            where.and(identity, SqlBuilder.isIn(ss));
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param consumer consumer
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteBy(BiConsumer<DeleteDSL<DeleteModel>, AbstractWhereDSL<?>> consumer) {
        return this.getRepository().delete(this.newDelete((dsl, whereDSL) -> {
            consumer.accept(dsl, whereDSL);
        }));
    }

    /**
     * 通过属性查询数据
     *
     * @param consumer consumer
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteBy(WhereApplier whereApplier) {
        if (Objects.isNull(whereApplier)) {
            return 0;
        }

        return this.deleteBy((dsl, where) -> {

            where.applyWhere(whereApplier);
        });
    }

    /**
     * 通过属性查询数据
     *
     * @param consumer consumer
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteById(ID s) {
        return this.deleteByFields(idColumn(), s);
    }

    /**
     * 通过属性查询数据
     *
     * @param consumer consumer
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteById(Collection<ID> ss) {
        return this.deleteByFields(idColumn(), ss);
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteWithDeleted(String identity, ID s) {
        return this.updateByFields(this.idColumn(), s, RootDomain.FIELD_DELETE_STATUS, Status.STATUS_TRUE);
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteWithDeleted(String identity, Collection<ID> ss) {
        return this.updateByFields(idColumn(), ss, RootDomain.FIELD_DELETE_STATUS, Status.STATUS_TRUE);
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteWithDeleted(SqlColumn<S> identity, S s) {
        return this.updateByFields(identity, s, RootDomain.FIELD_DELETE_STATUS, Status.STATUS_TRUE);
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteWithDeleted(SqlColumn<S> identity, Collection<S> ss) {
        return this.updateByFields(identity, ss, RootDomain.FIELD_DELETE_STATUS, Status.STATUS_TRUE);
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteWithDeleted(BiConsumer<UpdateDSL<UpdateModel>, AbstractWhereDSL<?>> consumer) {
        return this.getRepository().update(this.newUpdate((dsl, whereDSL) -> {
            consumer.accept(dsl, whereDSL);
        }));
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteWithDeleted(WhereApplier whereApplier) {
        return this.updateBy(dsl -> {
            dsl.set(this.sqlColumn(RootDomain.FIELD_DELETE_STATUS)).equalTo(Status.STATUS_TRUE);
        }, whereApplier);
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteWithDeletedById(ID s) {
        return this.updateById(s, (dsl) -> {
            dsl.set(this.sqlColumn(RootDomain.FIELD_DELETE_STATUS)).equalTo(Status.STATUS_TRUE);
        });
    }

    /**
     * 通过属性删除数据
     *
     * @param sqlColumn 列
     * @param vs        值
     * @param <T>       t
     * @return 数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <S, T> int deleteWithDeletedById(Collection<ID> ss) {
        return this.updateById(ss, (dsl) -> {
            dsl.set(this.sqlColumn(RootDomain.FIELD_DELETE_STATUS)).equalTo(Status.STATUS_TRUE);
        });
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param applier applier
     * @return 数据
     */
    default Long countBy(WhereApplier applier) {
        if (Objects.isNull(applier)) {
            return 0L;
        }
        return this.getRepository().count(this.newCount((dsl, where) -> {

            where.applyWhere(applier);
        }));
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Long countBy(BiConsumer<CountDSL<SelectModel>, AbstractWhereDSL<?>> consumer) {
        if (Objects.isNull(consumer)) {
            return 0L;
        }
        return this.getRepository().count(this.newCount(consumer));
    }


    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Long countByFields(String fields, T v) {
        return this.countByFields(this.sqlColumn(fields), v);
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Long countByFields(String fields, Collection<T> vs) {
        SqlColumn<T> sqlColumn = this.sqlColumn(fields);
        return this.countByFields(sqlColumn, vs);
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Long countByFields(SqlColumn<T> sqlColumn, T v) {
        if (Objects.isNull(sqlColumn) || Objects.isNull(v)) {
            return 0L;
        }
        return this.getRepository().count(this.newCount((dsl, where) -> {
            where.and(sqlColumn, SqlBuilder.isEqualTo(v));
        }));
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Long countByFields(SqlColumn<T> sqlColumn, Collection<T> vs) {
        if (Objects.isNull(sqlColumn) || Lists.isEmpty(vs)) {
            return 0L;
        }
        return this.getRepository().count(this.newCount((dsl, where) -> {
            where.and(sqlColumn, SqlBuilder.isIn(vs));
        }));
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Long countByFieldsWithoutDeleted(String fields, T v) {
        return this.countByFieldsWithoutDeleted(this.sqlColumn(fields), v);
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Long countByFieldsWithoutDeleted(String fields, Collection<T> vs) {
        SqlColumn<T> sqlColumn = this.sqlColumn(fields);
        return this.countByFieldsWithoutDeleted(sqlColumn, vs);
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Long countByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, T v) {
        if (Objects.isNull(sqlColumn) || Objects.isNull(v)) {
            return 0L;
        }
        return this.getRepository().count(this.newCount((dsl, where) -> {
            where.and(sqlColumn, SqlBuilder.isEqualTo(v));
            where.and(this.sqlColumn(RootDomain.FIELD_DELETE_STATUS), SqlBuilder.isEqualTo(Status.STATUS_FALSE));
        }));
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default <T> Long countByFieldsWithoutDeleted(SqlColumn<T> sqlColumn, Collection<T> vs) {
        if (Objects.isNull(sqlColumn) || Lists.isEmpty(vs)) {
            return 0L;
        }
        return this.getRepository().count(this.newCount((dsl, where) -> {
            where.and(sqlColumn, SqlBuilder.isIn(vs));
            where.and(this.sqlColumn(RootDomain.FIELD_DELETE_STATUS), SqlBuilder.isEqualTo(Status.STATUS_FALSE));
        }));
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @return 数据
     */
    default Long countById(ID v) {
        return this.countByFields(this.idColumn(), v);
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default Long countById(Collection<ID> vs) {
        return this.countByFields(this.idColumn(), vs);
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @return 数据
     */
    default Long countByIdWithoutDeleted(ID v) {
        return this.countByFieldsWithoutDeleted(this.idColumn(), v);
    }

    /**
     * 通过属性查询数据 （总数）
     *
     * @param sqlColumn 列
     * @param v         值
     * @param <T>       t
     * @return 数据
     */
    default Long countByIdWithoutDeleted(Collection<ID> vs) {
        return this.countByFieldsWithoutDeleted(this.idColumn(), vs);
    }
}
