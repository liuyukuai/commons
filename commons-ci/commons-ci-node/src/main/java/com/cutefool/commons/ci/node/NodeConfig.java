/*
 *  
 */
package com.cutefool.commons.ci.node;

import lombok.Data;
import com.cutefool.commons.ci.core.CiConfig;

/**
 * Nexus配置类
 *
 * @author 271007729@qq.com
 * @date 7/27/21 3:42 PM
 */
@Data
@SuppressWarnings({"unused"})
public class NodeConfig implements CiConfig {

    /**
     * npm
     */
    private String npm;

    /**
     * 镜像
     */
    private String registry;


    public static NodeConfig defaultConfig() {
        NodeConfig nodeConfig = new NodeConfig();
        nodeConfig.setNpm("npm");
        return nodeConfig;
    }
}
