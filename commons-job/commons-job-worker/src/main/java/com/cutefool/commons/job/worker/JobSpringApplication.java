/*
 *
 */
package com.cutefool.commons.job.worker;

import com.cutefool.commons.job.libs.JobLibs;
import com.cutefool.commons.job.libs.JobsExecutor;
import com.cutefool.commons.job.libs.MasterInvoker;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.job.worker.factory.JobFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * job应用
 *
 * @author 271007729@qq.com
 * @date 2021/9/16 12:40 AM
 */
@Slf4j
public class JobSpringApplication extends JobBaseApplication implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    @Resource
    private MasterInvoker masterInvoker;


    private ApplicationContext applicationContext;


    public JobSpringApplication(String address, String appId) {
        super(address, appId);
    }

    @Override
    public void destroy() {
        log.info("Jobs context is destroyed");
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.doInit();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void doInit() {
        this.initExecutors();
        this.doRegistry();
    }


    private void initExecutors() {
        List<JobLibs> jobs = this.masterInvoker.jobs(this.getAppId());
        if (applicationContext == null || Lists.isEmpty(jobs)) {
            log.warn("jobs JobsExecutor jobs = {}", jobs);
            return;
        }

        try {
            Map<String, List<Long>> jobsMap = jobs.stream().filter(e -> StringUtils.isNotBlank(e.getExecutor())).collect(Collectors.groupingBy(JobLibs::getExecutor, Collectors.mapping(JobLibs::getId, Collectors.toList())));
            Map<String, JobsExecutor> executorMap = applicationContext.getBeansOfType(JobsExecutor.class);
            Collection<JobsExecutor> values = executorMap.values();
            for (JobsExecutor executor : values) {
                List<Long> ids = jobsMap.get(executor.getClass().getName());
                if (Lists.iterable(ids)) {
                    ids.forEach(id -> {
                        log.info("jobs JobsExecutor id= {} executor = {}", id, executor);
                        JobFactory.put(String.valueOf(id), executor);
                    });
                }
            }
        } catch (Exception e) {
            log.warn("jobs JobsExecutor warning = {}", e.getMessage(), e);
        }

    }

    private void doRegistry() {
        try {
            Map<String, JobsRegistry> jobsRegistries = applicationContext.getBeansOfType(JobsRegistry.class);
            Maps.empty(jobsRegistries).forEach((k, v) -> {
                log.info("jobs JobsRegistry k = {} v = {}", k, v);

                v.doRegistry();
            });
        } catch (Exception e) {
            log.warn("jobs JobsRegistry warning = {}", e.getMessage(), e);
        }
    }
}
