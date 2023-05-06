/*
 *  
 */
package com.cutefool.commons.ci.pipeline;

import com.cutefool.commons.ci.core.*;
import com.cutefool.commons.ci.git.executing.GitExecutors;
import com.cutefool.commons.ci.maven.Pom;
import com.cutefool.commons.ci.spring.*;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.ProcessUtils;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.ci.core.*;
import com.cutefool.commons.ci.nexus.NexusRemoveExecuting;
import com.cutefool.commons.ci.spring.*;
import com.cutefool.commons.version.Version;
import org.apache.maven.model.Model;

import java.io.File;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 7/11/21 11:16 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Pipelines {
    private Pipelines() {
    }

    public static <T extends CiDeployContext<T>> Response<Boolean> deploy(T context) {

        // 第一步，创建本地分支
        Function<T, Response<Boolean>> createFunction = c -> GitExecutors.createBranch(context);
        // 第二步
        createFunction = createFunction.andThen(res -> {
            if (res.isSuccess()) {
                return Deploys.deploy(context);
            }
            return res;
        });
        return createFunction.apply(context);
    }

    public static <T extends CiContext<T>> Response<Boolean> versioning(T context, List<Dependency> dependencies) {

        // 第一步，创建本地分支
        Function<T, Response<Boolean>> createFunction = c -> GitExecutors.createBranch(context);

        // 第二步排序
        createFunction = createFunction.andThen(res -> CiExecutors.sort(context))
                .andThen(res -> GitExecutors.commit(context, "sort pom.xml by devops "));

        // 第三步
        createFunction = createFunction.andThen(res -> {
            if (res.isSuccess()) {
                return Versionings.update(context, dependencies);
            }
            return res;
        })
                // 提交代码
                .andThen(res -> {
                    if (res.isSuccess()) {
                        // message
                        String message = Lists.empty(dependencies)
                                .stream()
                                .map(e -> e.getGroupId() + Strings.COLON + e.getVersion())
                                .distinct()
                                .collect(Collectors.joining(Strings.COMMA));
                        return GitExecutors.commit(context, "update " + message);
                    }
                    return res;
                });

        return createFunction.apply(context);
    }

    public static <T extends CiContext<T>> Version version(T context) {
        Response<Boolean> response = GitExecutors.createBranch(context);
        if (response.isFailure()) {
            throw new BizException(ResponseCode.SYSTEM_ERROR.getCode(), context.getLocalRepository() + " " + response.getMsg());
        }
        return Versionings.version(context);
    }

    public static <T extends CiContext<T>> Identifier identifier(T context) {
        Response<Boolean> response = GitExecutors.createBranch(context);
        if (response.isFailure()) {
            throw new BizException(response.getCode(), context.getLocalRepository() + " " + response.getMsg());
        }
        return Identifiers.identifier(context);
    }

    public static <T extends CiReleaseContext<T>> Response<Boolean> release(T context) {
        return release(context, (ctx, res) -> {
        });
    }

    public static <T extends CiReleaseContext<T>> Response<Boolean> release(T context, BiConsumer<T, Response<Boolean>> consumer) {
        // 如果分支已经存在
        Response<Boolean> response = GitExecutors.tagExists(context);
        if (response.isFailure()) {
            return response;
        }

        // 第一步，创建本地分支
        Function<T, Response<Boolean>> createFunction = c -> GitExecutors.createBranch(context);

        // 发布版本
        createFunction = createFunction.andThen(res -> {
            if (res.isSuccess()) {
                return Releases.release(context);
            }
            return res;
        })
                // 重置代码
                .andThen(res -> {
                    if (res.isFailure()) {
                        // 代码回滚
                        GitExecutors.reset(context);
                    }
                    return res;
                })

                // 删除tag
                .andThen(res -> {
                    if (res.isFailure()) {
                        // 删除tag
                        GitExecutors.removeTag(context);

                    }
                    return res;
                })

                // 删除nexus中的jar包
                .andThen(res -> {
                    // 如果失败，删除已经发布的jar包
                    if (res.isFailure()) {
                        NexusRemoveExecuting<T> nexusRemoveExecuting = new NexusRemoveExecuting<>();
                        nexusRemoveExecuting.execute(context);
                    }
                    return res;
                })
                // 提交代码
                .andThen(res -> {
                    if (res.isSuccess()) {
                        return GitExecutors.push(context);
                    }
                    return res;
                })
                // 合并代码
                .andThen(res -> {
                    List<String> merges = context.getMerges();
                    if (res.isSuccess() && Lists.iterable(merges)) {
                        // 合并代码不影响打包
                        String source = "";
                        for (String merge : merges) {
                            Response<Boolean> mergeRes = GitExecutors.merge(context, source, merge);
                            if (mergeRes.isFailure()) {
                                // 重置代码
                                GitExecutors.reset(context);
                            }
                            source = merge;
                        }
                    }
                    return res;
                });

        // 合并代码不影响最终结果
        Response<Boolean> res = createFunction.apply(context);
        consumer.accept(context, res);
        return res;
    }

    @SuppressWarnings("unchecked")
    private static <T extends CiReleaseContext<T>> void removeChildren(T context, NexusRemoveExecuting nexusRemoveExecuting) {
        // 查询子模块
        Model model = Pom.load(new File(context.getLocalRepository(), context.getLocalSetting()));
        List<String> modules = model.getModules();
        Lists.empty(modules).forEach(e -> {
            CiReleaseContext<T> ctx = new CiReleaseContext<>();
            ProcessUtils.processObject(ctx, context, (d, s) -> {
                d.artifactId(e);
                d.localSetting(e + File.separator + "pom.xml");
            });
            nexusRemoveExecuting.execute(ctx);
            removeChildren((T) ctx, nexusRemoveExecuting);
        });
    }
}
