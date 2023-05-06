/*
 *  
 */
package com.cutefool.commons.job.core.timer;

import com.cutefool.commons.job.libs.JobsContext;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import com.cutefool.commons.job.core.executor.JobDispatchExecutor;

/**
 * @author 271007729@qq.com
 * @date 9/6/21 3:57 PM
 */
public class JobTimerTask implements TimerTask {

    private JobsContext context;

    private JobDispatchExecutor jobDispatchExecutor;

    public JobTimerTask(JobsContext context, JobDispatchExecutor jobDispatchExecutor) {
        this.context = context;
        this.jobDispatchExecutor = jobDispatchExecutor;
    }

    @Override
    public void run(Timeout timeout) {
        this.jobDispatchExecutor.doExecute(context);
    }
}
