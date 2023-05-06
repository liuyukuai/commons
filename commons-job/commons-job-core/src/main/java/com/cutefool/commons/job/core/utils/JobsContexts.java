/*
 *
 */
package com.cutefool.commons.job.core.utils;

import com.cutefool.commons.job.core.job.domain.JobCycle;
import com.cutefool.commons.job.core.job.domain.JobInstance;
import com.cutefool.commons.job.libs.JobsContext;

/**
 * @author 271007729@qq.com
 * @date 9/6/21 3:59 PM
 */
public class JobsContexts {

    public static JobsContext context(JobCycle config, JobInstance instance) {
        JobsContext context = new JobsContext();
        context.setInstanceId(instance.getId());
        context.setName(config.getName());
        context.setJobId(config.getId());
        context.setParameters(config.getParameters());
        return context;
    }
}
