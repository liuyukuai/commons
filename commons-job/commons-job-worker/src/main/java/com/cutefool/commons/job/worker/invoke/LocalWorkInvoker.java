/*
 *
 */
package com.cutefool.commons.job.worker.invoke;

import com.cutefool.commons.job.libs.JobsContext;
import com.cutefool.commons.job.libs.JobsExecutor;
import com.cutefool.commons.job.libs.MasterInvoker;
import com.cutefool.commons.job.libs.WorkerInvoker;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.job.worker.factory.JobFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author 271007729@qq.com
 * @date 9/6/21 3:45 PM
 */
@Component
public class LocalWorkInvoker implements WorkerInvoker {

    @Resource
    private MasterInvoker masterInvoker;

    @Override
    public void doInvoke(JobsContext context) {
        JobsExecutor jobsExecutor = JobFactory.get(String.valueOf(context.getJobId()));
        if (Objects.nonNull(jobsExecutor)) {
            Response<?> response = jobsExecutor.doJob(context);
            masterInvoker.feedback(context, response);
        }
    }
}
