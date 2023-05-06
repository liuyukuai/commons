/*
 *
 */
package com.cutefool.commons.job.libs.cron;

import com.cutefool.commons.job.libs.Job;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * cronJob
 *
 * @author 271007729@qq.com
 * @date 9/6/21 11:27 AM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CornJobs extends Job {

    /**
     * cron表达式
     */
    private String cron;

}
