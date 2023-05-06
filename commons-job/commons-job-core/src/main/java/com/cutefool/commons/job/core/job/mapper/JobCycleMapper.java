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

import com.cutefool.commons.job.core.job.domain.JobCycle;
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
public interface JobCycleMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<JobCycle>, CommonUpdateMapper, MybatisMapper<JobCycle, Long> {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(JobCycleDsl.id, JobCycleDsl.appId, JobCycleDsl.name, JobCycleDsl.cycle, JobCycleDsl.duration, JobCycleDsl.parameters, JobCycleDsl.executor, JobCycleDsl.description, JobCycleDsl.firstTime, JobCycleDsl.nextTime, JobCycleDsl.isStop, JobCycleDsl.createTime, JobCycleDsl.creator, JobCycleDsl.reviseTime, JobCycleDsl.reviser, JobCycleDsl.tenantId, JobCycleDsl.identifier, JobCycleDsl.jobType);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "JobCycleResult", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "app_id", property = "appId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "cycle", property = "cycle", jdbcType = JdbcType.VARCHAR),
            @Result(column = "duration", property = "duration", jdbcType = JdbcType.BIGINT),
            @Result(column = "parameters", property = "parameters", jdbcType = JdbcType.VARCHAR),
            @Result(column = "executor", property = "executor", jdbcType = JdbcType.VARCHAR),
            @Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
            @Result(column = "first_time", property = "firstTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "next_time", property = "nextTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "is_stop", property = "isStop", jdbcType = JdbcType.TINYINT),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "creator", property = "creator", jdbcType = JdbcType.BIGINT),
            @Result(column = "revise_time", property = "reviseTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "reviser", property = "reviser", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "identifier", property = "identifier", jdbcType = JdbcType.VARCHAR),
            @Result(column = "job_type", property = "jobType", jdbcType = JdbcType.VARCHAR)
    })
    List<JobCycle> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultMap("JobCycleResult")
    Optional<JobCycle> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, JobCycleDsl.jobCycle, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, JobCycleDsl.jobCycle, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(this.newDelete((c, where) ->
                c.where(JobCycleDsl.id, isEqualTo(id_))
        ));
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(JobCycle row) {
        return MyBatis3Utils.insert(this::insert, row, JobCycleDsl.jobCycle, c ->
                c.map(JobCycleDsl.id).toProperty("id")
                        .map(JobCycleDsl.appId).toProperty("appId")
                        .map(JobCycleDsl.name).toProperty("name")
                        .map(JobCycleDsl.cycle).toProperty("cycle")
                        .map(JobCycleDsl.duration).toProperty("duration")
                        .map(JobCycleDsl.parameters).toProperty("parameters")
                        .map(JobCycleDsl.executor).toProperty("executor")
                        .map(JobCycleDsl.description).toProperty("description")
                        .map(JobCycleDsl.firstTime).toProperty("firstTime")
                        .map(JobCycleDsl.nextTime).toProperty("nextTime")
                        .map(JobCycleDsl.isStop).toProperty("isStop")
                        .map(JobCycleDsl.createTime).toProperty("createTime")
                        .map(JobCycleDsl.creator).toProperty("creator")
                        .map(JobCycleDsl.reviseTime).toProperty("reviseTime")
                        .map(JobCycleDsl.reviser).toProperty("reviser")
                        .map(JobCycleDsl.tenantId).toProperty("tenantId")
                        .map(JobCycleDsl.identifier).toProperty("identifier")
                        .map(JobCycleDsl.jobType).toProperty("jobType")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<JobCycle> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, JobCycleDsl.jobCycle, c ->
                c.map(JobCycleDsl.id).toProperty("id")
                        .map(JobCycleDsl.appId).toProperty("appId")
                        .map(JobCycleDsl.name).toProperty("name")
                        .map(JobCycleDsl.cycle).toProperty("cycle")
                        .map(JobCycleDsl.duration).toProperty("duration")
                        .map(JobCycleDsl.parameters).toProperty("parameters")
                        .map(JobCycleDsl.executor).toProperty("executor")
                        .map(JobCycleDsl.description).toProperty("description")
                        .map(JobCycleDsl.firstTime).toProperty("firstTime")
                        .map(JobCycleDsl.nextTime).toProperty("nextTime")
                        .map(JobCycleDsl.isStop).toProperty("isStop")
                        .map(JobCycleDsl.createTime).toProperty("createTime")
                        .map(JobCycleDsl.creator).toProperty("creator")
                        .map(JobCycleDsl.reviseTime).toProperty("reviseTime")
                        .map(JobCycleDsl.reviser).toProperty("reviser")
                        .map(JobCycleDsl.tenantId).toProperty("tenantId")
                        .map(JobCycleDsl.identifier).toProperty("identifier")
                        .map(JobCycleDsl.jobType).toProperty("jobType")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(JobCycle row) {
        return MyBatis3Utils.insert(this::insert, row, JobCycleDsl.jobCycle, c ->
                c.map(JobCycleDsl.id).toPropertyWhenPresent("id", row::getId)
                        .map(JobCycleDsl.appId).toPropertyWhenPresent("appId", row::getAppId)
                        .map(JobCycleDsl.name).toPropertyWhenPresent("name", row::getName)
                        .map(JobCycleDsl.cycle).toPropertyWhenPresent("cycle", row::getCycle)
                        .map(JobCycleDsl.duration).toPropertyWhenPresent("duration", row::getDuration)
                        .map(JobCycleDsl.parameters).toPropertyWhenPresent("parameters", row::getParameters)
                        .map(JobCycleDsl.executor).toPropertyWhenPresent("executor", row::getExecutor)
                        .map(JobCycleDsl.description).toPropertyWhenPresent("description", row::getDescription)
                        .map(JobCycleDsl.firstTime).toPropertyWhenPresent("firstTime", row::getFirstTime)
                        .map(JobCycleDsl.nextTime).toPropertyWhenPresent("nextTime", row::getNextTime)
                        .map(JobCycleDsl.isStop).toPropertyWhenPresent("isStop", row::getIsStop)
                        .map(JobCycleDsl.createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
                        .map(JobCycleDsl.creator).toPropertyWhenPresent("creator", row::getCreator)
                        .map(JobCycleDsl.reviseTime).toPropertyWhenPresent("reviseTime", row::getReviseTime)
                        .map(JobCycleDsl.reviser).toPropertyWhenPresent("reviser", row::getReviser)
                        .map(JobCycleDsl.tenantId).toPropertyWhenPresent("tenantId", row::getTenantId)
                        .map(JobCycleDsl.identifier).toPropertyWhenPresent("identifier", row::getIdentifier)
                        .map(JobCycleDsl.jobType).toPropertyWhenPresent("jobType", row::getJobType)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobCycle> selectOne(SelectCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList(), JobCycleDsl.jobCycle, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobCycle> select(SelectCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList(), JobCycleDsl.jobCycle, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<JobCycle> selectDistinct(SelectCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList(), JobCycleDsl.jobCycle, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<JobCycle> selectByPrimaryKey(Long id_) {
        return selectOne(this.newSelect((c, where) ->
                c.where(JobCycleDsl.id, isEqualTo(id_))
        ));
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateCompleter completer) {
        return MyBatis3Utils.update(this::update, JobCycleDsl.jobCycle, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(JobCycle row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(JobCycleDsl.id).equalTo(row::getId)
                .set(JobCycleDsl.appId).equalTo(row::getAppId)
                .set(JobCycleDsl.name).equalTo(row::getName)
                .set(JobCycleDsl.cycle).equalTo(row::getCycle)
                .set(JobCycleDsl.duration).equalTo(row::getDuration)
                .set(JobCycleDsl.parameters).equalTo(row::getParameters)
                .set(JobCycleDsl.executor).equalTo(row::getExecutor)
                .set(JobCycleDsl.description).equalTo(row::getDescription)
                .set(JobCycleDsl.firstTime).equalTo(row::getFirstTime)
                .set(JobCycleDsl.nextTime).equalTo(row::getNextTime)
                .set(JobCycleDsl.isStop).equalTo(row::getIsStop)
                .set(JobCycleDsl.createTime).equalTo(row::getCreateTime)
                .set(JobCycleDsl.creator).equalTo(row::getCreator)
                .set(JobCycleDsl.reviseTime).equalTo(row::getReviseTime)
                .set(JobCycleDsl.reviser).equalTo(row::getReviser)
                .set(JobCycleDsl.tenantId).equalTo(row::getTenantId)
                .set(JobCycleDsl.identifier).equalTo(row::getIdentifier)
                .set(JobCycleDsl.jobType).equalTo(row::getJobType);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(JobCycle row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(JobCycleDsl.id).equalToWhenPresent(row::getId)
                .set(JobCycleDsl.appId).equalToWhenPresent(row::getAppId)
                .set(JobCycleDsl.name).equalToWhenPresent(row::getName)
                .set(JobCycleDsl.cycle).equalToWhenPresent(row::getCycle)
                .set(JobCycleDsl.duration).equalToWhenPresent(row::getDuration)
                .set(JobCycleDsl.parameters).equalToWhenPresent(row::getParameters)
                .set(JobCycleDsl.executor).equalToWhenPresent(row::getExecutor)
                .set(JobCycleDsl.description).equalToWhenPresent(row::getDescription)
                .set(JobCycleDsl.firstTime).equalToWhenPresent(row::getFirstTime)
                .set(JobCycleDsl.nextTime).equalToWhenPresent(row::getNextTime)
                .set(JobCycleDsl.isStop).equalToWhenPresent(row::getIsStop)
                .set(JobCycleDsl.createTime).equalToWhenPresent(row::getCreateTime)
                .set(JobCycleDsl.creator).equalToWhenPresent(row::getCreator)
                .set(JobCycleDsl.reviseTime).equalToWhenPresent(row::getReviseTime)
                .set(JobCycleDsl.reviser).equalToWhenPresent(row::getReviser)
                .set(JobCycleDsl.tenantId).equalToWhenPresent(row::getTenantId)
                .set(JobCycleDsl.identifier).equalToWhenPresent(row::getIdentifier)
                .set(JobCycleDsl.jobType).equalToWhenPresent(row::getJobType);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(JobCycle row) {
        return update(this.newUpdate((c, where) ->
                c.set(JobCycleDsl.appId).equalTo(row::getAppId)
                        .set(JobCycleDsl.name).equalTo(row::getName)
                        .set(JobCycleDsl.cycle).equalTo(row::getCycle)
                        .set(JobCycleDsl.duration).equalTo(row::getDuration)
                        .set(JobCycleDsl.parameters).equalTo(row::getParameters)
                        .set(JobCycleDsl.executor).equalTo(row::getExecutor)
                        .set(JobCycleDsl.description).equalTo(row::getDescription)
                        .set(JobCycleDsl.firstTime).equalTo(row::getFirstTime)
                        .set(JobCycleDsl.nextTime).equalTo(row::getNextTime)
                        .set(JobCycleDsl.isStop).equalTo(row::getIsStop)
                        .set(JobCycleDsl.createTime).equalTo(row::getCreateTime)
                        .set(JobCycleDsl.creator).equalTo(row::getCreator)
                        .set(JobCycleDsl.reviseTime).equalTo(row::getReviseTime)
                        .set(JobCycleDsl.reviser).equalTo(row::getReviser)
                        .set(JobCycleDsl.tenantId).equalTo(row::getTenantId)
                        .set(JobCycleDsl.identifier).equalTo(row::getIdentifier)
                        .set(JobCycleDsl.jobType).equalTo(row::getJobType)
                        .where(JobCycleDsl.id, isEqualTo(row::getId))
        ));
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(JobCycle row) {
        return update(this.newUpdate((c, where) ->
                c.set(JobCycleDsl.appId).equalToWhenPresent(row::getAppId)
                        .set(JobCycleDsl.name).equalToWhenPresent(row::getName)
                        .set(JobCycleDsl.cycle).equalToWhenPresent(row::getCycle)
                        .set(JobCycleDsl.duration).equalToWhenPresent(row::getDuration)
                        .set(JobCycleDsl.parameters).equalToWhenPresent(row::getParameters)
                        .set(JobCycleDsl.executor).equalToWhenPresent(row::getExecutor)
                        .set(JobCycleDsl.description).equalToWhenPresent(row::getDescription)
                        .set(JobCycleDsl.firstTime).equalToWhenPresent(row::getFirstTime)
                        .set(JobCycleDsl.nextTime).equalToWhenPresent(row::getNextTime)
                        .set(JobCycleDsl.isStop).equalToWhenPresent(row::getIsStop)
                        .set(JobCycleDsl.createTime).equalToWhenPresent(row::getCreateTime)
                        .set(JobCycleDsl.creator).equalToWhenPresent(row::getCreator)
                        .set(JobCycleDsl.reviseTime).equalToWhenPresent(row::getReviseTime)
                        .set(JobCycleDsl.reviser).equalToWhenPresent(row::getReviser)
                        .set(JobCycleDsl.tenantId).equalToWhenPresent(row::getTenantId)
                        .set(JobCycleDsl.identifier).equalToWhenPresent(row::getIdentifier)
                        .set(JobCycleDsl.jobType).equalToWhenPresent(row::getJobType)
                        .where(JobCycleDsl.id, isEqualTo(row::getId))
        ));
    }

    @Override
    default Map<String, SqlColumn<?>> columnsMap() {
        return JobCycleDsl.columns();
    }

    @Override
    default Map<String, String> columnMapping() {
        return JobCycleDsl.mapping();
    }

    @Override
    default Map<String, String> fieldsMap() {
        return JobCycleDsl.fields();
    }

    @Override
    default Map<String, String> excelsMap() {
        return JobCycleDsl.excels();
    }

    @Override
    default List<JobCycle> selectByPrimaryKey(Collection<Long> ids) {
        return this.select(this.newSelect((dsl, where) -> dsl.where(JobCycleDsl.id, isIn(ids))));
    }

    @Override
    default int deleteByPrimaryKey(Collection<Long> ids) {
        return this.delete(this.newDelete((c, where) -> c.where(JobCycleDsl.id, isIn(ids))));
    }

    @Override
    default String table() {
        return "job_cycle";
    }

    @Override
    default String remarks() {
        return "周期配置任务";
    }

    @Override
    default AliasableSqlTable<?> sqlTable() {
        return JobCycleDsl.jobCycle;
    }

    @Override
    default SqlColumn<Long> idColumn() {
        return JobCycleDsl.id;
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
    default int insert(JobCycle row, Consumer<InsertDSL<JobCycle>> consumer) {
        return MyBatis3Utils.insert(this::insert, row, JobCycleDsl.jobCycle, c -> {
            InsertDSL<JobCycle> insertDsl = this.doInsertDsl(c);
            if (Objects.nonNull(consumer)) {
                consumer.accept(insertDsl);
            }
            return insertDsl;
        });
    }

    @Override
    default int insertMultiple(Collection<JobCycle> records, Consumer<MultiRowInsertDSL<JobCycle>> consumer) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, JobCycleDsl.jobCycle, c -> {
            MultiRowInsertDSL<JobCycle> insertDsl = this.doInsertMultipleDsl(c);
            if (Objects.nonNull(consumer)) {
                consumer.accept(insertDsl);
            }
            return insertDsl;
        });
    }

    @Override
    default int insertSelective(JobCycle row, Consumer<InsertDSL<JobCycle>> consumer) {
        return MyBatis3Utils.insert(this::insert, row, JobCycleDsl.jobCycle, c -> {
            InsertDSL<JobCycle> insertDsl = this.doInsertSelectiveDsl(row, c);
            if (Objects.nonNull(consumer)) {
                consumer.accept(insertDsl);
            }
            return insertDsl;
        });
    }

    default void newPrimaryKey(JobCycle row, AbstractWhereDSL<?> where) {
        where.and(JobCycleDsl.id, isEqualTo(row::getId));
    }

    default InsertDSL<JobCycle> doInsertDsl(InsertDSL<JobCycle> c) {
        return c.map(JobCycleDsl.id).toProperty("id")
                .map(JobCycleDsl.appId).toProperty("appId")
                .map(JobCycleDsl.name).toProperty("name")
                .map(JobCycleDsl.cycle).toProperty("cycle")
                .map(JobCycleDsl.duration).toProperty("duration")
                .map(JobCycleDsl.parameters).toProperty("parameters")
                .map(JobCycleDsl.executor).toProperty("executor")
                .map(JobCycleDsl.description).toProperty("description")
                .map(JobCycleDsl.firstTime).toProperty("firstTime")
                .map(JobCycleDsl.nextTime).toProperty("nextTime")
                .map(JobCycleDsl.isStop).toProperty("isStop")
                .map(JobCycleDsl.createTime).toProperty("createTime")
                .map(JobCycleDsl.creator).toProperty("creator")
                .map(JobCycleDsl.reviseTime).toProperty("reviseTime")
                .map(JobCycleDsl.reviser).toProperty("reviser")
                .map(JobCycleDsl.tenantId).toProperty("tenantId")
                .map(JobCycleDsl.identifier).toProperty("identifier")
                .map(JobCycleDsl.jobType).toProperty("jobType");
    }

    default InsertDSL<JobCycle> doInsertSelectiveDsl(JobCycle row, InsertDSL<JobCycle> c) {
        return c.map(JobCycleDsl.id).toPropertyWhenPresent("id", row::getId)
                .map(JobCycleDsl.appId).toPropertyWhenPresent("appId", row::getAppId)
                .map(JobCycleDsl.name).toPropertyWhenPresent("name", row::getName)
                .map(JobCycleDsl.cycle).toPropertyWhenPresent("cycle", row::getCycle)
                .map(JobCycleDsl.duration).toPropertyWhenPresent("duration", row::getDuration)
                .map(JobCycleDsl.parameters).toPropertyWhenPresent("parameters", row::getParameters)
                .map(JobCycleDsl.executor).toPropertyWhenPresent("executor", row::getExecutor)
                .map(JobCycleDsl.description).toPropertyWhenPresent("description", row::getDescription)
                .map(JobCycleDsl.firstTime).toPropertyWhenPresent("firstTime", row::getFirstTime)
                .map(JobCycleDsl.nextTime).toPropertyWhenPresent("nextTime", row::getNextTime)
                .map(JobCycleDsl.isStop).toPropertyWhenPresent("isStop", row::getIsStop)
                .map(JobCycleDsl.createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
                .map(JobCycleDsl.creator).toPropertyWhenPresent("creator", row::getCreator)
                .map(JobCycleDsl.reviseTime).toPropertyWhenPresent("reviseTime", row::getReviseTime)
                .map(JobCycleDsl.reviser).toPropertyWhenPresent("reviser", row::getReviser)
                .map(JobCycleDsl.tenantId).toPropertyWhenPresent("tenantId", row::getTenantId)
                .map(JobCycleDsl.identifier).toPropertyWhenPresent("identifier", row::getIdentifier)
                .map(JobCycleDsl.jobType).toPropertyWhenPresent("jobType", row::getJobType);
    }

    default MultiRowInsertDSL<JobCycle> doInsertMultipleDsl(MultiRowInsertDSL<JobCycle> c) {
        return c.map(JobCycleDsl.id).toProperty("id")
                .map(JobCycleDsl.appId).toProperty("appId")
                .map(JobCycleDsl.name).toProperty("name")
                .map(JobCycleDsl.cycle).toProperty("cycle")
                .map(JobCycleDsl.duration).toProperty("duration")
                .map(JobCycleDsl.parameters).toProperty("parameters")
                .map(JobCycleDsl.executor).toProperty("executor")
                .map(JobCycleDsl.description).toProperty("description")
                .map(JobCycleDsl.firstTime).toProperty("firstTime")
                .map(JobCycleDsl.nextTime).toProperty("nextTime")
                .map(JobCycleDsl.isStop).toProperty("isStop")
                .map(JobCycleDsl.createTime).toProperty("createTime")
                .map(JobCycleDsl.creator).toProperty("creator")
                .map(JobCycleDsl.reviseTime).toProperty("reviseTime")
                .map(JobCycleDsl.reviser).toProperty("reviser")
                .map(JobCycleDsl.tenantId).toProperty("tenantId")
                .map(JobCycleDsl.identifier).toProperty("identifier")
                .map(JobCycleDsl.jobType).toProperty("jobType");
    }

    @Override
    default UpdateDSL<UpdateModel> newUpdateDsl(JobCycle row, UpdateDSL<UpdateModel> c) {
        return c.set(JobCycleDsl.appId).equalTo(row::getAppId)
                .set(JobCycleDsl.name).equalTo(row::getName)
                .set(JobCycleDsl.cycle).equalTo(row::getCycle)
                .set(JobCycleDsl.duration).equalTo(row::getDuration)
                .set(JobCycleDsl.parameters).equalTo(row::getParameters)
                .set(JobCycleDsl.executor).equalTo(row::getExecutor)
                .set(JobCycleDsl.description).equalTo(row::getDescription)
                .set(JobCycleDsl.firstTime).equalTo(row::getFirstTime)
                .set(JobCycleDsl.nextTime).equalTo(row::getNextTime)
                .set(JobCycleDsl.isStop).equalTo(row::getIsStop)
                .set(JobCycleDsl.createTime).equalTo(row::getCreateTime)
                .set(JobCycleDsl.creator).equalTo(row::getCreator)
                .set(JobCycleDsl.reviseTime).equalTo(row::getReviseTime)
                .set(JobCycleDsl.reviser).equalTo(row::getReviser)
                .set(JobCycleDsl.tenantId).equalTo(row::getTenantId)
                .set(JobCycleDsl.identifier).equalTo(row::getIdentifier)
                .set(JobCycleDsl.jobType).equalTo(row::getJobType);
    }

    @Override
    default UpdateDSL<UpdateModel> newUpdateSelectiveDsl(JobCycle row, UpdateDSL<UpdateModel> c) {
        return c.set(JobCycleDsl.appId).equalToWhenPresent(row::getAppId)
                .set(JobCycleDsl.name).equalToWhenPresent(row::getName)
                .set(JobCycleDsl.cycle).equalToWhenPresent(row::getCycle)
                .set(JobCycleDsl.duration).equalToWhenPresent(row::getDuration)
                .set(JobCycleDsl.parameters).equalToWhenPresent(row::getParameters)
                .set(JobCycleDsl.executor).equalToWhenPresent(row::getExecutor)
                .set(JobCycleDsl.description).equalToWhenPresent(row::getDescription)
                .set(JobCycleDsl.firstTime).equalToWhenPresent(row::getFirstTime)
                .set(JobCycleDsl.nextTime).equalToWhenPresent(row::getNextTime)
                .set(JobCycleDsl.isStop).equalToWhenPresent(row::getIsStop)
                .set(JobCycleDsl.createTime).equalToWhenPresent(row::getCreateTime)
                .set(JobCycleDsl.creator).equalToWhenPresent(row::getCreator)
                .set(JobCycleDsl.reviseTime).equalToWhenPresent(row::getReviseTime)
                .set(JobCycleDsl.reviser).equalToWhenPresent(row::getReviser)
                .set(JobCycleDsl.tenantId).equalToWhenPresent(row::getTenantId)
                .set(JobCycleDsl.identifier).equalToWhenPresent(row::getIdentifier)
                .set(JobCycleDsl.jobType).equalToWhenPresent(row::getJobType);
    }
}