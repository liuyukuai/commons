package com.cutefool.commons.job.core.job.domain;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.cutefool.commons.orm.BasicsDomain;

public class JobCycle extends BasicsDomain {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @NotBlank(message = "应用不能为空")
    @Size(max = 50, message = "应用不能超过50位")
    private String appId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @NotBlank(message = "名称不能为空")
    @Size(max = 100, message = "名称不能超过100位")
    private String name;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @NotBlank(message = "周期不能为空")
    @Size(max = 50, message = "周期不能超过50位")
    private String cycle;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @NotNull(message = "周期间隔不能为空")
    @Digits(integer = 19, fraction = 0, message = "周期间隔只能为数字")
    private Long duration;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @Size(max = 500, message = "自定义参数不能超过500位")
    private String parameters;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @Size(max = 500, message = "任务执行器不能超过500位")
    private String executor;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @Size(max = 500, message = "描述不能超过500位")
    private String description;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private LocalDateTime firstTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private LocalDateTime nextTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte isStop;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @Size(max = 200, message = "业务唯一标识不能超过200位")
    private String identifier;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @Size(max = 200, message = "任务类型不能超过200位")
    private String jobType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAppId() {
        return appId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getName() {
        return name;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getCycle() {
        return cycle;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCycle(String cycle) {
        this.cycle = cycle == null ? null : cycle.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getDuration() {
        return duration;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getParameters() {
        return parameters;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setParameters(String parameters) {
        this.parameters = parameters == null ? null : parameters.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getExecutor() {
        return executor;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExecutor(String executor) {
        this.executor = executor == null ? null : executor.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getDescription() {
        return description;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public LocalDateTime getFirstTime() {
        return firstTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setFirstTime(LocalDateTime firstTime) {
        this.firstTime = firstTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public LocalDateTime getNextTime() {
        return nextTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setNextTime(LocalDateTime nextTime) {
        this.nextTime = nextTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getIsStop() {
        return isStop;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIsStop(Byte isStop) {
        this.isStop = isStop;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getIdentifier() {
        return identifier;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setIdentifier(String identifier) {
        this.identifier = identifier == null ? null : identifier.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getJobType() {
        return jobType;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setJobType(String jobType) {
        this.jobType = jobType == null ? null : jobType.trim();
    }
}