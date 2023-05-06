/*
 *
 */
package com.cutefool.commons.ci.maven;


import com.cutefool.commons.ci.maven.command.DefineCommand;
import com.cutefool.commons.ci.maven.command.DeployCommand;
import com.cutefool.commons.ci.maven.command.release.CleanCommand;
import com.cutefool.commons.ci.maven.command.release.PerformCommand;
import com.cutefool.commons.ci.maven.command.release.RollbackCommand;
import com.cutefool.commons.ci.maven.context.MavenContext;

/**
 * maven对象
 *
 * @author 271007729@qq.com
 * @date 5/31/21 2:14 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Maven {

    /**
     * context
     */
    private MavenContext context;


    public Maven(MavenContext context) {
        this.context = context;
    }

    public static Maven open(MavenContext context) {
        return new Maven(context);
    }

    public PerformCommand release() {
        return new PerformCommand(context);
    }


    public RollbackCommand rollback() {
        return new RollbackCommand(context);
    }


    public CleanCommand clean() {
        return new CleanCommand(context);
    }


    public DeployCommand deploy() {
        return new DeployCommand(context);
    }


    public DefineCommand define() {
        return new DefineCommand(context);
    }
}
