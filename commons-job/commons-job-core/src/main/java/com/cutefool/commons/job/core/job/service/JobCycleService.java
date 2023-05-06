/*
 *  
 */
package com.cutefool.commons.job.core.job.service;

import com.cutefool.commons.expression.Conditioning;
import com.cutefool.commons.job.core.job.domain.JobCycle;
import com.cutefool.commons.job.core.job.mapper.JobCycleMapper;
import com.cutefool.commons.job.libs.JobQuery;
import com.cutefool.commons.job.libs.cycle.CycleJobs;
import com.cutefool.commons.mybatis.dsl.MybatisOperations;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 9/3/21 3:31 PM
 */
public interface JobCycleService extends MybatisOperations<CycleJobs, JobCycle, Long, JobCycleMapper, Conditioning> {

    /**
     * 查询需要执行的任务
     *
     * @param time nextTime
     * @return configs
     */
    List<JobCycle> listByNextTime(LocalDateTime time);


    /**
     * 查询需要执行的任务
     *
     * @param appId appId
     * @return configs
     */
    List<JobCycle> listByAppId(String appId);

    void updateBy(String identifier, CycleJobs jobs);

    void updateBy(String identifier, String jobType, CycleJobs jobs);


    void deleteBy(String identifier);


    void deleteBy(String identifier, String jobType);

    /**
     * 查询需要执行的任务
     *
     * @param jobQuery jobQuery
     * @return JobCycle
     */
    List<JobCycle> listByWhere(JobQuery jobQuery);

}
