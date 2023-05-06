/*
 *
 */
package com.cutefool.commons.ci.webpack;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.Identifier;
import com.cutefool.commons.ci.core.Identifying;
import com.cutefool.commons.ci.webpack.libs.WebpackConfig;
import com.cutefool.commons.ci.webpack.libs.Webpacks;
import com.cutefool.commons.version.Version;

/**
 * @author 271007729@qq.com
 * @date 7/21/21 1:51 PM
 */
@SuppressWarnings("unused")
public class WebpackIdentifying implements Identifying {

    @Override
    public <T extends CiContext<T>> Identifier identifier(T context) {
        WebpackConfig webpackConfig = Webpacks.loadPackageJson(context);
        return new Identifier(webpackConfig.getGroupId(), webpackConfig.getName(), Webpack.SUFFIX, new Version(webpackConfig.getVersion()));
    }

}
