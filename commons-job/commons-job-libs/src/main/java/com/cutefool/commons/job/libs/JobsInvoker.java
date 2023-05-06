package com.cutefool.commons.job.libs;

import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.job.libs.cycle.CycleJobs;

import java.util.List;

public interface JobsInvoker {


    /**
     * 删除任务
     *
     * @param jobId jobId
     * @param jobs  jobs
     * @return jobId
     */
    Long update(Long jobId, CycleJobs jobs);

    void updateBy(String identifier, CycleJobs jobs);

    void updateBy(String identifier, String jobType, CycleJobs jobs);

    /**
     * 删除任务
     *
     * @param jobId 任务ID
     */
    void delete(Long jobId);

    void deleteBy(String identifier);


    void deleteBy(String identifier, String jobType);

    /**
     * 查询任务
     *
     * @param appId appId
     * @return 任务
     */
    List<JobLibs> jobs(String appId);

    /**
     * 查询任务
     *
     * @param jobQuery jobQuery
     * @return 任务
     */
    List<CycleJobs> jobs(JobQuery jobQuery);

    /**
     * 反馈
     *
     * @param context  context
     * @param response response
     */
    void feedback(JobsContext context, Response<?> response);
}
