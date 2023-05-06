/*
 *
 */
package com.cutefool.commons.ci.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 271007729@qq.com
 * @date 7/21/21 2:08 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dependency {

    /**
     * 分组
     */
    private String groupId;

    /**
     * 骨架
     */
    private String artifactId;

    /**
     * 版本号
     */
    private String version;
}
