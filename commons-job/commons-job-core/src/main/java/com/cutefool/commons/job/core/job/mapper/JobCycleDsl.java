package com.cutefool.commons.job.core.job.mapper;

import java.sql.JDBCType;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

@SuppressWarnings("all")
public final class JobCycleDsl {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final JobCycle jobCycle = new JobCycle();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = jobCycle.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> appId = jobCycle.appId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> name = jobCycle.name;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> cycle = jobCycle.cycle;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> duration = jobCycle.duration;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> parameters = jobCycle.parameters;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> executor = jobCycle.executor;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> description = jobCycle.description;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> firstTime = jobCycle.firstTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> nextTime = jobCycle.nextTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> isStop = jobCycle.isStop;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> createTime = jobCycle.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> creator = jobCycle.creator;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> reviseTime = jobCycle.reviseTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> reviser = jobCycle.reviser;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> tenantId = jobCycle.tenantId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> identifier = jobCycle.identifier;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> jobType = jobCycle.jobType;

    public static final String FIELD_ID = "id";

    public static final String FIELD_APP_ID = "appId";

    public static final String FIELD_NAME = "name";

    public static final String FIELD_CYCLE = "cycle";

    public static final String FIELD_DURATION = "duration";

    public static final String FIELD_PARAMETERS = "parameters";

    public static final String FIELD_EXECUTOR = "executor";

    public static final String FIELD_DESCRIPTION = "description";

    public static final String FIELD_FIRST_TIME = "firstTime";

    public static final String FIELD_NEXT_TIME = "nextTime";

    public static final String FIELD_IS_STOP = "isStop";

    public static final String FIELD_CREATE_TIME = "createTime";

    public static final String FIELD_CREATOR = "creator";

    public static final String FIELD_REVISE_TIME = "reviseTime";

    public static final String FIELD_REVISER = "reviser";

    public static final String FIELD_TENANT_ID = "tenantId";

    public static final String FIELD_IDENTIFIER = "identifier";

    public static final String FIELD_JOB_TYPE = "jobType";

    public static Map<String, SqlColumn<?>> columns() {
        Map<String, SqlColumn<?>> maps = new LinkedHashMap(18);
        maps.put("id", jobCycle.id);
        maps.put("appId", jobCycle.appId);
        maps.put("name", jobCycle.name);
        maps.put("cycle", jobCycle.cycle);
        maps.put("duration", jobCycle.duration);
        maps.put("parameters", jobCycle.parameters);
        maps.put("executor", jobCycle.executor);
        maps.put("description", jobCycle.description);
        maps.put("firstTime", jobCycle.firstTime);
        maps.put("nextTime", jobCycle.nextTime);
        maps.put("isStop", jobCycle.isStop);
        maps.put("createTime", jobCycle.createTime);
        maps.put("creator", jobCycle.creator);
        maps.put("reviseTime", jobCycle.reviseTime);
        maps.put("reviser", jobCycle.reviser);
        maps.put("tenantId", jobCycle.tenantId);
        maps.put("identifier", jobCycle.identifier);
        maps.put("jobType", jobCycle.jobType);
        return maps;
    }

    public static Map<String, String> fields() {
        Map<String, String> maps = new LinkedHashMap(18);
        maps.put("id", "主键");
        maps.put("appId", "应用");
        maps.put("name", "名称");
        maps.put("cycle", "周期");
        maps.put("duration", "周期间隔");
        maps.put("parameters", "自定义参数");
        maps.put("executor", "任务执行器");
        maps.put("description", "描述");
        maps.put("firstTime", "第一次执行时间");
        maps.put("nextTime", "下一次执行时间");
        maps.put("isStop", "是否终止 0：未终止1：已终止（不在执行）");
        maps.put("createTime", "创建时间");
        maps.put("creator", "创建人");
        maps.put("reviseTime", "修改时间");
        maps.put("reviser", "修改人");
        maps.put("identifier", "业务唯一标识");
        maps.put("jobType", "任务类型");
        return maps;
    }

    public static Map<String, String> mapping() {
        Map<String, String> maps = new LinkedHashMap(18);
        maps.put("id", "id");
        maps.put("app_id", "appId");
        maps.put("name", "name");
        maps.put("cycle", "cycle");
        maps.put("duration", "duration");
        maps.put("parameters", "parameters");
        maps.put("executor", "executor");
        maps.put("description", "description");
        maps.put("first_time", "firstTime");
        maps.put("next_time", "nextTime");
        maps.put("is_stop", "isStop");
        maps.put("create_time", "createTime");
        maps.put("creator", "creator");
        maps.put("revise_time", "reviseTime");
        maps.put("reviser", "reviser");
        maps.put("tenant_id", "tenantId");
        maps.put("identifier", "identifier");
        maps.put("job_type", "jobType");
        return maps;
    }

    public static Map<String, String> excels() {
        Map<String, String> maps = new LinkedHashMap<>(18);
        maps.put("appId", "应用");
        maps.put("name", "名称");
        maps.put("cycle", "周期");
        maps.put("duration", "周期间隔");
        maps.put("parameters", "自定义参数");
        maps.put("executor", "任务执行器");
        maps.put("description", "描述");
        maps.put("firstTime", "第一次执行时间");
        maps.put("nextTime", "下一次执行时间");
        maps.put("isStop", "是否终止 0：未终止1：已终止（不在执行）");
        maps.put("createTime", "创建时间");
        maps.put("reviseTime", "修改时间");
        maps.put("identifier", "业务唯一标识");
        maps.put("jobType", "任务类型");
        maps.put("orgName", "所属企业");
        return maps;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class JobCycle extends AliasableSqlTable<JobCycle> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> appId = column("app_id", JDBCType.VARCHAR);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> cycle = column("cycle", JDBCType.VARCHAR);

        public final SqlColumn<Long> duration = column("duration", JDBCType.BIGINT);

        public final SqlColumn<String> parameters = column("parameters", JDBCType.VARCHAR);

        public final SqlColumn<String> executor = column("executor", JDBCType.VARCHAR);

        public final SqlColumn<String> description = column("description", JDBCType.VARCHAR);

        public final SqlColumn<LocalDateTime> firstTime = column("first_time", JDBCType.TIMESTAMP);

        public final SqlColumn<LocalDateTime> nextTime = column("next_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Byte> isStop = column("is_stop", JDBCType.TINYINT);

        public final SqlColumn<LocalDateTime> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> creator = column("creator", JDBCType.BIGINT);

        public final SqlColumn<LocalDateTime> reviseTime = column("revise_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> reviser = column("reviser", JDBCType.BIGINT);

        public final SqlColumn<Long> tenantId = column("tenant_id", JDBCType.BIGINT);

        public final SqlColumn<String> identifier = column("identifier", JDBCType.VARCHAR);

        public final SqlColumn<String> jobType = column("job_type", JDBCType.VARCHAR);

        public JobCycle() {
            super("job_cycle", JobCycle::new);
        }
    }
}