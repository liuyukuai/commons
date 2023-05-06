/*
 *
 */
package com.cutefool.commons.job.core.executor;

import com.cutefool.commons.job.core.job.enums.JobState;
import com.cutefool.commons.job.core.job.service.JobInstanceService;
import com.cutefool.commons.job.libs.JobsContext;
import com.cutefool.commons.job.libs.WorkerInvoker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 271007729@qq.com
 * @date 9/3/21 5:21 PM
 */
@Service
public class JobDispatchExecutor implements JobExecutor {

    @Resource
    private WorkerInvoker workerInvoker;

    @Resource
    private JobInstanceService jobInstanceService;

    @Override
    public void doExecute(JobsContext context) {
        this.workerInvoker.doInvoke(context);
        // 只要调用成功修改实例状态为执行中
        jobInstanceService.update(context.getInstanceId(), e -> e.setState(JobState.EXECUTING.getKey()));
    }
}
