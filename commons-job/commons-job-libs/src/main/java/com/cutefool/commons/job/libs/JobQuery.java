/*
 *
 */
package com.cutefool.commons.job.libs;

import lombok.Data;

/**
 * @author 271007729@qq.com
 * @date 10/31/22 10:14 PM
 */
@Data
public class JobQuery {

    /**
     * 应用
     */
    private String appId;

    /**
     * 业务唯一标识
     */
    private String identifier;

    /**
     * 任务类型
     */
    private String jobType;
}