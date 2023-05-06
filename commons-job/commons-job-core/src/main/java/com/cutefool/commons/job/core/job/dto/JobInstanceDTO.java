package com.cutefool.commons.job.core.job.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 任务实例数据对象
 *
 * @author : 271007729@qq.com
 **/
@Data
public class JobInstanceDTO {
    /**
     * jobId
     **/
    @NotNull(message = "jobId不能为空")
    @Digits(integer = 19, fraction = 0, message = "jobId只能为数字")
    private Long jobId;

    /**
     * 应用
     **/
    @NotBlank(message = "应用不能为空")
    @Size(max = 50, message = "应用不能超过50位")
    private String appId;

    /**
     * 触发时间
     **/
    private java.time.LocalDateTime triggerTime;

    /**
     * 执行时间
     **/
    private java.time.LocalDateTime executeTime;

    /**
     * 是否终止 0：未终止1：已终止（不在执行）
     **/
    private Byte state;

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
}