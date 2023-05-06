package com.cutefool.commons.mybatis.dsl;

import org.mybatis.dynamic.sql.delete.DeleteDSL;
import org.mybatis.dynamic.sql.delete.DeleteModel;
import org.mybatis.dynamic.sql.select.CountDSL;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.where.AbstractWhereDSL;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface MybatisCompleter<E, ID> {

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    default SelectCompleter newSelect() {
        return newSelect((dsl, where) -> {
        });
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    default SelectCompleter newSelect(BiConsumer<QueryExpressionDSL<SelectModel>, AbstractWhereDSL<?>> consumer) {
        return new SelectCompleter() {
            @Override
            public void where(QueryExpressionDSL<SelectModel> dsl, AbstractWhereDSL<?> where) {
                if (Objects.nonNull(consumer)) {
                    consumer.accept(dsl, where);
                }
            }
        };
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    default CountCompleter newCount() {
        return newCount((dsl, where) -> {
        });
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    default CountCompleter newCount(BiConsumer<CountDSL<SelectModel>, AbstractWhereDSL<?>> consumer) {
        return new CountCompleter() {
            @Override
            protected void where(CountDSL<SelectModel> dsl, AbstractWhereDSL<?> where) {
                if (Objects.nonNull(consumer)) {
                    consumer.accept(dsl, where);
                }
            }
        };
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    default DeleteCompleter newDelete(BiConsumer<DeleteDSL<DeleteModel>, AbstractWhereDSL<?>> consumer) {
        return new DeleteCompleter() {
            @Override
            public void where(DeleteDSL<DeleteModel> dsl, AbstractWhereDSL<?> where) {
                if (Objects.nonNull(consumer)) {
                    consumer.accept(dsl, where);
                }
            }
        };
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    default UpdateCompleter newUpdate(BiConsumer<UpdateDSL<UpdateModel>, AbstractWhereDSL<?>> consumer) {
        return new UpdateCompleter() {
            @Override
            public void where(UpdateDSL<UpdateModel> dsl, AbstractWhereDSL<?> where) {
                if (Objects.nonNull(consumer)) {
                    consumer.accept(dsl, where);
                }
            }
        };
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    UpdateCompleter newIdUpdate(ID id, Consumer<UpdateDSL<UpdateModel>> consumer);

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    UpdateCompleter newIdUpdate(Collection<ID> ids, Consumer<UpdateDSL<UpdateModel>> consumer);

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    DeleteCompleter newIdDelete(ID id);

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    DeleteCompleter newIdDelete(Collection<ID> ids);


    /**
     * new dsl
     *
     * @param row domain
     * @param c   dsl
     * @return dsl
     */
    default UpdateDSL<UpdateModel> newUpdateDsl(E row, UpdateDSL<UpdateModel> c) {
        return c;
    }

    /**
     * new dsl
     *
     * @param row domain
     * @param c   dsl
     * @return dsl
     */
    default UpdateDSL<UpdateModel> newUpdateSelectiveDsl(E row, UpdateDSL<UpdateModel> c) {
        return c;
    }

    default void newPrimaryKey(E e, AbstractWhereDSL<?> where) {

    }

}
