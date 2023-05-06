/*
 *
 */
package com.cutefool.commons.ci.node;

import com.cutefool.common.ci.shell.Executors;
import com.cutefool.commons.ci.core.CiConsumer;
import com.cutefool.commons.ci.core.CiReleaseContext;
import com.cutefool.commons.core.page.Response;
import org.apache.commons.exec.CommandLine;

import java.io.IOException;

/**
 * Nexus 工具类
 *
 * @author 271007729@qq.com
 * @date 7/27/21 3:42 PM
 */
public class NodeBuildExecuting<T extends CiReleaseContext<T>> implements NodeExecuting<T, Boolean> {

    @Override
    public Response<Boolean> execute(T ciContext) {
        CiConsumer<T> ciConsumer = CiConsumer.empty(ciContext.getCiProcessConsumer());
        try {
            // 执行构建
            NodeInstallExecuting<T> executing = new NodeInstallExecuting<>();
            executing.execute(ciContext);
            ciConsumer.consumer(ciContext, NodeConstants.YARN_BUILD_EXECUTE);
            CommandLine commandLine = CommandLine.parse(NodeConstants.YARN_BUILD_EXECUTE);
            Executors.execute(ciContext, commandLine);
            return Response.ok();
        } catch (IOException e) {
            ciConsumer.consumer(ciContext, e.getMessage());
            return Response.failure(e.getMessage());
        }
    }
}
