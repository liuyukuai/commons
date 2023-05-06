/*
 *
 */
package com.cutefool.commons.job.core.job.service;

import com.cutefool.commons.expression.Conditioning;
import com.cutefool.commons.job.core.job.domain.JobCycle;
import com.cutefool.commons.job.core.job.domain.JobInstance;
import com.cutefool.commons.job.core.job.enums.JobState;
import com.cutefool.commons.job.core.job.mapper.JobInstanceMapper;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.mybatis.dsl.MybatisIdWorkerTemplate;
import com.cutefool.commons.snowflake.IdWorker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 9/3/21 3:31 PM
 */
@Service
public class JobInstanceServiceImpl extends MybatisIdWorkerTemplate<JobInstance, JobInstance, Long, JobInstanceMapper, Conditioning> implements JobInstanceService {

    @Resource
    private IdWorker idWorker;

    @Override
    public BiConsumer<JobInstance, JobInstance> preCreate() {
        return (d, s) -> d.setCreateTime(LocalDateTime.now());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<JobInstance> createByConfig(List<JobCycle> configs) {
        if (Lists.isEmpty(configs)) {
            return Collections.emptyList();
        }
        List<JobInstance> instances = configs.stream()
                .map(e -> {
                    JobInstance instance = new JobInstance();
                    instance.setAppId(e.getAppId());
                    instance.setJobId(e.getId());
                    instance.setTriggerTime(LocalDateTime.now());
                    instance.setExecuteTime(e.getNextTime());
                    instance.setState(JobState.WAIT.getKey());
                    instance.setId(idWorker.nextId());
                    return instance;
                })
                .collect(Collectors.toList());
        this.create(instances);
        return instances;
    }
}
