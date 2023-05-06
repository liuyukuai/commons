/*
 *
 */
package com.cutefool.commons.ci.maven;

import lombok.Builder;
import lombok.Data;
import com.cutefool.commons.ci.core.CiConfig;

/**
 * @author 271007729@qq.com
 * @date 7/11/21 10:29 PM
 */
@Data
@Builder
public class MavenConfig implements CiConfig {
    /**
     * maven home
     */
    private String home;

    /**
     * maven配置文件路径
     */
    private String settings;
}
