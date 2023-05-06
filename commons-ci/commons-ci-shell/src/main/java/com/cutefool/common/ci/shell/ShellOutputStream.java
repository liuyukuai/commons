/*
 *
 */
package com.cutefool.common.ci.shell;

import com.cutefool.commons.ci.core.CiContext;
import org.apache.commons.exec.LogOutputStream;

/**
 * @author 271007729@qq.com
 * @date 7/20/21 3:47 PM
 */
public class ShellOutputStream extends LogOutputStream {

    private CiContext ciContext;


    public ShellOutputStream(CiContext ciContext) {
        this.ciContext = ciContext;
    }


    @Override
    protected void processLine(String line, int logLevel) {
        ciContext.getCiLogsConsumer().consumer(this.ciContext, line);
    }
}
