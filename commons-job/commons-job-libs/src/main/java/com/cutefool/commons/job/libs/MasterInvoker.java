/*
 *
 */
package com.cutefool.commons.job.libs;

import com.cutefool.commons.job.libs.cycle.CycleJobs;

/**
 * worker执行器
 *
 * @author 271007729@qq.com
 * @date 9/15/21 4:02 PM
 */
public interface MasterInvoker extends JobsInvoker {

    /**
     * 创建任务
     *
     * @param jobs jobs
     * @return jobId
     */
    Long create(CycleJobs jobs);

}
