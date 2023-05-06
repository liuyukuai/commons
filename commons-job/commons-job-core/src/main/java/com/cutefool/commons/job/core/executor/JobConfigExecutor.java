/*
 *  
 */
package com.cutefool.commons.job.core.executor;

/**
 * 任务执行器
 *
 * @author 271007729@qq.com
 * @date 9/3/21 4:47 PM
 */
public interface JobConfigExecutor<T> {

    /**
     * 任务执行器
     *
     * @param job 任务
     */
    void doExecute(T job);
}
