/*
 *  
 */
package com.cutefool.commons.job.libs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务配置
 *
 * @author 271007729@qq.com
 * @date 9/6/21 11:27 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobLibs {

    private Long id;

    /**
     * 任务执行器
     */
    private String executor;
}
