package com.cutefool.commons.mybatis.dsl;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.mybatis.dsl.utils.MybatisUtils;
import com.cutefool.commons.orm.ColumnLibs;
import com.cutefool.commons.orm.ExtensibleOperations;
import com.cutefool.commons.orm.rds.cache.context.RdsContext;
import com.cutefool.commons.spring.SpiSpringContext;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.insert.InsertDSL;
import org.mybatis.dynamic.sql.insert.MultiRowInsertDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.mybatis3.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public interface MybatisMapper<E, ID> extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<E>, CommonUpdateMapper, MybatisCompleter<E, ID>, ExtensibleOperations {

    /**
     * 数据库表名
     *
     * @return 数据库表名
     */
    String table();

    /**
     * 获取表注释
     *
     * @return 表名称
     */
    String remarks();

    /**
     * 获取表（SqlTable)
     *
     * @return 获取表
     */
    AliasableSqlTable<?> sqlTable();

    /**
     * 获取主键（只支持单个主键）
     *
     * @return 获取表
     */
    default SqlColumn<ID> idColumn() {
        throw new BizException("please override this method");
    }

    /**
     * 所有字段列集合
     *
     * @return map
     */
    Map<String, SqlColumn<?>> columnsMap();

    /**
     * 所有字段列集合 (数据库字段到java类型映射）
     *
     * @return map
     */
    default Map<String, String> columnMapping() {
        return Maps.hashMap();
    }

    /**
     * 所有属性集合
     *
     * @return 属性集合
     */
    Map<String, String> fieldsMap();

    /**
     * 所有属性集合
     *
     * @return 属性集合
     */
    Map<String, String> excelsMap();

    /**
     * 查询多条数据
     *
     * @param selectStatement selectStatement
     * @return 数据集合
     */
    List<E> selectMany(SelectStatementProvider selectStatement);

    /**
     * 查询一个对象
     *
     * @param selectStatement selectStatement
     * @return Optional
     */
    Optional<E> selectOne(SelectStatementProvider selectStatement);

    /**
     * 统计总数
     *
     * @param completer completer
     * @return long
     */
    long count(CountCompleter completer);

    /**
     * 删除数据
     *
     * @param completer completer
     * @return int
     */
    int delete(DeleteCompleter completer);

    /**
     * 删除数据（主键）
     *
     * @param id id
     * @return int
     */
    int deleteByPrimaryKey(ID id);

    /**
     * 删除数据（主键）
     *
     * @param ids ids
     * @return int
     */
    int deleteByPrimaryKey(Collection<ID> ids);

    /**
     * 新增数据
     *
     * @param row 数据
     * @return int
     */
    int insert(E row);

    /**
     * 新增数据
     *
     * @param row 数据
     * @return int
     */
    int insert(E row, Consumer<InsertDSL<E>> consumer);


    /**
     * 新增数据
     *
     * @param records 数据
     * @return int
     */
    int insertMultiple(Collection<E> records);

    /**
     * 新增数据
     *
     * @param records 数据
     * @return int
     */
    int insertMultiple(Collection<E> records, Consumer<MultiRowInsertDSL<E>> consumer);

    /**
     * 新增数据
     *
     * @param row 数据
     * @return int
     */
    int insertSelective(E row);

    /**
     * 新增数据
     *
     * @param row 数据
     * @return int
     */
    int insertSelective(E row, Consumer<InsertDSL<E>> consumer);

    /**
     * 查询一个对象
     *
     * @param completer completer
     * @return Optional
     */
    Optional<E> selectOne(SelectCompleter completer);

    /**
     * 查询数据
     *
     * @param completer completer
     * @return Optional
     */
    List<E> select(SelectCompleter completer);


    /**
     * 查询数据
     *
     * @param completer completer
     * @return Optional
     */
    default List<E> select(SelectCompleter completer, String selects) {
        return this.select(completer, this.doSelects(selects));
    }

    /**
     * 查询数据
     *
     * @param completer completer
     * @return Optional
     */
    default List<E> select(SelectCompleter completer, List<String> selects) {
        return this.select(completer, this.doSelects(selects));
    }


    /**
     * 查询数据
     *
     * @param completer completer
     * @return Optional
     */
    default List<E> select(SelectCompleter completer, SqlColumn<?>... selects) {
        if (Lists.isEmpty(selects)) {
            return this.select(completer);
        }
        int length = selects.length;
        // 如果只有一个属性
        if (length == 0) {
            return MyBatis3Utils.selectDistinct(this::selectMany, selects, sqlTable(), completer);
        }
        return MyBatis3Utils.selectList(this::selectMany, selects, sqlTable(), completer);
    }

    /**
     * 解析字段
     *
     * @param selects 要查询的字段
     * @param <T>     类型
     * @return 字段集合
     */
    @SuppressWarnings("all")
    default <T> SqlColumn<?>[] doSelects(List<String> selects) {
        Map<String, SqlColumn<?>> columnMap = this.columnsMap();
        if (Lists.isEmpty(selects)) {
            return null;
        }
        SqlColumn<?>[] sqlColumns = new SqlColumn[selects.size()];
        for (int i = 0; i < selects.size(); i++) {
            SqlColumn<?> byName = MybatisUtils.findByName(selects.get(i), columnMap);
            if (Objects.nonNull(byName)) {
                sqlColumns[i] = byName;
            }
        }
        return sqlColumns;
    }

    /**
     * 解析字段
     *
     * @param selects 要查询的字段
     * @param <T>     类型
     * @return 字段集合
     */
    @SuppressWarnings("all")
    default <T> SqlColumn<?>[] doSelects(String... selects) {
        Map<String, SqlColumn<?>> columnMap = this.columnsMap();
        if (Lists.isEmpty(selects)) {
            return null;
        }
        SqlColumn<?>[] sqlColumns = new SqlColumn[selects.length];
        for (int i = 0; i < selects.length; i++) {
            SqlColumn<?> byName = MybatisUtils.findByName(selects[i], columnMap);
            if (Objects.nonNull(byName)) {
                sqlColumns[i] = byName;
            }
        }
        return sqlColumns;
    }



    /**
     * 解析字段
     *
     * @param selects 要查询的字段
     * @param <T>     类型
     * @return 字段集合
     */
    @SuppressWarnings("all")
    default <T> SqlColumn<?>[] doSelects(String selects) {
      return doSelects(Strings.split(selects));
    }

    /**
     * 查询数据（去重）
     *
     * @param completer completer
     * @return Optional
     */
    List<E> selectDistinct(SelectCompleter completer);

    /**
     * 查询数据（主键）
     *
     * @param id id
     * @return Optional
     */
    Optional<E> selectByPrimaryKey(ID id);

    /**
     * 查询数据（主键）
     *
     * @param ids ids
     * @return list
     */
    List<E> selectByPrimaryKey(Collection<ID> ids);

    /**
     * 更新数据
     *
     * @param completer completer
     * @return int
     */
    int update(UpdateCompleter completer);

    /**
     * 更新数据
     *
     * @param completer
     * @return dsl
     */
    default UpdateDSL<UpdateModel> updateAllColumnsDsl(E row, UpdateDSL<UpdateModel> dsl) {
        return dsl;
    }

    /**
     * 更新数据
     *
     * @param completer
     * @return dsl
     */
    default UpdateDSL<UpdateModel> updateSelectiveColumnsDsl(E row, UpdateDSL<UpdateModel> dsl) {
        return dsl;
    }

    /**
     * 更新数据
     *
     * @param row row
     * @return int
     */
    int updateByPrimaryKey(E row);


    /**
     * 更新数据
     *
     * @param row      row
     * @param consumer consumer
     * @return int
     */
    default int updateByPrimaryKey(E row, Consumer<UpdateDSL<UpdateModel>> consumer) {
        return update(this.newUpdate((c, where) -> {
            this.newUpdateDsl(row, c);
            if (Objects.nonNull(consumer)) {
                consumer.accept(c);
            }
            this.newPrimaryKey(row, where);
        }));
    }

    /**
     * 更新数据
     *
     * @param row row
     * @return int
     */
    int updateByPrimaryKeySelective(E row);

    /**
     * 更新数据
     *
     * @param row      row
     * @param consumer consumer
     * @return int
     */
    default int updateByPrimaryKeySelective(E row, Consumer<UpdateDSL<UpdateModel>> consumer) {
        return update(this.newUpdate((c, where) -> {
            UpdateDSL<UpdateModel> dsl = this.newUpdateSelectiveDsl(row, c);
            if (Objects.nonNull(consumer)) {
                consumer.accept(dsl);
            }
            this.newPrimaryKey(row, where);
        }));
    }

    @Override
    default Map<String, String> extensibleFields() {
        RdsContext rdsContext = SpiSpringContext.getOneSpi(RdsContext.class);
        if (Objects.isNull(rdsContext)) {
            return Maps.hashMap();
        }
        return extensibleFields(rdsContext.columnsDefault(this.table()));
    }

    default Map<String, String> extensibleFields(List<ColumnLibs> columnLibs) {
        // 查询额外的属性映射
        // 转数据库类型映射
        Map<String, String> dbColumnMap = this.columnMapping();
        // 兼容历史版本逻辑（默认实现为空map）
        if (Maps.isEmpty(dbColumnMap)) {
            return Maps.hashMap();
        }
        return Lists.empty(columnLibs)
                .stream()
                .filter(e -> !dbColumnMap.containsKey(e.getName())).collect(Collectors.toMap(e -> Strings.camel(e.getName()), e -> e.getName()));
    }

    default BasicColumn[] selectList() {
        return new BasicColumn[0];
    }
}
