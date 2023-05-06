/*
 *
 */
package com.cutefool.commons.ci.maven.command.release;


import com.cutefool.commons.ci.maven.command.BaseCommand;
import com.cutefool.commons.ci.maven.context.MavenContext;

import java.util.Collections;

/**
 * maven命令执行类
 *
 * @author 271007729@qq.com
 * @date 5/31/21 2:08 PM
 */
public class CleanCommand extends BaseCommand {

    public CleanCommand(MavenContext context) {
        super(context);
        setGoals(Collections.singletonList("release:clean"));
    }
}
