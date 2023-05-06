/*
 *
 */
package com.cutefool.commons.ci.webpack.libs;

import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.JsonUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Webpack工具类
 *
 * @author 271007729@qq.com
 * @date 8/13/21 10:15 AM
 */
public final class Webpacks {

    public static WebpackConfig loadPackageJson(CiContext context) {
        try {
            String json = FileUtils.readFileToString(new File(context.getLocalRepository(), context.getLocalSetting()), StandardCharsets.UTF_8);
            Map<String, Object> configs = JsonUtils.toMap(json)
                    .orElseThrow(() -> new BizException(ResponseCode.SYSTEM_ERROR.getCode(), "webpack config parse error"));
            WebpackConfig webpackConfig = new WebpackConfig();
            webpackConfig.setConfigs(configs);
            return webpackConfig;
        } catch (IOException e) {
            throw new BizException(e);
        }
    }

    public static void write(CiContext context, WebpackConfig webpackConfig) {
        try {
            webpackConfig.setVersion(webpackConfig.getVersion());
            webpackConfig.setDependencies(webpackConfig.getDependencies());
            String json = JsonUtils.toJson(webpackConfig.getConfigs(), true);
            FileUtils.write(new File(context.getLocalRepository(), context.getLocalSetting()), json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new BizException(e);
        }
    }
}
