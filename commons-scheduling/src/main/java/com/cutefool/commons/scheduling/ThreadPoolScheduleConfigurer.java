/*
 *
 */
package com.cutefool.commons.scheduling;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * 定时任务线程池配置
 *
 * @author 271007729@qq.com
 * @date 2022/7/10 11:03 AM
 */
@Configuration
public class ThreadPoolScheduleConfigurer implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        BasicThreadFactory factory = new BasicThreadFactory.Builder().namingPattern("sys-task-" + "-%d").build();
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10, factory));
    }
}
