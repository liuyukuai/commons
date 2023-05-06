/*
 *
 */
package com.cutefool.commons.ci.webpack;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.CiReleaseContext;

import java.io.File;

/**
 * @author 271007729@qq.com
 * @date 8/14/21 1:30 PM
 */
public class Webpack {

    /**
     * 打包后缀
     */
    public static final String SUFFIX = "zip";

    /**
     * dist
     */
    public static final String DIST = "dist";

    public static final String GIT_SSH = "git+ssh";

    public static final File packageName(CiContext context) {
        CiReleaseContext ciReleaseContext = (CiReleaseContext) context;
        String artifactId = ciReleaseContext.getArtifactId();
        String tag = ciReleaseContext.getTag();
        return new File(context.getLocalRepository(), artifactId + "-" + tag + "." + SUFFIX);
    }
}
