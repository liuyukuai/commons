/*
 *
 */
package com.cutefool.commons.job.core.executor;

import com.cutefool.commons.job.core.job.domain.JobCycle;
import com.cutefool.commons.job.core.job.domain.JobInstance;
import com.cutefool.commons.job.core.job.service.JobCycleService;
import com.cutefool.commons.job.core.job.service.JobInstanceService;
import com.cutefool.commons.job.core.timer.JobTimeWheelService;
import com.cutefool.commons.job.core.utils.JobsContexts;
import com.cutefool.commons.job.libs.JobsContext;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.Status;
import com.cutefool.commons.core.cycle.Cycles;
import com.cutefool.commons.core.util.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 任务执行器
 *
 * @author 271007729@qq.com
 * @date 9/3/21 4:47 PM
 */
@Slf4j
@Service
public class JobCycleExecutor implements JobConfigExecutor<List<JobCycle>> {

    @Resource
    private JobCycleService jobCycleService;

    @Resource
    private JobInstanceService jobInstanceService;

    @Resource
    private JobDispatchExecutor jobDispatchExecutor;

    @Resource
    private JobTimeWheelService jobTimeWheelService;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void doExecute(List<JobCycle> jobs) {
        LocalDateTime now = LocalDateTime.now();
        // 处理过期的任务
        jobs = this.doExpire(now, jobs);


        Map<Long, JobCycle> cycleMap = Lists.empty(jobs).stream().collect(Collectors.toMap(JobCycle::getId, e -> e));
        List<JobInstance> instances = jobInstanceService.createByConfig(jobs);
        log.info("create instances = {}", instances);
        Lists.empty(instances)
                .forEach(e -> {
                    // 时间差
                    Duration between = Duration.between(now, e.getExecuteTime());
                    JobsContext context = JobsContexts.context(cycleMap.get(e.getJobId()), e);
                    log.info("execute instance= {} duration = {} ", e, between.getSeconds());

                    // 如果时间差为零或者负数,立即执行
                    if (between.getSeconds() <= 0 || between.isZero()) {
                        jobDispatchExecutor.doExecute(context);
                    }

                    if (between.getSeconds() > 0) {
                        // 如果不是立即执行
                        jobTimeWheelService.create(context, between);
                    }
                });
        doRefresh(cycleMap.values());
    }


    private void doRefresh(Collection<JobCycle> jobs) {
        jobs.forEach(e -> {
            // 计算下一次执行时间
            LocalDateTime next = Cycles.next(e.getCycle(), e.getDuration(), e.getNextTime());
            e.setNextTime(next);
            if (Objects.isNull(next)) {
                e.setIsStop(Status.STATUS_TRUE);
            }
            jobCycleService.update(e);
        });
    }


    private List<JobCycle> doExpire(LocalDateTime now, List<JobCycle> jobs) {
        return jobs.stream()
                .peek(e -> {
                    // 时间差
                    Duration between = Duration.between(now, e.getNextTime());
                    // 说明过期
                    if (between.getSeconds() <= 0) {
                        this.doNextTime(e);
                    }
                })
                .filter(e -> Objects.nonNull(e.getNextTime()))
                .filter(e -> {
                    Duration between = Duration.between(now, e.getNextTime());
                    return between.getSeconds() < 30L;
                }).collect(Collectors.toList());
    }

    @Async
    public void doNextTime(JobCycle e) {
        // 已经过期的任务
        if (Cycles.isOnce(e.getCycle())) {
            // 如果是一次，本次结束
            e.setNextTime(null);
            this.jobCycleService.update(e);
            return;
        }

        LocalDateTime next = Cycles.next(e.getCycle(), e.getDuration(), e.getNextTime());
        Duration between = Duration.between(LocalDateTime.now(), next);
        while (between.getSeconds() <= 0) {
            next = Cycles.next(e.getCycle(), e.getDuration(), next);
            between = Duration.between(LocalDateTime.now(), next);
        }
        e.setNextTime(next);
        this.jobCycleService.update(e);
    }
}
