/*
 *
 */
package com.cutefool.commons.mybatis.dsl;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.Naming;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.Status;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Sort;
import com.cutefool.commons.core.page.Sorts;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.expression.Conditioning;
import com.cutefool.commons.expression.libs.Conditional;
import com.cutefool.commons.expression.libs.Conditionals;
import com.cutefool.commons.mybatis.dsl.utils.MybatisUtils;
import com.cutefool.commons.mybatis.dsl.utils.Restrictions;
import com.cutefool.commons.orm.ColumnLibs;
import com.cutefool.commons.orm.PageRequest;
import com.cutefool.commons.orm.RootDomain;
import com.cutefool.commons.orm.interfaces.IOperations;
import com.cutefool.commons.orm.interfaces.ISpecificationExecutor;
import com.cutefool.commons.orm.interfaces.RdsOperations;
import com.cutefool.commons.orm.rds.cache.context.RdsContext;
import com.cutefool.commons.orm.utils.Encrypts;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.SortSpecification;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.VisitableCondition;
import org.mybatis.dynamic.sql.delete.DeleteDSL;
import org.mybatis.dynamic.sql.delete.DeleteModel;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.CountDSL;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.where.AbstractWhereDSL;
import org.mybatis.dynamic.sql.where.WhereApplier;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 2022/9/20 4:15 PM
 */
@SuppressWarnings({"all"})
public interface MybatisOperations<DTO, E, ID, R extends MybatisMapper<E, ID>, Query extends Conditioning> extends IOperations<DTO, E, ID, R>, ISpecificationExecutor<E, Query>, MybatisSpecificationExecutor<E, Query>, MybatisCompleter<E, ID>, MybatisExtensible<DTO, E, ID, R>, RdsOperations, MybatisFieldsOperations<DTO, E, ID, R, Query>, Naming<ID> {

    /**
     * 获取 rds
     *
     * @return
     */
    RdsContext rdsContext();


    /**
     * 初始化条件
     *
     * @param where where
     */
    default void init(AbstractWhereDSL<?> where) {
    }

    /**
     * 是否排查删除状态的数据
     *
     * @return true or flase
     */
    default boolean withoutDeleted() {
        return true;
    }


    default List<Sort> defaultSorts() {
        return Collections.singletonList(new Sort(RootDomain.FIELD_ID, Sort.DESC, false));
    }


    default List<Sort> findSorts(Query query) {
        String sort = query.getSort();
        if (StringUtils.isNotBlank(sort)) {
            return Sorts.newInstance().sorts(sort);
        }
        return defaultSorts();
    }

    /**
     * 复合查询参数查询
     *
     * @param dsl   dsl
     * @param query 复合查询参数
     */
    default void newSelect(Query query, AbstractWhereDSL<?> where) {
    }

    /**
     * 复合查询参数查询
     *
     * @param where    where
     * @param query    复合查询参数
     * @param consumer consumer
     */
    default void newSelect(AbstractWhereDSL<?> where, Query query, Consumer<AbstractWhereDSL<?>> consumer) {
        Restrictions.of(where, this.getRepository().columnsMap()).where(query);
        if (this.withoutDeleted()) {
            SqlColumn<Byte> sqlColumn = this.sqlColumn(RootDomain.FIELD_DELETE_STATUS);
            if (Objects.nonNull(sqlColumn)) {
                where.and(sqlColumn, SqlBuilder.isEqualTo(Status.STATUS_FALSE));
            }
        }
        if (Objects.nonNull(consumer)) {
            consumer.accept(where);
        }
        this.newSelect(query, where);
    }


    /**
     * 复合查询参数查询
     *
     * @param dsl      dsl
     * @param consumer consumer
     */
    default void newSelect(AbstractWhereDSL<?> dsl, Consumer<AbstractWhereDSL<?>> consumer) {
        if (Objects.nonNull(consumer)) {
            consumer.accept(dsl);
        }
    }

    /**
     * 复合查询参数查询
     *
     * @param dsl          dsl
     * @param conditionals conditionals
     */
    default void newSelect(AbstractWhereDSL<?> dsl, Conditionals conditionals) {
        newSelect(dsl, conditionals, e -> {
        });
    }

