/*
 *
 */
package com.cutefool.commons.job.core.invoker;

import com.cutefool.commons.job.core.job.domain.JobCycle;
import com.cutefool.commons.job.core.job.service.JobCycleService;
import com.cutefool.commons.job.core.job.service.JobInstanceService;
import com.cutefool.commons.job.libs.JobLibs;
import com.cutefool.commons.job.libs.JobQuery;
import com.cutefool.commons.job.libs.JobsContext;
import com.cutefool.commons.job.libs.MasterInvoker;
import com.cutefool.commons.job.libs.cycle.CycleJobs;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.ProcessUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 9/15/21 4:36 PM
 */
@Component
public class LocalMasterInvoker implements MasterInvoker {

    @Resource
    private JobCycleService jobCycleService;

    @Resource
    private JobInstanceService jobInstanceService;

    /**
     * 创建作业
     *
     * @param jobs jobs
     * @return 作业id
     */
    @Override
    public Long create(CycleJobs jobs) {
        JobCycle jobCycle = this.jobCycleService.create(jobs);
        return jobCycle.getId();
    }

    /**
     * 创建作业
     *
     * @param jobs jobs
     * @return 作业id
     */
    @Override
    public Long update(Long jobId, CycleJobs jobs) {
        this.jobCycleService.update(jobId, jobs);
        return jobId;
    }

    @Override
    public void updateBy(String identifier, CycleJobs jobs) {
        this.jobCycleService.updateBy(identifier, jobs);
    }

    @Override
    public void updateBy(String identifier, String jobType, CycleJobs jobs) {
        this.jobCycleService.updateBy(identifier, jobType, jobs);
    }

    /**
     * 删除作业
     */
    @Override
    public void delete(Long jobId) {
        this.jobCycleService.delete(jobId);
    }

    @Override
    public void deleteBy(String identifier) {
        this.jobCycleService.deleteBy(identifier);
    }

    @Override
    public void deleteBy(String identifier, String jobType) {
        this.jobCycleService.deleteBy(identifier, jobType);
    }

    @Override
    public List<JobLibs> jobs(String appId) {
        List<JobCycle> jobCycles = this.jobCycleService.listByAppId(appId);
        return Lists.empty(jobCycles).stream().map(e -> new JobLibs(e.getId(), e.getExecutor())).collect(Collectors.toList());
    }

    @Override
    public List<CycleJobs> jobs(JobQuery jobQuery) {
        List<JobCycle> jobCycles = this.jobCycleService.listByWhere(jobQuery);
        return ProcessUtils.processList(CycleJobs.class, jobCycles);
    }

    @Override
    public void feedback(JobsContext context, Response<?> response) {
        jobInstanceService.update(context.getInstanceId(), (e) -> e.setState((byte) 2));

    }
}
