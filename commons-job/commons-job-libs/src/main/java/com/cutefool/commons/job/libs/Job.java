/*
 *
 */
package com.cutefool.commons.job.libs;

import lombok.Data;

/**
 * 任务配置
 *
 * @author 271007729@qq.com
 * @date 9/6/21 11:27 AM
 */
@Data
public class Job {

    /**
     * id
     */
    private Long id;

    /**
     * 任务名称
     */
    private String name;
    /**
     * 唯一标识
     */
    private String identifier;

    /**
     * 任务类型
     */
    private String jobType;

    /**
     * 应用
     */
    private String appId;

    /**
     * 任务执行器
     */
    private String executor;

    /**
     * 自定义参数
     */
    private String parameters;

    /**
     * 描述
     */
    private String description;

    /**
     * 租户
     */
    private Long tenantId;
}
