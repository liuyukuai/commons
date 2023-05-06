/*
 *
 */
package com.cutefool.commons.job.core.job.service;

import com.cutefool.commons.expression.Conditioning;
import com.cutefool.commons.job.core.job.domain.JobCycle;
import com.cutefool.commons.job.core.job.domain.JobInstance;
import com.cutefool.commons.job.core.job.mapper.JobInstanceMapper;
import com.cutefool.commons.mybatis.dsl.MybatisOperations;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 9/3/21 3:31 PM
 */
public interface JobInstanceService extends MybatisOperations<JobInstance, JobInstance, Long, JobInstanceMapper, Conditioning> {

    /**
     * 批量创建任务实例
     *
     * @param configs 任务配置
     * @return JobInstances
     */
    List<JobInstance> createByConfig(List<JobCycle> configs);

}
