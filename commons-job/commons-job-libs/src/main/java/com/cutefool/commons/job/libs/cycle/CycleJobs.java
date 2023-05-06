/*
 *
 */
package com.cutefool.commons.job.libs.cycle;

import com.cutefool.commons.job.libs.Job;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * cronJob
 *
 * @author 271007729@qq.com
 * @date 9/6/21 11:27 AM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CycleJobs extends Job {

    /**
     * 首次执行时间
     */
    private LocalDateTime firstTime;
    /**
     * 周期
     */
    private String cycle;

    /**
     * 周期间隔
     */
    private Long duration;

}
