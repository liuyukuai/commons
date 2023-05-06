package com.cutefool.commons.job.core.job.mapper;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isIn;

import java.util.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import javax.annotation.Generated;

import com.cutefool.commons.job.core.job.domain.JobInstance;
import com.cutefool.commons.mybatis.dsl.*;
import com.cutefool.commons.core.util.Maps;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.insert.InsertDSL;
import org.mybatis.dynamic.sql.insert.MultiRowInsertDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;
import org.mybatis.dynamic.sql.where.AbstractWhereDSL;

@Mapper
@SuppressWarnings({"all"})
public interface JobInstanceMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<JobInstance>, CommonUpdateMapper, MybatisMapper<JobInstance, Long> {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(JobInstanceDsl.id, JobInstanceDsl.jobId, JobInstanceDsl.appId, JobInstanceDsl.triggerTime, JobInstanceDsl.executeTime, JobInstanceDsl.state, JobInstanceDsl.createTime, JobInstanceDsl.creator, JobInstanceDsl.reviseTime, JobInstanceDsl.reviser, JobInstanceDsl.tenantId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="JobInstanceResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="app_id", property="appId", jdbcType=JdbcType.VARCHAR),
        @Result(column="trigger_time", property="triggerTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="execute_time", property="executeTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="state", property="state", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="creator", property="creator", jdbcType=JdbcType.BIGINT),
        @Result(column="revise_time", property="reviseTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="reviser", property="reviser", jdbcType=JdbcType.BIGINT),
        @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT)
    })
    List<JobInstance> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("JobInstanceResult")
    Optional<JobInstance> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, JobInstanceDsl.jobInstance, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, JobInstanceDsl.jobInstance, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
         return delete(this.newDelete((c, where) ->
            c.where(JobInstanceDsl.id, isEqualTo(id_))
        ));
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(JobInstance row) {
        return MyBatis3Utils.insert(this::insert, row, JobInstanceDsl.jobInstance, c ->
            c.map(JobInstanceDsl.id).toProperty("id")
            .map(JobInstanceDsl.jobId).toProperty("jobId")
            .map(JobInstanceDsl.appId).toProperty("appId")
            .map(JobInstanceDsl.triggerTime).toProperty("triggerTime")
            .map(JobInstanceDsl.executeTime).toProperty("executeTime")
            .map(JobInstanceDsl.state).toProperty("state")
            .map(JobInstanceDsl.createTime).toProperty("createTime")
            .map(JobInstanceDsl.creator).toProperty("creator")
            .map(JobInstanceDsl.reviseTime).toProperty("reviseTime")
            .map(JobInstanceDsl.reviser).toProperty("reviser")
            .map(JobInstanceDsl.tenantId).toProperty("tenantId")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<JobInstance> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, JobInstanceDsl.jobInstance, c ->
            c.map(JobInstanceDsl.id).toProperty("id")
            .map(JobInstanceDsl.jobId).toProperty("jobId")
            .map(JobInstanceDsl.appId).toProperty("appId")
            .map(JobInstanceDsl.triggerTime).toProperty("triggerTime")
            .map(JobInstanceDsl.executeTime).toProperty("executeTime")
            .map(JobInstanceDsl.state).toProperty("state")
            .map(JobInstanceDsl.createTime).toProperty("createTime")
            .map(JobInstanceDsl.creator).toProperty("creator")
            .map(JobInstanceDsl.reviseTime).toProperty("reviseTime")
            .map(JobInstanceDsl.reviser).toProperty("reviser")
            .map(JobInstanceDsl.tenantId).toProperty("tenantId")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(JobInstance row) {
        return MyBatis3Utils.insert(this::insert, row, JobInstanceDsl.jobInstance, c ->
            c.map(JobInstanceDsl.id).toPropertyWhenPresent("id", row::getId)
            .map(JobInstanceDsl.jobId).toPropertyWhenPresent("jobId", row::getJobId)
            .map(JobInstanceDsl.appId).toPropertyWhenPresent("appId", row::getAppId)
            .map(JobInstanceDsl.triggerTime).toPropertyWhenPresent("triggerTime", row::getTriggerTime)
            .map(JobInstanceDsl.executeTime).toPropertyWhenPresent("executeTime", row::getExecuteTime)
            .map(JobInstanceDsl.state).toPropertyWhenPresent("state", row::getState)
            .map(JobInstanceDsl.createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
            .map(JobInstanceDsl.creator).toPropertyWhenPresent("creator", row::getCreator)
            .map(JobInstanceDsl.reviseTime).toPropertyWhenPresent("reviseTime", row::getReviseTime)
            .map(JobInstanceDsl.reviser).toPropertyWhenPresent("reviser", row::getReviser)
            .map(JobInstanceDsl.tenantId).toPropertyWhenPresent("tenantId", row::getTenantId)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobInstance> selectOne(SelectCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList(), JobInstanceDsl.jobInstance, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobInstance> select(SelectCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList(), JobInstanceDsl.jobInstance, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobInstance> selectDistinct(SelectCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList(), JobInstanceDsl.jobInstance, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobInstance> selectByPrimaryKey(Long id_) {
        return selectOne(this.newSelect((c, where) -> 
            c.where(JobInstanceDsl.id, isEqualTo(id_))
        ));
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateCompleter completer) {
        return MyBatis3Utils.update(this::update, JobInstanceDsl.jobInstance, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(JobInstance row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(JobInstanceDsl.id).equalTo(row::getId)
                .set(JobInstanceDsl.jobId).equalTo(row::getJobId)
                .set(JobInstanceDsl.appId).equalTo(row::getAppId)
                .set(JobInstanceDsl.triggerTime).equalTo(row::getTriggerTime)
                .set(JobInstanceDsl.executeTime).equalTo(row::getExecuteTime)
                .set(JobInstanceDsl.state).equalTo(row::getState)
                .set(JobInstanceDsl.createTime).equalTo(row::getCreateTime)
                .set(JobInstanceDsl.creator).equalTo(row::getCreator)
                .set(JobInstanceDsl.reviseTime).equalTo(row::getReviseTime)
                .set(JobInstanceDsl.reviser).equalTo(row::getReviser)
                .set(JobInstanceDsl.tenantId).equalTo(row::getTenantId);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobInstance row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(JobInstanceDsl.id).equalToWhenPresent(row::getId)
                .set(JobInstanceDsl.jobId).equalToWhenPresent(row::getJobId)
                .set(JobInstanceDsl.appId).equalToWhenPresent(row::getAppId)
                .set(JobInstanceDsl.triggerTime).equalToWhenPresent(row::getTriggerTime)
                .set(JobInstanceDsl.executeTime).equalToWhenPresent(row::getExecuteTime)
                .set(JobInstanceDsl.state).equalToWhenPresent(row::getState)
                .set(JobInstanceDsl.createTime).equalToWhenPresent(row::getCreateTime)
                .set(JobInstanceDsl.creator).equalToWhenPresent(row::getCreator)
                .set(JobInstanceDsl.reviseTime).equalToWhenPresent(row::getReviseTime)
                .set(JobInstanceDsl.reviser).equalToWhenPresent(row::getReviser)
                .set(JobInstanceDsl.tenantId).equalToWhenPresent(row::getTenantId);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(JobInstance row) {
         return update(this.newUpdate((c, where) ->
            c.set(JobInstanceDsl.jobId).equalTo(row::getJobId)
            .set(JobInstanceDsl.appId).equalTo(row::getAppId)
            .set(JobInstanceDsl.triggerTime).equalTo(row::getTriggerTime)
            .set(JobInstanceDsl.executeTime).equalTo(row::getExecuteTime)
            .set(JobInstanceDsl.state).equalTo(row::getState)
            .set(JobInstanceDsl.createTime).equalTo(row::getCreateTime)
            .set(JobInstanceDsl.creator).equalTo(row::getCreator)
            .set(JobInstanceDsl.reviseTime).equalTo(row::getReviseTime)
            .set(JobInstanceDsl.reviser).equalTo(row::getReviser)
            .set(JobInstanceDsl.tenantId).equalTo(row::getTenantId)
            .where(JobInstanceDsl.id, isEqualTo(row::getId))
        ));
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(JobInstance row) {
         return update(this.newUpdate((c, where) ->
            c.set(JobInstanceDsl.jobId).equalToWhenPresent(row::getJobId)
            .set(JobInstanceDsl.appId).equalToWhenPresent(row::getAppId)
            .set(JobInstanceDsl.triggerTime).equalToWhenPresent(row::getTriggerTime)
            .set(JobInstanceDsl.executeTime).equalToWhenPresent(row::getExecuteTime)
            .set(JobInstanceDsl.state).equalToWhenPresent(row::getState)
            .set(JobInstanceDsl.createTime).equalToWhenPresent(row::getCreateTime)
            .set(JobInstanceDsl.creator).equalToWhenPresent(row::getCreator)
            .set(JobInstanceDsl.reviseTime).equalToWhenPresent(row::getReviseTime)
            .set(JobInstanceDsl.reviser).equalToWhenPresent(row::getReviser)
            .set(JobInstanceDsl.tenantId).equalToWhenPresent(row::getTenantId)
            .where(JobInstanceDsl.id, isEqualTo(row::getId))
        ));
    }

    @Override
    default Map<String, SqlColumn<?>> columnsMap() {
        return JobInstanceDsl.columns();
    }

    @Override
    default Map<String, String> columnMapping() {
        return JobInstanceDsl.mapping();
    }

    @Override
    default Map<String, String> fieldsMap() {
        return JobInstanceDsl.fields();
    }

    @Override
    default Map<String, String> excelsMap() {
        return JobInstanceDsl.excels();
    }

    @Override
    default List<JobInstance> selectByPrimaryKey(Collection<Long> ids) {
        return this.select(this.newSelect((dsl, where) -> dsl.where(JobInstanceDsl.id, isIn(ids))));
    }

    @Override
    default int deleteByPrimaryKey(Collection<Long> ids) {
        return this.delete(this.newDelete((c, where) -> c.where(JobInstanceDsl.id, isIn(ids))));
    }

    @Override
    default String table() {
        return "job_instance";
    }

    @Override
    default String remarks() {
        return "任务实例";
    }

    @Override
    default AliasableSqlTable<?> sqlTable() {
        return JobInstanceDsl.jobInstance;
    }

    @Override
    default SqlColumn<Long> idColumn() {
        return JobInstanceDsl.id;
    }

    @Override
    default BasicColumn[] selectList() {
        Map<String, String> extensibleFields = this.extensibleFields();
        if (Maps.isEmpty(extensibleFields)) {
            return selectList;
        }
        BasicColumn[] columns = new BasicColumn[extensibleFields.size() + selectList.length];
        AtomicInteger index = new AtomicInteger(0);
        for (BasicColumn basicColumn : selectList) {
            columns[index.getAndIncrement()] = basicColumn;
        }
        Set<Map.Entry<String, String>> entries = extensibleFields.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            columns[index.getAndIncrement()] = SqlColumn.of(entry.getValue(), this.sqlTable());
        }
        return columns;
    }

    @Override
    default int insert(JobInstance row, Consumer<InsertDSL<JobInstance>> consumer) {
        return MyBatis3Utils.insert(this::insert, row, JobInstanceDsl.jobInstance, c -> {
            InsertDSL<JobInstance> insertDsl = this.doInsertDsl(c);
            if (Objects.nonNull(consumer)) {
                consumer.accept(insertDsl);
            }
            return insertDsl;
        });
    }

    @Override
    default int insertMultiple(Collection<JobInstance> records, Consumer<MultiRowInsertDSL<JobInstance>> consumer) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, JobInstanceDsl.jobInstance, c -> {
            MultiRowInsertDSL<JobInstance> insertDsl = this.doInsertMultipleDsl(c);
            if (Objects.nonNull(consumer)) {
                consumer.accept(insertDsl);
            }
            return insertDsl;
        });
    }

    @Override
    default int insertSelective(JobInstance row, Consumer<InsertDSL<JobInstance>> consumer) {
        return MyBatis3Utils.insert(this::insert, row, JobInstanceDsl.jobInstance, c -> {
            InsertDSL<JobInstance> insertDsl = this.doInsertSelectiveDsl(row, c);
            if (Objects.nonNull(consumer)) {
                consumer.accept(insertDsl);
            }
            return insertDsl;
        });
    }

    default void newPrimaryKey(JobInstance row, AbstractWhereDSL<?> where) {
        where.and(JobInstanceDsl.id, isEqualTo(row::getId));
    }

    default InsertDSL<JobInstance> doInsertDsl(InsertDSL<JobInstance> c) {
        return c.map(JobInstanceDsl.id).toProperty("id")
                .map(JobInstanceDsl.jobId).toProperty("jobId")
                .map(JobInstanceDsl.appId).toProperty("appId")
                .map(JobInstanceDsl.triggerTime).toProperty("triggerTime")
                .map(JobInstanceDsl.executeTime).toProperty("executeTime")
                .map(JobInstanceDsl.state).toProperty("state")
                .map(JobInstanceDsl.createTime).toProperty("createTime")
                .map(JobInstanceDsl.creator).toProperty("creator")
                .map(JobInstanceDsl.reviseTime).toProperty("reviseTime")
                .map(JobInstanceDsl.reviser).toProperty("reviser")
                .map(JobInstanceDsl.tenantId).toProperty("tenantId");
    }

    default InsertDSL<JobInstance> doInsertSelectiveDsl(JobInstance row, InsertDSL<JobInstance> c) {
        return c.map(JobInstanceDsl.id).toPropertyWhenPresent("id", row::getId)
                .map(JobInstanceDsl.jobId).toPropertyWhenPresent("jobId", row::getJobId)
                .map(JobInstanceDsl.appId).toPropertyWhenPresent("appId", row::getAppId)
                .map(JobInstanceDsl.triggerTime).toPropertyWhenPresent("triggerTime", row::getTriggerTime)
                .map(JobInstanceDsl.executeTime).toPropertyWhenPresent("executeTime", row::getExecuteTime)
                .map(JobInstanceDsl.state).toPropertyWhenPresent("state", row::getState)
                .map(JobInstanceDsl.createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
                .map(JobInstanceDsl.creator).toPropertyWhenPresent("creator", row::getCreator)
                .map(JobInstanceDsl.reviseTime).toPropertyWhenPresent("reviseTime", row::getReviseTime)
                .map(JobInstanceDsl.reviser).toPropertyWhenPresent("reviser", row::getReviser)
                .map(JobInstanceDsl.tenantId).toPropertyWhenPresent("tenantId", row::getTenantId);
    }

    default MultiRowInsertDSL<JobInstance> doInsertMultipleDsl(MultiRowInsertDSL<JobInstance> c) {
        return c.map(JobInstanceDsl.id).toProperty("id")
                .map(JobInstanceDsl.jobId).toProperty("jobId")
                .map(JobInstanceDsl.appId).toProperty("appId")
                .map(JobInstanceDsl.triggerTime).toProperty("triggerTime")
                .map(JobInstanceDsl.executeTime).toProperty("executeTime")
                .map(JobInstanceDsl.state).toProperty("state")
                .map(JobInstanceDsl.createTime).toProperty("createTime")
                .map(JobInstanceDsl.creator).toProperty("creator")
                .map(JobInstanceDsl.reviseTime).toProperty("reviseTime")
                .map(JobInstanceDsl.reviser).toProperty("reviser")
                .map(JobInstanceDsl.tenantId).toProperty("tenantId");
    }

    @Override
    default UpdateDSL<UpdateModel> newUpdateDsl(JobInstance row, UpdateDSL<UpdateModel> c) {
        return c.set(JobInstanceDsl.jobId).equalTo(row::getJobId)
                .set(JobInstanceDsl.appId).equalTo(row::getAppId)
                .set(JobInstanceDsl.triggerTime).equalTo(row::getTriggerTime)
                .set(JobInstanceDsl.executeTime).equalTo(row::getExecuteTime)
                .set(JobInstanceDsl.state).equalTo(row::getState)
                .set(JobInstanceDsl.createTime).equalTo(row::getCreateTime)
                .set(JobInstanceDsl.creator).equalTo(row::getCreator)
                .set(JobInstanceDsl.reviseTime).equalTo(row::getReviseTime)
                .set(JobInstanceDsl.reviser).equalTo(row::getReviser)
                .set(JobInstanceDsl.tenantId).equalTo(row::getTenantId);
    }

    @Override
    default UpdateDSL<UpdateModel> newUpdateSelectiveDsl(JobInstance row, UpdateDSL<UpdateModel> c) {
        return c.set(JobInstanceDsl.jobId).equalToWhenPresent(row::getJobId)
                .set(JobInstanceDsl.appId).equalToWhenPresent(row::getAppId)
                .set(JobInstanceDsl.triggerTime).equalToWhenPresent(row::getTriggerTime)
                .set(JobInstanceDsl.executeTime).equalToWhenPresent(row::getExecuteTime)
                .set(JobInstanceDsl.state).equalToWhenPresent(row::getState)
                .set(JobInstanceDsl.createTime).equalToWhenPresent(row::getCreateTime)
                .set(JobInstanceDsl.creator).equalToWhenPresent(row::getCreator)
                .set(JobInstanceDsl.reviseTime).equalToWhenPresent(row::getReviseTime)
                .set(JobInstanceDsl.reviser).equalToWhenPresent(row::getReviser)
                .set(JobInstanceDsl.tenantId).equalToWhenPresent(row::getTenantId);
    }
}