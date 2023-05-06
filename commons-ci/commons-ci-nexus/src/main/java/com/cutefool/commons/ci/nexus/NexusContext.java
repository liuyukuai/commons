/*
 *
 */
package com.cutefool.commons.ci.nexus;

import com.cutefool.commons.ci.core.CiConsumer;
import com.cutefool.commons.ci.core.CiReleaseContext;
import lombok.Builder;
import lombok.Data;
import com.cutefool.commons.spring.SpiSpringContext;

import java.util.function.Consumer;

/**
 * Nexus配置类
 *
 * @author 271007729@qq.com
 * @date 7/27/21 3:42 PM
 */
@Data
@Builder
@SuppressWarnings({"unused", "WeakerAccess"})
public class NexusContext {
    /**
     * nexus服务地址
     */
    private String address;

    /**
     * 仓库名称
     */
    private String repository;

    /**
     * groupId
     */
    private String groupId;

    /**
     * artifactId
     */

    private String artifactId;

    /**
     * version
     */
    private String version;

    private String user;

    private String password;

    /**
     * 日志
     */
    private Consumer<String> consumer;

    public static <T extends CiReleaseContext<T>> NexusContext init(T context) {
        NexusConfigurable oneSpi = SpiSpringContext.getOneSpi(NexusConfigurable.class);
        CiConsumer<T> ciConsumer = CiConsumer.empty(context.getCiLogsConsumer());
        NexusConfig setting = oneSpi.config();
        return NexusContext.builder()
                           .address(setting.getAddress())
                           .groupId(context.getGroupId())
                           .user(setting.getUser())
                           .password(setting.getPassword())
                           .repository(setting.getReleaseRepository())
                           .artifactId(context.getArtifactId())
                           .version(context.getRelease())
                           .consumer(s -> ciConsumer.consumer(context, s))
                           .build();
    }

}
