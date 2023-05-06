/*
 *
 */
package com.cutefool.commons.ci.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.cutefool.commons.version.Version;

/**
 * @author 271007729@qq.com
 * @date 7/22/21 4:43 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Identifier {

    /**
     * 分组
     */
    private String groupId;

    /**
     * 唯一标识
     */
    private String artifactId;

    /**
     * 后缀 （jar,zip,war)
     */
    private String suffix;

    /**
     * 版本号
     */
    private Version version;

}
