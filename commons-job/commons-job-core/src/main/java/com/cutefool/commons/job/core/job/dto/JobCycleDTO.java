package com.cutefool.commons.job.core.job.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 周期配置任务数据对象
 *
 * @author : 271007729@qq.com
 **/
@Data
public class JobCycleDTO {
    /**
     * 应用
     **/
    @NotBlank(message = "应用不能为空")
    @Size(max = 50, message = "应用不能超过50位")
    private String appId;

    /**
     * 名称
     **/
    @NotBlank(message = "名称不能为空")
    @Size(max = 100, message = "名称不能超过100位")
    private String name;

    /**
     * 周期
     **/
    @NotBlank(message = "周期不能为空")
    @Size(max = 50, message = "周期不能超过50位")
    private String cycle;

    /**
     * 周期间隔
     **/
    @NotNull(message = "周期间隔不能为空")
    @Digits(integer = 19, fraction = 0, message = "周期间隔只能为数字")
    private Long duration;

    /**
     * 自定义参数
     **/
    @Size(max = 500, message = "自定义参数不能超过500位")
    private String parameters;

    /**
     * 任务执行器
     **/
    @Size(max = 500, message = "任务执行器不能超过500位")
    private String executor;

    /**
     * 描述
     **/
    @Size(max = 500, message = "描述不能超过500位")
    private String description;

    /**
     * 第一次执行时间
     **/
    private java.time.LocalDateTime firstTime;

    /**
     * 下一次执行时间
     **/
    private java.time.LocalDateTime nextTime;

    /**
     * 是否终止 0：未终止1：已终止（不在执行）
     **/
    private Byte isStop;

    /**
     * 创建时间
     **/
    private java.time.LocalDateTime createTime;

    /**
     * 创建人
     **/
    @Digits(integer = 19, fraction = 0, message = "创建人只能为数字")
    private Long creator;

    /**
     * 修改时间
     **/
    private java.time.LocalDateTime reviseTime;

    /**
     * 修改人
     **/
    @Digits(integer = 19, fraction = 0, message = "修改人只能为数字")
    private Long reviser;

    /**
     * 业务唯一标识
     **/
    @Size(max = 200, message = "业务唯一标识不能超过200位")
    private String identifier;

    /**
     * 任务类型
     **/
    @Size(max = 200, message = "任务类型不能超过200位")
    private String jobType;
}