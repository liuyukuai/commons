/*
 *  
 */
package com.cutefool.commons.ci.nexus;

import com.cutefool.commons.ci.core.CiConfig;
import lombok.Data;

/**
 * Nexus配置类
 *
 * @author 271007729@qq.com
 * @date 7/27/21 3:42 PM
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class NexusConfig implements CiConfig {

    /**
     * nexus服务地址
     */
    private String address;

    /**
     * 正式库名称
     */
    private String releaseRepository;

    /**
     * 开发库名称
     */
    private String snapshotRepository;

    private String user;

    private String password;

}
