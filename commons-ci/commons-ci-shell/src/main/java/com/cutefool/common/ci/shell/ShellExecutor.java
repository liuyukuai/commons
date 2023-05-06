/*
 *
 */
package com.cutefool.common.ci.shell;

import com.cutefool.commons.ci.core.CiShellContext;
import com.cutefool.commons.ci.core.Executing;
import com.cutefool.commons.core.page.Response;
import org.apache.commons.exec.CommandLine;

import java.io.IOException;

/**
 * @author 271007729@qq.com
 * @date 7/11/21 5:09 PM
 */
@SuppressWarnings("unused")
public class ShellExecutor<T extends CiShellContext<T>> implements Executing<T, Boolean> {

    @Override
    public Response<Boolean> execute(T context) {
        try {
            context.getCiProcessConsumer()
                   .consumer(context, String.format(Messages.SHELL_EXECUTING, context.getExecute()));
            CommandLine commandline = CommandLine.parse(context.getExecute());
            Executors.execute(context, commandline);
            return Response.ok();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.failure(e.getMessage());
        }

    }
}
