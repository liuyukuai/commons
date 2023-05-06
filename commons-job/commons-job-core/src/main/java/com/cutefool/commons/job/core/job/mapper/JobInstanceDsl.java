package com.cutefool.commons.job.core.job.mapper;

import java.sql.JDBCType;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

@SuppressWarnings("all")
public final class JobInstanceDsl {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final JobInstance jobInstance = new JobInstance();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = jobInstance.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> jobId = jobInstance.jobId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> appId = jobInstance.appId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> triggerTime = jobInstance.triggerTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> executeTime = jobInstance.executeTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> state = jobInstance.state;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> createTime = jobInstance.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> creator = jobInstance.creator;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> reviseTime = jobInstance.reviseTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> reviser = jobInstance.reviser;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> tenantId = jobInstance.tenantId;

    public static final String FIELD_ID = "id";

    public static final String FIELD_JOB_ID = "jobId";

    public static final String FIELD_APP_ID = "appId";

    public static final String FIELD_TRIGGER_TIME = "triggerTime";

    public static final String FIELD_EXECUTE_TIME = "executeTime";

    public static final String FIELD_STATE = "state";

    public static final String FIELD_CREATE_TIME = "createTime";

    public static final String FIELD_CREATOR = "creator";

    public static final String FIELD_REVISE_TIME = "reviseTime";

    public static final String FIELD_REVISER = "reviser";

    public static final String FIELD_TENANT_ID = "tenantId";

    public static Map<String, SqlColumn<?>> columns() {
        Map<String, SqlColumn<?>> maps = new LinkedHashMap(11);
        maps.put("id", jobInstance.id);
        maps.put("jobId", jobInstance.jobId);
        maps.put("appId", jobInstance.appId);
        maps.put("triggerTime", jobInstance.triggerTime);
        maps.put("executeTime", jobInstance.executeTime);
        maps.put("state", jobInstance.state);
        maps.put("createTime", jobInstance.createTime);
        maps.put("creator", jobInstance.creator);
        maps.put("reviseTime", jobInstance.reviseTime);
        maps.put("reviser", jobInstance.reviser);
        maps.put("tenantId", jobInstance.tenantId);
        return maps;
    }

    public static Map<String, String> fields() {
        Map<String, String> maps = new LinkedHashMap(11);
        maps.put("id", "主键");
        maps.put("jobId", "jobId");
        maps.put("appId", "应用");
        maps.put("triggerTime", "触发时间");
        maps.put("executeTime", "执行时间");
        maps.put("state", "是否终止 0：未终止1：已终止（不在执行）");
        maps.put("createTime", "创建时间");
        maps.put("creator", "创建人");
        maps.put("reviseTime", "修改时间");
        maps.put("reviser", "修改人");
        return maps;
    }

    public static Map<String, String> mapping() {
        Map<String, String> maps = new LinkedHashMap(11);
        maps.put("id", "id");
        maps.put("job_id", "jobId");
        maps.put("app_id", "appId");
        maps.put("trigger_time", "triggerTime");
        maps.put("execute_time", "executeTime");
        maps.put("state", "state");
        maps.put("create_time", "createTime");
        maps.put("creator", "creator");
        maps.put("revise_time", "reviseTime");
        maps.put("reviser", "reviser");
        maps.put("tenant_id", "tenantId");
        return maps;
    }

    public static Map<String, String> excels() {
        Map<String, String> maps = new LinkedHashMap<>(11);
        maps.put("jobId", "jobId");
        maps.put("appId", "应用");
        maps.put("triggerTime", "触发时间");
        maps.put("executeTime", "执行时间");
        maps.put("state", "是否终止 0：未终止1：已终止（不在执行）");
        maps.put("createTime", "创建时间");
        maps.put("reviseTime", "修改时间");
        maps.put("orgName", "所属企业");
        return maps;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class JobInstance extends AliasableSqlTable<JobInstance> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<String> appId = column("app_id", JDBCType.VARCHAR);

        public final SqlColumn<LocalDateTime> triggerTime = column("trigger_time", JDBCType.TIMESTAMP);

        public final SqlColumn<LocalDateTime> executeTime = column("execute_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Byte> state = column("state", JDBCType.TINYINT);

        public final SqlColumn<LocalDateTime> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> creator = column("creator", JDBCType.BIGINT);

        public final SqlColumn<LocalDateTime> reviseTime = column("revise_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> reviser = column("reviser", JDBCType.BIGINT);

        public final SqlColumn<Long> tenantId = column("tenant_id", JDBCType.BIGINT);

        public JobInstance() {
            super("job_instance", JobInstance::new);
        }
    }
}