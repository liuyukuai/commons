/*
 *
 */
package com.cutefool.commons.ci.webpack;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.Dependency;
import com.cutefool.commons.ci.core.Versioning;
import com.cutefool.commons.ci.webpack.libs.WebpackConfig;
import com.cutefool.commons.ci.webpack.libs.WebpackConstants;
import com.cutefool.commons.ci.webpack.libs.Webpacks;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.version.Version;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author 271007729@qq.com
 * @date 8/13/21 10:49 AM
 */
public class WebpackVersioning implements Versioning {

    @Override
    public  <T extends CiContext<T>> Version version(T context) {
        WebpackConfig webpackConfig = Webpacks.loadPackageJson(context);
        return new Version(webpackConfig.getVersion());
    }

    @Override
    public  <T extends CiContext<T>> Response<Boolean> updateProperties(T context, Map<String, String> props) {
        return Response.ok();
    }

    @Override
    public  <T extends CiContext<T>> Response<Boolean> update(T context, Dependency dependency) {
        return this.update(context, dependency);
    }

    @Override
    public  <T extends CiContext<T>> Response<Boolean> update(T context, List<Dependency> dependencies) {
        WebpackConfig webpackConfig = Webpacks.loadPackageJson(context);
        Map<String, String> dependenciesMap = webpackConfig.getDependencies();
        Lists.empty(dependencies).forEach(e -> {
            // 原来的值
            String s = dependenciesMap.get(e.getArtifactId());
            // 兼容 @
            if (StringUtils.isBlank(s)) {
                s = dependenciesMap.get("@" + e.getArtifactId());
            }
            if (StringUtils.isNotBlank(s)) {
                int index = s.indexOf(WebpackConstants.TAG_SEP);
                // 如果是tag配置
                if (index != -1) {
                    dependenciesMap.put(e.getArtifactId(), s.split(WebpackConstants.TAG_SEP)[0] + WebpackConstants.TAG_SEP + e.getVersion());
                }

                // 如果是git开头
                else if (s.startsWith(Webpack.GIT_SSH)) {
                    dependenciesMap.put(e.getArtifactId(), s + WebpackConstants.TAG_SEP + e.getVersion());
                    // 其他情况 直接版本号
                } else {
                    dependenciesMap.put(e.getArtifactId(), e.getVersion());
                }
            }
        });

        // 写入文件
        Webpacks.write(context, webpackConfig);
        return Response.ok();
    }
}
