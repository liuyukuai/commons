/*
 *
 */
package com.cutefool.common.ci.shell;

import com.cutefool.commons.ci.core.CiContext;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.File;
import java.io.IOException;

/**
 * @author 271007729@qq.com
 * @date 8/13/21 6:22 PM
 */
public class Executors {


    public static void execute(CiContext context, CommandLine commandline) throws IOException {
        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(new File(context.getLocalRepository()));
        // 输出流
        ShellOutputStream shellOutputStream = new ShellOutputStream(context);
        PumpStreamHandler streamHandler = new PumpStreamHandler(shellOutputStream, shellOutputStream);
        executor.setStreamHandler(streamHandler);
        executor.execute(commandline);
    }

    public static boolean exists(CommandLine commandline) {
        try {
            DefaultExecutor executor = new DefaultExecutor();
            int execute = executor.execute(commandline);
            return !executor.isFailure(execute);
        } catch (IOException e) {
            return false;
        }
    }


}
