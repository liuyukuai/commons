/*
 *
 */
package com.cutefool.commons.ci.maven.context;

import com.cutefool.commons.ci.maven.MavenConfig;
import com.cutefool.commons.ci.maven.MavenConfigurable;
import lombok.*;
import com.cutefool.commons.ci.core.CiConsumer;
import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.spring.SpiSpringContext;
import org.apache.maven.shared.invoker.InvocationOutputHandler;

/**
 * maven context
 *
 * @author 271007729@qq.com
 * @date 5/31/21 2:49 PM
 */
@Data
@Builder
public class MavenContext {

    /**
     * maven home
     */
    private String home;

    /**
     * maven配置文件路径
     */
    private String settings;

    /**
     * 项目本地目录
     */
    private String localRepository;

    /**
     * pom文件路径
     */
    private String pom;


    /**
     * 是否debug
     */
    private boolean debug;

    /**
     * 日志处理类
     */
    private InvocationOutputHandler invocationOutputHandler;


    public static <C extends CiContext<C>> MavenContext init(C context) {
        MavenConfigurable oneSpi = SpiSpringContext.getOneSpi(MavenConfigurable.class);
        MavenConfig setting = oneSpi.config();
        return MavenContext.builder()
                .settings(setting.getSettings())
                .home(setting.getHome())
                .localRepository(context.getLocalRepository())
                .pom(context.getLocalSetting())
                .debug(context.isDebug())
                .invocationOutputHandler(s -> {
                    CiConsumer<C> ciConsumer = CiConsumer.empty(context.getCiLogsConsumer());
                    ciConsumer.consumer(context, s);
                })
                .build();
    }

}
