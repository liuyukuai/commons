/*
 *
 */
package com.cutefool.commons.job.libs;

import com.cutefool.commons.core.page.Response;

/**
 * @author 271007729@qq.com
 * @date 2021/9/16 2:32 AM
 */
public interface JobsExecutor {

    /**
     * 执行任务
     *
     * @param context context
     * @return res
     */
    Response<?> doJob(JobsContext context);
}