    /**
     * 复合查询参数查询
     *
     * @param dsl          dsl
     * @param conditionals conditionals
     */
    default void newSelect(AbstractWhereDSL<?> dsl, Conditionals conditionals, Consumer<AbstractWhereDSL<?>> consumer) {
        Restrictions.of(dsl, this.getRepository().columnsMap()).where(conditionals);
        if (Objects.nonNull(consumer)) {
            consumer.accept(dsl);
        }
    }

    /**
     * 复合查询参数查询
     *
     * @param where       where
     * @param conditional conditional
     */
    default void newSelect(AbstractWhereDSL<?> where, Conditional conditional) {
        this.newSelect(where, conditional, e -> {
        });
    }

    /**
     * 复合查询参数查询
     *
     * @param where       where
     * @param conditional conditional
     */
    default void newSelect(AbstractWhereDSL<?> where, Conditional conditional, Consumer<AbstractWhereDSL<?>> consumer) {
        Restrictions.of(where, this.getRepository().columnsMap()).where(conditional);
        if (Objects.nonNull(consumer)) {
            consumer.accept(where);
        }
    }

    /**
     * 基础排序处理
     *
     * @param c     查询对象
     * @param sorts 排查集合
     */
    default void newSelect(QueryExpressionDSL<SelectModel> c, Collection<Sort> sorts) {
        c.orderBy(MybatisUtils.of(this.getRepository().columnsMap(), sorts));
    }


    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    @Override
    default SelectCompleter newSelect() {
        return newSelect(((dsl, where) -> {
        }));
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    @Override
    default SelectCompleter newSelect(BiConsumer<QueryExpressionDSL<SelectModel>, AbstractWhereDSL<?>> consumer) {
        return new SelectCompleter() {
            @Override
            public void where(QueryExpressionDSL<SelectModel> dsl, AbstractWhereDSL<?> where) {
                init(where);
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
    default SelectCompleter newSelect(Consumer<AbstractWhereDSL<?>> consumer) {
        return this.newSelect((dsl, where) -> this.newSelect(where, consumer));
    }

    /**
     * 通过名称查询
     *
     * @param name 名称
     * @return 查询对象
     */
    default SelectCompleter newSelect(String name) {
        return this.newSelect((dsl, where) -> this.newSelect(where, fieldName(), Operator.EQ, name));
    }

    ;

    /**
     * 属性名称
     *
     * @return 属性名称
     */

    default String fieldName() {
        throw new BizException("if yout want validate name , please override this method.");
    }

    /**
     * 排序查询
     *
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelect(Sort... sorts) {
        return this.newSelect(e -> {
        }, sorts);
    }

    /**
     * 排序查询
     *
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelect(Consumer<AbstractWhereDSL<?>> consumer, Sort... sorts) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(where, consumer);
            this.newSelect(dsl, Arrays.asList(sorts));
        });
    }

    /**
     * Conditional对象解析
     *
     * @param conditional Conditional
     * @return 查询对象
     */
    default SelectCompleter newSelect(Conditional conditional) {
        return this.newSelect(conditional, Lists.newArrayList(), e -> {
        });
    }

    /**
     * Conditional对象解析
     *
     * @param conditional Conditional
     * @return 查询对象
     */
    default SelectCompleter newSelect(Conditional conditional, Consumer<AbstractWhereDSL<?>> consumer) {
        return this.newSelect(conditional, Lists.newArrayList(), consumer);
    }

    /**
     * Conditional对象解析
     *
     * @param conditional Conditional
     * @param sorts       排序对象
     * @return 查询对象
     */
    default SelectCompleter newSelect(Conditional conditional, Collection<Sort> sorts) {
        return this.newSelect(conditional, sorts, e -> {
        });
    }

    /**
     * Conditional对象解析
     *
     * @param conditional Conditional
     * @param sorts       排序对象
     * @return 查询对象
     */
    default SelectCompleter newSelect(Conditional conditional, Collection<Sort> sorts, Consumer<AbstractWhereDSL<?>> consumer) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(where, conditional, consumer);
            this.newSelect(dsl, sorts);
        });
    }

    /**
     * Conditional对象解析
     *
     * @param conditionals conditionals
     * @return 查询对象
     */
    default SelectCompleter newSelect(Conditionals conditionals) {
        return this.newSelect(conditionals, Lists.newArrayList());
    }

    /**
     * Conditional对象解析
     *
     * @param conditionals conditionals
     * @return 查询对象
     */
    default SelectCompleter newSelect(Conditionals conditionals, Consumer<AbstractWhereDSL<?>> consumer) {
        return this.newSelect(conditionals, Lists.newArrayList(), consumer);
    }

    /**
     * Conditional对象解析
     *
     * @param conditionals conditionals
     * @param sorts        排序对象
     * @return 查询对象
     */
    default SelectCompleter newSelect(Conditionals conditionals, Collection<Sort> sorts) {
        return this.newSelect(conditionals, sorts, e -> {
        });
    }

    /**
     * Conditional对象解析
     *
     * @param conditionals conditionals
     * @param sorts        排序对象
     * @return 查询对象
     */
    default SelectCompleter newSelect(Conditionals conditionals, Collection<Sort> sorts, Consumer<AbstractWhereDSL<?>> consumer) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(where, conditionals, consumer);
            this.newSelect(dsl, sorts);
        });
    }

    /**
     * Conditional对象解析
     *
     * @param conditional conditionals
     * @return 查询对象
     */
    default CountCompleter newCount(Conditional conditional) {
        return this.newCount(conditional, e -> {
        });
    }

    /**
     * Conditional对象解析
     *
     * @param conditional conditionals
     * @return 查询对象
     */
    default CountCompleter newCount(Conditional conditional, Consumer<AbstractWhereDSL<?>> consumer) {
        return this.newCount((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(where, conditional, consumer);
        });
    }

    /**
     * Conditional对象解析
     *
     * @param conditional conditional
     * @return 查询对象
     */
    default CountCompleter newCount(Conditionals conditional) {
        return this.newCount(conditional, c -> {
        });
    }

    /**
     * Conditional对象解析
     *
     * @param conditional conditional
     * @return 查询对象
     */
    default CountCompleter newCount(Conditionals conditional, Consumer<AbstractWhereDSL<?>> consumer) {
        return this.newCount((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(where, conditional, consumer);
        });
    }


    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @return 查询对象
     */
    default SelectCompleter newSelect(Query query) {
        return this.newSelect(query, (e) -> {
        });
    }


    /**
     * 复合查询参数查询
     *
     * @param query    复合查询参数
     * @param consumer consumer
     * @return 查询对象
     */
    default SelectCompleter newSelect(Query query, Consumer<AbstractWhereDSL<?>> consumer) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(where, query, consumer);
        });
    }


    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelect(Query query, Collection<Sort> sorts) {
        return this.newSelect(query, e -> {
        }, sorts);
    }


    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelect(Query query, Sort... sorts) {
        return this.newSelect(query, e -> {
        }, sorts);
    }

    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelect(Query query, Consumer<AbstractWhereDSL<?>> consumer, Sort... sorts) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(dsl, Arrays.asList(sorts));
            this.newSelect(where, query, consumer);
        });
    }

    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelect(Query query, String sorts) {
        return this.newSelect(query, e -> {
        }, sorts);
    }


    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelect(Query query, Consumer<AbstractWhereDSL<?>> consumer, String sorts) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(dsl, Sorts.newInstance().sorts(sorts));
            this.newSelect(where, query, consumer);
        });
    }

    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelect(Query query, BiConsumer<QueryExpressionDSL<SelectModel>, AbstractWhereDSL<?>> consumer) {
        return this.newSelect((dsl, where) -> {
            consumer.accept(dsl, where);
        });
    }


    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelect(Query query, Consumer<AbstractWhereDSL<?>> consumer, Collection<Sort> sorts) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(dsl, sorts);
            this.newSelect(where, query, consumer);
        });
    }

    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @return 查询对象
     */
    default CountCompleter newCount(Query query) {
        return this.newCount(query, (e) -> {
        });
    }

    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @return 查询对象
     */
    default CountCompleter newCount(Query query, Consumer<AbstractWhereDSL<?>> consumer) {
        return this.newCount((dsl, where) -> this.newSelect(where, query, consumer));
    }


    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    @Override
    default CountCompleter newCount() {
        return new CountCompleter() {
            @Override
            protected void where(CountDSL<SelectModel> selectModelCountDSL, AbstractWhereDSL<?> where) {
                init(where);
            }
        };
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    @Override
    default CountCompleter newCount(BiConsumer<CountDSL<SelectModel>, AbstractWhereDSL<?>> consumer) {
        return new CountCompleter() {
            @Override
            protected void where(CountDSL<SelectModel> dsl, AbstractWhereDSL<?> where) {
                init(where);
                if (Objects.nonNull(consumer)) {
                    consumer.accept(dsl, where);
                }
            }
        };
    }

    /**
     * 复合查询参数查询
     *
     * @return 查询对象
     */
    default CountCompleter newCount(Consumer<AbstractWhereDSL<?>> consumer) {
        return this.newCount((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(where, consumer);
        });
    }

    /**
     * 复合查询参数查询
     *
     * @param name     字段名称
     * @param operator 操作
     */
    default void newSelect(AbstractWhereDSL<?> builder, String name, Operator operator, Object value) {
        Map<String, SqlColumn<?>> columnMap = this.getRepository().columnsMap();
        SqlColumn<?> byName = MybatisUtils.findByName(name, columnMap);
        if (Objects.nonNull(byName)) {
            VisitableCondition visitableCondition = MybatisUtils.initCondition(operator, value);
            builder.and(byName, visitableCondition);
        }
    }

    /**
     * 复合参数查询
     *
     * @param builder  条件构建
     * @param name     属性名称
     * @param operator 操作类型
     * @param value    值
     */
    default void newSelect(AbstractWhereDSL<?> builder, SqlColumn<?> name, Operator operator, Object value) {
        if (Objects.nonNull(name)) {
            VisitableCondition visitableCondition = MybatisUtils.initCondition(operator, value);
            builder.and(name, visitableCondition);
        }
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    @Override
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
    @Override
    default DeleteCompleter newIdDelete(ID id) {
        SqlColumn<ID> sqlColumn = this.idColumn();
        return new DeleteCompleter() {
            @Override
            public void where(DeleteDSL<DeleteModel> dsl, AbstractWhereDSL<?> where) {
                where.and(sqlColumn, SqlBuilder.isEqualTo(id));
            }
        };
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    @Override
    default DeleteCompleter newIdDelete(Collection<ID> ids) {
        SqlColumn<ID> sqlColumn = this.idColumn();
        return new DeleteCompleter() {
            @Override
            public void where(DeleteDSL<DeleteModel> dsl, AbstractWhereDSL<?> where) {
                where.and(sqlColumn, SqlBuilder.isIn(ids));
            }
        };
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    @Override
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
    @Override
    default UpdateCompleter newIdUpdate(ID id, Consumer<UpdateDSL<UpdateModel>> consumer) {
        return newUpdate((dsl, where) -> {
            where.and(this.idColumn(), SqlBuilder.isEqualTo(id));
            consumer.accept(dsl);
        });
    }

    /**
     * 基础查询对象
     *
     * @return 查询对象
     */
    @Override
    default UpdateCompleter newIdUpdate(Collection<ID> ids, Consumer<UpdateDSL<UpdateModel>> consumer) {
        return newUpdate((dsl, where) -> {
            where.and(idColumn(), SqlBuilder.isIn(ids));
            consumer.accept(dsl);
        });
    }


    /**
     * 执行分页查询
     *
     * @param pageRequest 分页参数
     * @param iSelect     数据查询执行器
     * @return 分页数据
     */
    default PageResponse<E> doPage(PageRequest pageRequest, ISelect iSelect) {
        try (Page<E> objects = PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize()).doSelectPage(iSelect)) {
            return MybatisUtils.apply(objects);
        } catch (Throwable e) {
            return PageResponse.empty();
        } finally {
            PageHelper.clearPage();
        }
    }


    @Transactional(rollbackFor = RuntimeException.class)
    default E updateAll(E e, WhereApplier whereApplier) {
        UpdateDSL<UpdateModel> update = SqlBuilder.update(this.getRepository().sqlTable());
        update = this.getRepository().updateAllColumnsDsl(e, update);
        // 处理动态属性
        this.doExtensible(e, update, this.extensibleFields());

        UpdateDSL<UpdateModel>.UpdateWhereBuilder where = update.where();
        this.init(where);
        UpdateStatementProvider render = where
                .applyWhere(whereApplier)
                .build()
                .render(RenderingStrategies.MYBATIS3);
        this.getRepository().update(render);
        return e;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    default E updateAll(E e, Consumer<AbstractWhereDSL<?>> consumer) {
        this.getRepository().update(this.newUpdate((dsl, where) -> {
            UpdateDSL<UpdateModel> update = this.getRepository().updateAllColumnsDsl(e, dsl);
            // 处理动态属性
            this.doExtensible(e, update, this.extensibleFields());
            consumer.accept(where);
        }));
        return e;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    default E updateSelective(E e, WhereApplier whereApplier) {
        UpdateDSL<UpdateModel> update = SqlBuilder.update(this.getRepository().sqlTable());
        update = this.getRepository().updateSelectiveColumnsDsl(e, update);
        UpdateDSL<UpdateModel>.UpdateWhereBuilder where = update.where();
        // 处理动态属性
        this.doExtensiblePresent(e, update, this.extensibleFields());
        this.init(where);
        UpdateStatementProvider render = where
                .applyWhere(whereApplier)
                .build()
                .render(RenderingStrategies.MYBATIS3);
        this.getRepository().update(render);
        return e;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    default E updateSelective(E e, Consumer<AbstractWhereDSL<?>> consumer) {
        this.getRepository().update(this.newUpdate((dsl, where) -> {
            UpdateDSL<UpdateModel> update = this.getRepository().updateSelectiveColumnsDsl(e, dsl);
            // 处理动态属性
            this.doExtensiblePresent(e, update, this.extensibleFields());
            consumer.accept(where);
        }));
        return e;
    }


    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelectSp(SortSpecification... sorts) {
        return this.newSelectSp(e -> {
        }, sorts);
    }

    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelectSp(Collection<SortSpecification> sorts) {
        return this.newSelectSp(e -> {
        }, sorts);
    }

    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelectSp(Query query, Collection<SortSpecification> sorts) {
        return this.newSelectSp(query, e -> {
        }, sorts);
    }

    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelectSp(Query query, SortSpecification... sorts) {
        return this.newSelectSp(query, e -> {
        }, sorts);
    }


    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelectSp(Query query, Consumer<AbstractWhereDSL<?>> consumer, Collection<SortSpecification> sorts) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelectSp(dsl, sorts);
            this.newSelect(where, query, consumer);
        });
    }

    /**
     * 复合查询参数查询
     *
     * @param query 复合查询参数
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelectSp(Query query, Consumer<AbstractWhereDSL<?>> consumer, SortSpecification... sorts) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelectSp(dsl, sorts);
            this.newSelect(where, query, consumer);
        });
    }

    /**
     * 排序查询
     *
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelectSp(Consumer<AbstractWhereDSL<?>> consumer, SortSpecification... sorts) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(where, consumer);
            this.newSelectSp(dsl, Arrays.asList(sorts));
        });
    }


    /**
     * 排序查询
     *
     * @param sorts sorts
     * @return 查询对象
     */
    default SelectCompleter newSelectSp(Consumer<AbstractWhereDSL<?>> consumer, Collection<SortSpecification> sorts) {
        return this.newSelect((dsl, where) -> {
            // 处理多条件查询
            this.newSelect(where, consumer);
            this.newSelectSp(dsl, sorts);
        });
    }

    /**
     * 基础排序处理
     *
     * @param c     查询对象
     * @param sorts 排查集合
     */
    default void newSelectSp(QueryExpressionDSL<SelectModel> c, Collection<SortSpecification> sorts) {
        c.orderBy(sorts);
    }

    /**
     * 基础排序处理
     *
     * @param c     查询对象
     * @param sorts 排查集合
     */
    default void newSelectSp(QueryExpressionDSL<SelectModel> c, SortSpecification... sorts) {
        c.orderBy(sorts);
    }


    /**
     * 查询所有的对象
     *
     * @param sorts sorts
     * @return list
     */
    default List<E> listSp(SortSpecification... sorts) {
        return this.getRepository().select(this.newSelectSp(sorts));
    }

    /**
     * 查询所有的对象
     *
     * @param sorts sorts
     * @return list
     */
    default List<E> listSp(Collection<SortSpecification> sorts) {
        return this.getRepository().select(this.newSelectSp(sorts));
    }

    ;

    @Override
    default List<E> listSp(WhereApplier whereApplier, SortSpecification... sorts) {
        return this.getRepository().select(this.newSelectSp(where -> {
            if (Objects.nonNull(whereApplier)) {
                where.applyWhere(whereApplier);
            }
        }, sorts));
    }

    ;

    @Override
    default List<E> listSp(Consumer<AbstractWhereDSL<?>> consumer, SortSpecification... sorts) {
        return this.getRepository().select(this.newSelectSp(where -> {
            if (Objects.nonNull(consumer)) {
                consumer.accept(where);
            }
        }, sorts));
    }

    @Override
    default List<E> listSp(WhereApplier whereApplier, Collection<SortSpecification> sorts) {
        return this.getRepository().select(this.newSelectSp(where -> {
            if (Objects.nonNull(whereApplier)) {
                where.applyWhere(whereApplier);
            }
        }, sorts));

    }

    @Override
    default List<E> listSp(Consumer<AbstractWhereDSL<?>> consumer, Collection<SortSpecification> sorts) {
        return this.getRepository().select(this.newSelectSp(where -> {
            if (Objects.nonNull(consumer)) {
                consumer.accept(where);
            }
        }, sorts));
    }

    ;

    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @param sorts sorts
     * @return list
     */
    default List<E> listBySpWhere(Query query, SortSpecification... sorts) {
        return this.getRepository().select(this.newSelectSp(query, sorts));
    }

    ;

    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @param sorts sorts
     * @return list
     */
    default List<E> listBySpWhere(Query query, Collection<SortSpecification> sorts) {
        return this.getRepository().select(this.newSelectSp(query, sorts));
    }


    /**
     * 获取所有属性
     *
     * @return 属性集合
     */
    @Override
    default Map<String, String> fieldsRemarks() {
        return this.fieldsRemarks(null);
    }

    @Override
    default Map<String, String> extensibleFields() {
        // 查询额外的属性映射
        String table = this.getRepository().table();
        // 查询所有的字段
        List<ColumnLibs> columnLibs = this.rdsContext().columnsDefault(table);
        // 转数据库类型映射
        return this.getRepository().extensibleFields(columnLibs);
    }

    @Override
    default List<ColumnLibs> listColumns() {
        // 查询额外的属性映射
        String table = this.getRepository().table();
        // 查询所有的字段
        return this.rdsContext().columnsDefault(table);
    }

    /**
     * 获取所有属性（带过滤）
     *
     * @return 属性集合
     */
    @Override
    default Map<String, String> fieldsRemarks(Predicate<Map.Entry<String, String>> predicate) {
        if (Objects.isNull(predicate)) {
            return this.getRepository().fieldsMap();
        }
        return this.getRepository().fieldsMap().entrySet().stream().filter(predicate).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    default Map<String, String> excelsRemarks() {
        return this.getRepository().excelsMap();
    }

    ;

    /**
     * 数据库表名
     *
     * @return 数据库表名
     */
    @Override
    default String table() {
        return Encrypts.encode(this.getRepository().table());
    }

    /**
     * 获取表注释
     *
     * @return 表名称
     */
    @Override
    default String remarks() {
        return this.getRepository().remarks();
    }

    @Override
    default String name() {
        return this.getClass().getName();
    }
}
