/*
 *  
 */
package com.cutefool.commons.job.core.job.service;

import com.cutefool.commons.expression.Conditioning;
import com.cutefool.commons.job.core.job.domain.JobCycle;
import com.cutefool.commons.job.core.job.mapper.JobCycleDsl;
import com.cutefool.commons.job.libs.JobQuery;
import com.cutefool.commons.job.libs.cycle.CycleJobs;
import com.cutefool.commons.core.Status;
import com.cutefool.commons.core.util.ProcessUtils;
import com.cutefool.commons.job.core.job.mapper.JobCycleMapper;
import com.cutefool.commons.mybatis.dsl.MybatisIdWorkerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * 周期任务
 *
 * @author 271007729@qq.com
 * @date 9/3/21 3:31 PM
 */
@Service
public class JobCycleServiceImpl extends MybatisIdWorkerTemplate<CycleJobs, JobCycle, Long, JobCycleMapper, Conditioning> implements JobCycleService {


    @Override
    public List<JobCycle> listByNextTime(LocalDateTime time) {
        return this.getRepository().select(this.newSelect(((dsl, where) -> {
            where.and(JobCycleDsl.nextTime, SqlBuilder.isNotNull());
            where.and(JobCycleDsl.nextTime, SqlBuilder.isLessThanOrEqualTo(time));
            where.and(JobCycleDsl.isStop, SqlBuilder.isEqualTo((Status.STATUS_FALSE)));
        })));
    }

    @Override
    public List<JobCycle> listByAppId(String appId) {
        return this.getRepository().select(this.newSelect(((dsl, where) -> {
            where.and(JobCycleDsl.appId, SqlBuilder.isEqualTo(appId));
        })));
    }

    @Override
    public List<JobCycle> listByWhere(JobQuery jobQuery) {
        return this.getRepository().select(this.newSelect(((dsl, where) -> {
            if (StringUtils.isNotBlank(jobQuery.getAppId())) {
                where.and(JobCycleDsl.appId, SqlBuilder.isEqualTo(jobQuery.getAppId()));
            }
            if (StringUtils.isNotBlank(jobQuery.getIdentifier())) {
                where.and(JobCycleDsl.identifier, SqlBuilder.isEqualTo(jobQuery.getIdentifier()));
            }
            if (StringUtils.isNotBlank(jobQuery.getJobType())) {
                where.and(JobCycleDsl.jobType, SqlBuilder.isEqualTo(jobQuery.getJobType()));
            }
        })));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateBy(String identifier, CycleJobs jobs) {
        if (StringUtils.isNotBlank(identifier)) {
            Optional<JobCycle> optional = this.getRepository().selectOne(this.newSelect(((dsl, where) -> where.and(JobCycleDsl.identifier, SqlBuilder.isEqualTo(identifier)))));
            if (optional.isPresent()) {
                JobCycle jobCycle = optional.get();
                ProcessUtils.processObject(jobCycle, jobs);
                this.update(jobCycle);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateBy(String identifier, String jobType, CycleJobs jobs) {
        if (StringUtils.isNotBlank(identifier)) {
            Optional<JobCycle> optional = this.getRepository().selectOne(this.newSelect(((dsl, where) -> {
                where.and(JobCycleDsl.identifier, SqlBuilder.isEqualTo(identifier));
                where.and(JobCycleDsl.jobType, SqlBuilder.isEqualTo(jobType));
            })));
            if (optional.isPresent()) {
                JobCycle jobCycle = optional.get();
                ProcessUtils.processObject(jobCycle, jobs);
                this.update(jobCycle);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteBy(String identifier) {
        if (StringUtils.isNotBlank(identifier)) {
            this.getRepository().delete(this.newDelete(((deleteModelDeleteDSL, where) -> where.and(JobCycleDsl.identifier, SqlBuilder.isEqualTo(identifier)))));
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteBy(String identifier, String jobType) {
        if (StringUtils.isNotBlank(identifier) && StringUtils.isNotBlank(jobType)) {
            this.getRepository().delete(this.newDelete((dsl, where) -> {
                where.and(JobCycleDsl.identifier, SqlBuilder.isEqualTo(identifier));
                where.and(JobCycleDsl.jobType, SqlBuilder.isEqualTo(jobType));
            }));
        }
    }

    @Override
    public BiConsumer<JobCycle, CycleJobs> preCreate() {
        return super.preCreate().andThen((d, s) -> {
            // nextTime
            d.setNextTime(s.getFirstTime());
            // isStop
            d.setIsStop(Status.STATUS_FALSE);
        });
    }

    @Override
    public BiConsumer<JobCycle, CycleJobs> preUpdate() {
        return super.preUpdate().andThen((d, s) -> {
            // nextTime
            d.setNextTime(s.getFirstTime());
            // isStop
            d.setIsStop(Status.STATUS_FALSE);
        });
    }
}
