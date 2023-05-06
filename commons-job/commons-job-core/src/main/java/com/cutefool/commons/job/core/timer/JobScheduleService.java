/*
 *
 */
package com.cutefool.commons.job.core.timer;

import com.cutefool.commons.job.core.job.domain.JobCycle;
import com.cutefool.commons.job.core.job.service.JobCycleService;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.job.core.executor.JobCycleExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务
 *
 * @author 271007729@qq.com
 * @date 9/3/21 3:37 PM
 */
@Slf4j
@Component
public class JobScheduleService {

    /**
     * 定时任务循环间隔
     */
    private static final long SCHEDULE_RATE = 15;

    @Resource
    private JobCycleService jobCycleService;

    @Resource
    private JobCycleExecutor jobConfigExecutor;

    /**
     * 15秒调度一次
     */
    @Scheduled(fixedRate = SCHEDULE_RATE * 1000)
    public void schedule() {
        log.info("begin execute jobs");
        // 查询当前时间30秒内的任务
        LocalDateTime nextTime = LocalDateTime.now().plusSeconds(15 * 2);
        List<JobCycle> jobConfigs = this.jobCycleService.listByNextTime(nextTime);
        log.info("query jobs is {}", jobConfigs);
        // 如果任务为空
        if (Lists.isEmpty(jobConfigs)) {
            return;
        }
        jobConfigExecutor.doExecute(jobConfigs);
    }
}
