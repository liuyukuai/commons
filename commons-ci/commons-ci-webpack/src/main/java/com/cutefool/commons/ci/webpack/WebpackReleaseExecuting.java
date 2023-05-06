/*
 *  
 */
package com.cutefool.commons.ci.webpack;

import com.cutefool.commons.ci.git.Gits;
import com.cutefool.commons.ci.git.executing.GitExecutors;
import com.cutefool.commons.ci.node.NodeBuildExecuting;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.ci.core.CiConsumer;
import com.cutefool.commons.ci.core.CiReleaseContext;
import com.cutefool.commons.ci.core.Executing;
import com.cutefool.commons.ci.nexus.NexusUploadExecuting;
import com.cutefool.commons.ci.webpack.libs.Messages;
import com.cutefool.commons.ci.webpack.libs.WebpackConfig;
import com.cutefool.commons.ci.webpack.libs.Webpacks;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.packs.Packs;
import org.eclipse.jgit.api.Git;

import java.io.File;

/**
 * @author 271007729@qq.com
 * @date 8/13/21 11:14 AM
 */
@Slf4j
@SuppressWarnings("unchecked")
public class WebpackReleaseExecuting<T extends CiReleaseContext<T>> implements Executing<T, Boolean> {

    @Override
    public Response<Boolean> execute(T context) {
        CiConsumer<T> ciProcessConsumer = CiConsumer.empty(context.getCiProcessConsumer());
        CiConsumer<T> ciConsumer = CiConsumer.empty(context.getCiLogsConsumer());

        try {// webpack 创建tag
            ciProcessConsumer.consumer(context, Messages.WEBPACK_RELEASE_EXECUTING);

            // 如果是组件，不需要打包上传
            Boolean component = context.getComponent();
            if (!component) {
                // build
                NodeBuildExecuting<T> executing = new NodeBuildExecuting<>();
                Response<Boolean> response = executing.execute(context);
                // build failed
                if (response.isFailure()) {
                    ciProcessConsumer.consumer(context, String.format(Messages.WEBPACK_RELEASE_FAIL, response.getMsg()));
                    return response;
                }
                ciProcessConsumer.consumer(context, Messages.WEBPACK_RELEASE_SUCCESS);
                // 打包
                File dist = new File(context.getLocalRepository(), Webpack.DIST);
                // 文件不存在
                if (!dist.exists()) {
                    ciProcessConsumer.consumer(context, Messages.WEBPACK_DIST_DIR_NOT_EXISTS);
                    return Response.failure(Messages.WEBPACK_DIST_DIR_NOT_EXISTS);
                }

                File file = Webpack.packageName(context);

                ciProcessConsumer.consumer(context, String.format(Messages.WEBPACK_ZIP_EXECUTING, dist.getAbsolutePath()));
                Response<Boolean> execute = Packs.zip(dist.getAbsolutePath(), file.getAbsolutePath())
                                                 .streamConsumer(s -> ciConsumer.consumer(context, s))
                                                 .execute();

                // zip failed
                if (execute.isFailure()) {
                    String format = String.format(Messages.WEBPACK_ZIP_EXECUTING_FAILED, dist.getAbsolutePath());
                    ciProcessConsumer.consumer(context, format);
                    return Response.failure(format);
                }

                // 上传包到nexus
                context.file(file);
                ciProcessConsumer.consumer(context, String.format(Messages.WEBPACK_UPLOAD_ZIP_TO_NEXUS, file.getAbsolutePath()));
                NexusUploadExecuting<T> nexusUploadExecuting = new NexusUploadExecuting<>();
                response = nexusUploadExecuting.execute(context);
                if (!response.isSuccess()) {
                    String format = String.format(Messages.WEBPACK_UPLOAD_ZIP_TO_NEXUS_FAILED, file.getAbsolutePath(), response.getMsg());
                    ciProcessConsumer.consumer(context, format);
                    return response;
                }
                ciProcessConsumer.consumer(context, String.format(Messages.WEBPACK_UPLOAD_ZIP_TO_NEXUS_SUCCESS, file.getAbsolutePath()));
            }
            Git git = Gits.open(context.getLocalRepository());

            ciProcessConsumer.consumer(context, String.format(Messages.WEBPACK_CREATE_TAG, context.getTag()));
            // 设置 提交信息
            Gits.createTag(git, context.getTag(), "create tag " + context.getTag());
            // 修改当前版本号
            ciProcessConsumer.consumer(context, String.format(Messages.WEBPACK_UPDATE_VERSION, context.getLocalSetting(), context.getSnapshot()));
            WebpackConfig webpackConfig = Webpacks.loadPackageJson(context);
            webpackConfig.setVersion(context.getSnapshot());
            Webpacks.write(context, webpackConfig);
            GitExecutors.commit(context, "update version to " + context.getSnapshot());
            return Response.ok();
        } catch (Exception e) {
            ciProcessConsumer.consumer(context, String.format(Messages.WEBPACK_RELEASE_FAIL, e.getMessage()));
            log.warn(e.getMessage(), e);
            return Response.failure(e.getMessage());
        }
    }
}
