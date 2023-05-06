/*
 *  
 */
package com.cutefool.commons.ci.maven.command;

import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.ci.maven.context.MavenContext;
import com.cutefool.commons.core.Executable;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 基础工具类
 *
 * @author 271007729@qq.com
 * @date 5/31/21 3:36 PM
 */
@Slf4j
@SuppressWarnings("unused")
public class BaseCommand implements Executable<Response<Boolean>> {

    /**
     * context
     */
    private MavenContext context;

    /**
     * 执行命令
     */
    private InvocationRequest request;

    /**
     * maven命令执行器
     */
    private Invoker invoker;


    protected BaseCommand(MavenContext context) {
        // invoker
        invoker = new DefaultInvoker();
        // request
        request = new DefaultInvocationRequest();
        request.setDebug(context.isDebug());
        String home = context.getHome();
        // 设置maven home
        if (StringUtils.isNotBlank(home)) {
            invoker.setMavenHome(new File(home));
        }
        // 日志处理
        InvocationOutputHandler handler = context.getInvocationOutputHandler();

        if (Objects.nonNull(handler)) {
            invoker.setOutputHandler(handler);
        }

        // 设置setting文件路径
        String settings = context.getSettings();
        if (StringUtils.isNotBlank(settings)) {
            settings = home + File.separator + "conf" + File.separator + settings;
            request.setUserSettingsFile(new File(settings));
        }
        File pomFile = new File(context.getLocalRepository(), context.getPom());

        invoker.setWorkingDirectory(new File(context.getLocalRepository(), context.getPom()).getParentFile());
        // 项目pom文件
        request.setPomFile(pomFile);
    }

    protected void setGoals(List<String> executes) {
        List<String> goals = request.getGoals();
        if (!Lists.iterable(goals)) {
            this.request.setGoals(executes);
            return;
        }
        ArrayList<String> newList = new ArrayList<>(goals);
        newList.addAll(executes);
        request.setGoals(newList);
    }

    @Override
    public Response<Boolean> execute() {
        try {
            InvocationResult execute = invoker.execute(request);
            return Response.ok(execute.getExitCode() == 0);
        } catch (MavenInvocationException e) {
            log.warn(e.getMessage(), e);
            return Response.failure(e.getMessage());
        }
    }
}
