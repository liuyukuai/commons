package com.cutefool.commons.job.core.job.domain;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.cutefool.commons.orm.BasicsDomain;

public class JobInstance extends BasicsDomain {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @NotNull(message = "jobId不能为空")
    @Digits(integer = 19, fraction = 0, message = "jobId只能为数字")
    private Long jobId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @NotBlank(message = "应用不能为空")
    @Size(max = 50, message = "应用不能超过50位")
    private String appId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private LocalDateTime triggerTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private LocalDateTime executeTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte state;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getJobId() {
        return jobId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAppId() {
        return appId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public LocalDateTime getTriggerTime() {
        return triggerTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTriggerTime(LocalDateTime triggerTime) {
        this.triggerTime = triggerTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public LocalDateTime getExecuteTime() {
        return executeTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecuteTime(LocalDateTime executeTime) {
        this.executeTime = executeTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getState() {
        return state;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setState(Byte state) {
        this.state = state;
    }
}