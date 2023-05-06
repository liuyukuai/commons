/*
 *  
 */
package com.cutefool.commons.ci.node;

import com.cutefool.common.ci.shell.Executors;
import com.cutefool.commons.ci.core.CiConsumer;
import com.cutefool.commons.ci.core.CiContext;
import org.apache.commons.exec.CommandLine;

import java.io.IOException;

/**
 * node 命令监测类
 *
 * @author 271007729@qq.com
 * @date 8/14/21 1:04 PM
 */
public class NodeChecker {


    public static boolean exists() {
        return Executors.exists(CommandLine.parse(NodeConstants.YARN_VERSION));
    }

    public static void install(CiContext ciContext, NodeContext context) throws IOException {
        CiConsumer ciConsumer = CiConsumer.empty(ciContext.getCiProcessConsumer());
        boolean exists = exists();
        ciConsumer.consumer(ciContext, "yarn " + (exists ? "exists" : "not exists."));
        if (!exists) {
            String execute = String.format(NodeConstants.YARN_INSTALL, context.getNpm());
            ciConsumer.consumer(ciContext, execute);
            Executors.execute(ciContext, CommandLine.parse(execute));
        }
    }
}
