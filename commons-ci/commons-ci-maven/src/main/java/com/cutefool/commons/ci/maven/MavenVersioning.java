/*
 *
 */
package com.cutefool.commons.ci.maven;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.Dependency;
import com.cutefool.commons.ci.core.Versioning;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.SortedProperties;
import com.cutefool.commons.version.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.*;

import java.io.File;
import java.util.*;

/**
 * @author 271007729@qq.com
 * @date 7/21/21 1:51 PM
 */
@SuppressWarnings("unchecked")
public class MavenVersioning implements Versioning {

    @Override
    public <T extends CiContext<T>> Version version(T context) {
        Model model = this.load(context);
        String version = model.getVersion();
        return new Version(version);
    }

    @Override
    public <T extends CiContext<T>> Response<Boolean> updateProperties(T context, Map<String, String> props) {
        File file = new File(context.getLocalRepository(), context.getLocalSetting());
        Pom.write(file, (d) -> {
            Properties properties = d.getProperties();
            properties.putAll(props);
            d.setProperties(new SortedProperties(properties, false));
            return d;
        });
        return Response.ok();
    }

    @Override
    public <T extends CiContext<T>> Response<Boolean> update(T context, Dependency dependency) {
        File file = new File(context.getLocalRepository(), context.getLocalSetting());

        Pom.write(file, d -> {
            // 更新依赖
            this.update(context, Collections.singletonList(dependency), d);
            return d;
        });
        return Response.ok();

    }

    @Override
    public <T extends CiContext<T>> Response<Boolean> update(T context, List<Dependency> dependencies) {
        File file = new File(context.getLocalRepository(), context.getLocalSetting());
        this.update(context, file, dependencies);
        return Response.ok();

    }

    private <T extends CiContext<T>> void update(T context, File file, List<Dependency> dependencies) {
        Pom.write(file, (d) -> {
            // 更新依赖
            this.update(context, dependencies, d);
            // 排查属性
            d.setProperties(new SortedProperties(d.getProperties(), false));
            List<String> modules = d.getModules();
            // 修改子模块版本号
            if (Lists.iterable(modules)) {
                modules.forEach(module -> this.update(context, new File(file.getParent(), module + "/pom.xml"), dependencies));
            }
            return d;
        });
    }

    private <T extends CiContext<T>> void update(T context, List<Dependency> dependencies, Model model) {
        dependencies.forEach(e -> {
            // 更新父工程编码
            this.updateParent(model.getParent(), e);
            // 更新依赖（直接依赖）
            this.updateDependency(context, model.getDependencies(), model.getProperties(), e);

            // 更新包管理依赖
            DependencyManagement dependencyManagement = model.getDependencyManagement();
            if (Objects.nonNull(dependencyManagement)) {
                this.updateDependency(context, dependencyManagement.getDependencies(), model.getProperties(), e);
            }

            Build build = model.getBuild();
            if (Objects.isNull(build)) {
                return;
            }

            List<Plugin> plugins = build.getPlugins();
            Lists.empty(plugins).forEach(plugin -> {
                this.updateDependency(context, plugin.getDependencies(), model.getProperties(), e);
            });

        });
    }

    private void updateParent(Parent parent, Dependency dependency) {
        // 修改父项目编号
        if (Objects.equals(parent.getArtifactId(), dependency.getArtifactId()) && Objects.equals(parent.getGroupId(), dependency.getGroupId())) {
            parent.setVersion(dependency.getVersion());
        }
    }

    private <T extends CiContext<T>> void updateDependency(T context, List<org.apache.maven.model.Dependency> dependencies, Properties properties, Dependency dependency) {

        Map<String, String> maps = new HashMap<>(32);
        // 修改依赖编号
        Lists.empty(dependencies).forEach(e -> {
            if (Objects.equals(e.getArtifactId(), dependency.getArtifactId()) && Objects.equals(e.getGroupId(), dependency.getGroupId())) {
                boolean isProperties = this.isProperties(e.getVersion());
                // 如果原始版本为空，不修改
                String version = e.getVersion();
                if (StringUtils.isBlank(version)) {
                    context.getCiProcessConsumer()
                            .consumer(context, "update  " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "-> " + e.getVersion());
                    return;
                }

                if (isProperties) {
                    String key = this.getProperties(e);

                    String value = properties.getProperty(key);
                    if (!Objects.equals(value, dependency.getVersion()) && !Objects.equals(key, "project.parent.version")) {
                        context.getCiProcessConsumer()
                                .consumer(context, "update property " + key + " -> " + dependency.getVersion());
                        maps.put(key, dependency.getVersion());
                    }
                } else {
                    e.setVersion(dependency.getVersion());
                    context.getCiProcessConsumer()
                            .consumer(context, "update  " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "-> " + dependency.getVersion());
                }
            }
        });

        // 修改属性
        if (!maps.isEmpty()) {
            properties.putAll(maps);
        }
    }

    private boolean isProperties(String version) {
        return StringUtils.isNotBlank(version) && version.startsWith("${") && version.endsWith("}");
    }

    private String getProperties(org.apache.maven.model.Dependency dependency) {
        String version = dependency.getVersion();
        if (StringUtils.isBlank(version)) {
            return version;
        }
        if (this.isProperties(version)) {
            int startIndex = version.indexOf("${") + 2;
            int endIndex = version.indexOf("}");
            return version.substring(startIndex, endIndex);
        }
        return version;

    }

    private <T extends CiContext<T>> Model load(T context) {
        return Pom.load(new File(context.getLocalRepository(), context.getLocalSetting()));
    }
}
