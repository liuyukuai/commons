/*
 *
 */
package com.cutefool.commons.ci.git.libs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Git合并对象
 *
 * @author 271007729@qq.com
 * @date 5/26/21 10:34 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitMerge {

    /**
     * from branch
     */
    private String from;

    /**
     * to branch
     */
    private String to;

    /**
     * 提交日志
     */
    private String message;
}
