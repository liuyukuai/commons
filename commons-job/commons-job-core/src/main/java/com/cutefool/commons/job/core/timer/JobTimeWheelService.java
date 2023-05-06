/*
 *
 */
package com.cutefool.commons.job.core.timer;

import com.cutefool.commons.job.libs.JobsContext;
import io.netty.util.HashedWheelTimer;
import com.cutefool.commons.job.core.executor.JobDispatchExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 271007729@qq.com
 * @date 9/3/21 11:34 AM
 */
@Component
public class JobTimeWheelService {

    @Resource
    private JobDispatchExecutor jobDispatchExecutor;

    private static final Map<Long, HashedWheelTimer> JOBS_MAP = new ConcurrentHashMap<>();

    public void create(JobsContext context, Duration between) {
        HashedWheelTimer timer = new HashedWheelTimer();
        JobTimerTask timerTask = new JobTimerTask(context, this.jobDispatchExecutor);
        timer.newTimeout(timerTask, between.getSeconds(), TimeUnit.SECONDS);
        timer.start();
        JOBS_MAP.put(context.getInstanceId(), timer);
    }

    //TODO
    public void remove(Long instanceId) {
        HashedWheelTimer hashedWheelTimer = JOBS_MAP.get(instanceId);
        if (Objects.nonNull(hashedWheelTimer)) {
            hashedWheelTimer.stop();
        }
        JOBS_MAP.remove(instanceId);
    }

}
