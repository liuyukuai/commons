/*
 *
 */
package com.cutefool.commons.ci.maven.command;


import com.cutefool.commons.ci.maven.context.MavenContext;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 5/31/21 4:21 PM
 */
public class DefineCommand extends BaseCommand {

    public DefineCommand(MavenContext context) {
        super(context);
    }

    public DefineCommand goals(List<String> goals) {
        setGoals(goals);
        return this;
    }
}
