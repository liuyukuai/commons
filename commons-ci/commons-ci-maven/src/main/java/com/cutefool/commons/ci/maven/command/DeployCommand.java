/*
 *
 */
package com.cutefool.commons.ci.maven.command;


import com.cutefool.commons.ci.maven.context.MavenContext;

import java.util.Arrays;

/**
 * 自定义命令
 *
 * @author 271007729@qq.com
 * @date 5/31/21 4:21 PM
 */
public class DeployCommand extends BaseCommand {

    public DeployCommand(MavenContext context) {
        super(context);
        setGoals(Arrays.asList("clean", "deploy"));
    }
}
