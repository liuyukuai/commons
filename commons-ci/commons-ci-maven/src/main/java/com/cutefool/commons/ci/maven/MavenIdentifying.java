/*
 *
 */
package com.cutefool.commons.ci.maven;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.Identifier;
import com.cutefool.commons.ci.core.Identifying;
import com.cutefool.commons.version.Version;
import org.apache.maven.model.Model;

import java.io.File;

/**
 * @author 271007729@qq.com
 * @date 7/21/21 1:51 PM
 */
@SuppressWarnings("unused")
public class MavenIdentifying implements Identifying {

    @Override
    public <T extends CiContext<T>> Identifier identifier(T context) {
        Model model = Pom.load(new File(context.getLocalRepository(), context.getLocalSetting()));
        String version = model.getVersion();
        return new Identifier(model.getGroupId(), model.getArtifactId(), model.getPackaging(), new Version(version));
    }
}
