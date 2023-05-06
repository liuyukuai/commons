/*
 *
 */
package com.cutefool.commons.job.libs;

/**
 * worker执行器
 *
 * @author 271007729@qq.com
 * @date 9/15/21 4:02 PM
 */
public interface WorkerInvoker {

    /**
     * 执行任务
     *
     * @param context context
     */
    default void doInvoke(JobsContext context) {
    }
}
