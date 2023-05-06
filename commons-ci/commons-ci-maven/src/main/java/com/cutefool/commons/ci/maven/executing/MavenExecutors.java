/*
 *
 */
package com.cutefool.commons.ci.maven.executing;

import com.cutefool.commons.ci.maven.DependencyComparator;
import com.cutefool.commons.ci.maven.Pom;
import com.cutefool.commons.ci.maven.context.MavenContext;
import com.cutefool.commons.ci.maven.libs.Messages;
import com.cutefool.commons.ci.core.CiConsumer;
import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.SortedProperties;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * maven 执行器
 *
 * @author 271007729@qq.com
 * @date 2022/9/14 12:47 AM
 */
@SuppressWarnings("unused")
public final class MavenExecutors {
    private MavenExecutors() {
    }

    public static <T extends CiContext<T>> Response<Boolean> sort(T context) {
        CiConsumer<T> ciProcessConsumer = CiConsumer.empty(context.getCiProcessConsumer());
        MavenContext mavenContext = MavenContext.init(context);
        ciProcessConsumer.consumer(context, Messages.MAVEN_SORT_EXECUTING);
        File pomFile = new File(mavenContext.getLocalRepository(), mavenContext.getPom());

        Pom.write(pomFile, d -> {
            // 排序配置
            d.setProperties(sort(d.getProperties()));
            // 排序依赖
            sort(d.getDependencies());
            // 排序
            DependencyManagement dependencyManagement = d.getDependencyManagement();
            if (Objects.nonNull(dependencyManagement)) {
                sort(dependencyManagement.getDependencies());
            }

            Build build = d.getBuild();
            if (Objects.nonNull(build)) {
                Lists.empty(build.getPlugins()).forEach(e -> {
                    sort(e.getDependencies());
                });

            }
            return d;
        });

        ciProcessConsumer.consumer(context, Messages.MAVEN_SORT_SUCCESS);
        return Response.ok();
    }

    public static Properties sort(Properties properties) {
        return new SortedProperties(properties, false);
    }

    public static void sort(List<Dependency> dependencies) {

        Lists.empty(dependencies)
             .sort(Comparator.comparing(Dependency::getGroupId, new DependencyComparator())
                             .thenComparing(Dependency::getArtifactId, new DependencyComparator()));
    }
}
