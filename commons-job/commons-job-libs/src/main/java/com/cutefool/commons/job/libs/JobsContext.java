/*
 *  
 */
package com.cutefool.commons.job.libs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务执行上下文
 *
 * @author 271007729@qq.com
 * @date 9/3/21 5:39 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsContext {

    private Long jobId;
    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务实例ID
     */
    private Long instanceId;

    /**
     * 分片总数
     */
    private Long shardingCount;

    /**
     * 当前分区
     */
    private Long shardingItem;

    /**
     * 自定义参数
     */
    private String parameters;
}
