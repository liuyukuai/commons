/*
 *
 */
package com.cutefool.commons.job.core.executor;

import com.cutefool.commons.job.libs.JobsContext;

/**
 * 任务执行器
 *
 * @author 271007729@qq.com
 * @date 9/3/21 4:47 PM
 */
public interface JobExecutor {

    /**
     * 任务执行器
     *
     * @param context   context
     */
    void doExecute(JobsContext context);
}
