/*
 *  
 */
package com.cutefool.commons.job.worker;

import com.cutefool.commons.job.libs.*;
import com.cutefool.commons.job.libs.cycle.CycleJobs;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.job.libs.*;
import com.cutefool.commons.job.worker.factory.JobFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 作业操作类
 *
 * @author 271007729@qq.com
 * @date 9/6/21 11:24 AM
 */
@Component
@SuppressWarnings("unused")
public class JobOperation implements JobsInvoker {

    @Resource
    private MasterInvoker masterInvoker;

    @Resource
    private JobBaseApplication jobBaseApplication;

    /**
     * 创建作业
     *
     * @param jobs jobs
     * @return 作业id
     */
    public Long create(CycleJobs jobs, JobsExecutor jobsExecutor) {
        this.initAppId(jobs);
        Long id = this.masterInvoker.create(jobs);
        JobFactory.put(String.valueOf(id), jobsExecutor);
        return id;
    }


    public void createIf(CycleJobs jobs, JobsExecutor jobsExecutor) {
        this.initAppId(jobs);
        // 查询任务是否已经存在
        JobQuery query = new JobQuery();
        query.setAppId(jobs.getAppId());
        query.setIdentifier(jobs.getIdentifier());
        query.setJobType(jobs.getJobType());
        List<CycleJobs> jobsLists = this.jobs(query);
        if (Lists.iterable(jobsLists)) {
            for (CycleJobs job : jobsLists) {
                JobFactory.put(String.valueOf(job.getId()), jobsExecutor);
            }
            return;
        }
        Long id = this.masterInvoker.create(jobs);
        JobFactory.put(String.valueOf(id), jobsExecutor);
    }

    /**
     * 创建作业
     *
     * @param jobs jobs
     * @return 作业id
     */
    public Long update(Long jobId, CycleJobs jobs) {
        this.initAppId(jobs);
        this.masterInvoker.update(jobId, jobs);
        return jobId;
    }

    @Override
    public void updateBy(String identifier, CycleJobs jobs) {
        this.initAppId(jobs);
        this.masterInvoker.updateBy(identifier, jobs);
    }

    @Override
    public void updateBy(String identifier, String jobType, CycleJobs jobs) {
        this.initAppId(jobs);
        this.masterInvoker.updateBy(identifier, jobType, jobs);
    }

    /**
     * 删除作业
     */
    public void delete(Long jobId) {
        this.masterInvoker.delete(jobId);
        JobFactory.remove(String.valueOf(jobId));
    }

    @Override
    public void deleteBy(String identifier) {
        this.masterInvoker.deleteBy(identifier);
    }

    @Override
    public void deleteBy(String identifier, String jobType) {
        this.masterInvoker.deleteBy(identifier, jobType);

    }

    @Override
    public List<JobLibs> jobs(String appId) {
        return this.masterInvoker.jobs(appId);
    }

    @Override
    public List<CycleJobs> jobs(JobQuery jobQuery) {
        return this.masterInvoker.jobs(jobQuery);
    }

    @Override
    public void feedback(JobsContext context, Response<?> response) {
        this.masterInvoker.feedback(context, response);
    }


    private void initAppId(Job job) {
        String appId = job.getAppId();
        if (StringUtils.isBlank(appId)) {
            job.setAppId(this.jobBaseApplication.getAppId());
        }
    }
}
