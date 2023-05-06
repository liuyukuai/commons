/*
 *
 */
package com.cutefool.commons.ci.maven.command.release;


import com.cutefool.commons.ci.maven.command.BaseCommand;
import com.cutefool.commons.ci.maven.context.MavenContext;

import java.util.Arrays;
import java.util.Collections;

/**
 * maven命令执行类
 *
 * @author 271007729@qq.com
 * @date 5/31/21 2:08 PM
 */
public class PerformCommand extends BaseCommand {


    public PerformCommand(MavenContext context) {
        super(context);
        setGoals(Arrays.asList("-B", "release:prepare", "release:perform", "-Darguments=-Dmaven.test.skip=true", "-Darguments=-Dmaven.javadoc.skip=true"));
    }

    public PerformCommand tag(String tag) {
        setGoals(Collections.singletonList("-Dtag=" + tag));
        return this;
    }

    public PerformCommand release(String version) {
        setGoals(Collections.singletonList("-DreleaseVersion=" + version));
        return this;
    }

    public PerformCommand snapshot(String version) {
        setGoals(Collections.singletonList("-DdevelopmentVersion=" + version));
        return this;
    }
}
