/*
 *
 */
package com.cutefool.commons.ci.git.executing;

import com.cutefool.commons.ci.git.Gits;
import com.cutefool.commons.ci.git.Messages;
import com.cutefool.commons.ci.git.config.GitConfig;
import com.cutefool.commons.ci.git.config.GitConfigurable;
import com.cutefool.commons.ci.git.libs.GitMerge;
import com.cutefool.commons.ci.core.CiConsumer;
import com.cutefool.commons.ci.core.CiContext;
import com.cutefool.commons.ci.core.CiReleaseContext;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.spring.SpiSpringContext;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;

import java.util.Objects;

/**
 * git执行器
 *
 * @author 271007729@qq.com
 * @date 2022/9/13 10:51 PM
 */
public class GitExecutors {

    public static <T extends CiContext<T>> Response<Boolean> createBranch(T context) {

        try {
            Git git = Gits.open(context.getLocalRepository());
            // 创建本地分支
            CiConsumer.empty(context.getCiProcessConsumer())
                    .consumer(context, String.format(Messages.GIT_BRANCH_CREATE, context.getBranch()));
            Gits.create(git, context.getBranch());
            // 更新代码
            CiConsumer.empty(context.getCiProcessConsumer())
                    .consumer(context, String.format(Messages.GIT_PULL, context.getLocalRepository()));

            GitConfigurable oneSpi = SpiSpringContext.getOneSpi(GitConfigurable.class);
            GitConfig setting = oneSpi.config();
            Gits.pull(git, setting);

        } catch (Exception e) {
            CiConsumer.empty(context.getCiProcessConsumer()).consumer(context, e.getMessage());
            return Response.failure(e.getMessage());
        }
        return Response.ok();
    }

    public static <T extends CiContext<T>> Response<Boolean> commit(T context) {
        return commit(context, "commit by devops.");
    }

    public static <T extends CiContext<T>> Response<Boolean> commit(T context, String message) {
        try {
            CiConsumer.empty(context.getCiProcessConsumer())
                    .consumer(context, String.format(Messages.GIT_COMMIT, context.getLocalRepository()));
            GitConfigurable oneSpi = SpiSpringContext.getOneSpi(GitConfigurable.class);
            GitConfig config = oneSpi.config();
            Gits.commit(Gits.open(context.getLocalRepository()), config, message);
        } catch (Exception e) {
            CiConsumer.empty(context.getCiProcessConsumer()).consumer(context, e.getMessage());
            return Response.failure(e.getMessage());
        }
        return Response.ok();
    }

    public static <T extends CiContext<T>> Response<Boolean> push(T context) {
        try {
            Git git = Gits.open(context.getLocalRepository());
            // 提交代码
            CiConsumer.empty(context.getCiProcessConsumer())
                    .consumer(context, String.format(Messages.GIT_PUSH, context.getLocalRepository()));

            GitConfigurable oneSpi = SpiSpringContext.getOneSpi(GitConfigurable.class);
            GitConfig setting = oneSpi.config();
            Gits.push(git, setting);
        } catch (Exception e) {
            CiConsumer.empty(context.getCiProcessConsumer()).consumer(context, e.getMessage());
            return Response.failure(e.getMessage());
        }
        return Response.ok();
    }

    public static <T extends CiContext<T>> Response<Boolean> reset(T context) {

        try {
            CiConsumer.empty(context.getCiProcessConsumer())
                    .consumer(context, String.format(Messages.GIT_RESET, context.getLocalRepository()));
            Gits.reset(Gits.open(context.getLocalRepository()));
        } catch (Exception e) {
            CiConsumer.empty(context.getCiProcessConsumer()).consumer(context, e.getMessage());
            return Response.failure(e.getMessage());
        }
        return Response.ok();
    }

    public static <T extends CiReleaseContext<T>> Response<Boolean> tagExists(T context) {

        try {
            Git git = Gits.open(context.getLocalRepository());
            // 更新代码
            CiConsumer.empty(context.getCiProcessConsumer())
                    .consumer(context, String.format(Messages.GIT_CHECK_EXIST_TAG, context.getTag()));
            boolean b = Gits.tagExists(git, context.getTag());
            if (b) {
                CiConsumer.empty(context.getCiProcessConsumer())
                        .consumer(context, String.format(Messages.GIT_CHECK_EXIST_TAG_END, context.getTag()));
            }
            return Response.ok(!b, b ? "tag " + context.getTag() + " has exists." : "ok");
        } catch (Exception e) {
            CiConsumer.empty(context.getCiProcessConsumer()).consumer(context, e.getMessage());
            return Response.failure(e.getMessage());
        }
    }

    public static <T extends CiReleaseContext<T>> Response<Boolean> removeTag(T context) {
        try {
            Git git = Gits.open(context.getLocalRepository());
            CiConsumer.empty(context.getCiProcessConsumer())
                    .consumer(context, String.format(Messages.GIT_DELETE_TAG, context.getTag()));
            GitConfigurable oneSpi = SpiSpringContext.getOneSpi(GitConfigurable.class);
            GitConfig setting = oneSpi.config();
            Gits.removeTag(git, context.getTag(), setting);
        } catch (Exception e) {
            CiConsumer.empty(context.getCiProcessConsumer()).consumer(context, e.getMessage());
            return Response.failure(e.getMessage());
        }
        return Response.ok();
    }

    public static <T extends CiReleaseContext<T>> Response<Boolean> merge(T context, String source, String mergeTo) {
        try {
            // 原来分支
            source = StringUtils.isBlank(source) ? context.getBranch() : source;
            if (Objects.equals(mergeTo, source)) {
                return Response.ok();
            }

            if (StringUtils.isNotBlank(mergeTo)) {
                GitConfigurable oneSpi = SpiSpringContext.getOneSpi(GitConfigurable.class);
                GitConfig setting = oneSpi.config();

                CiConsumer<T> ciConsumer = CiConsumer.empty(context.getCiProcessConsumer());
                CiConsumer<T> ciLogConsumer = CiConsumer.empty(context.getCiLogsConsumer());

                Git git = Gits.open(context.getLocalRepository());
                // 创建目标分支
                Gits.create(git, mergeTo);

                // 切换到需要合并的分支
                git.checkout().setName(mergeTo).call();
                // 更新代码
                Gits.pull(git, setting);

                String message = "merge " + source + " to " + mergeTo;
                ciConsumer.consumer(context, message);

                GitMerge merge = new GitMerge();
                merge.setFrom(source);
                merge.setTo(mergeTo);
                merge.setMessage(message);
                Response<Boolean> mergeResponse = Gits.merge(git, setting, merge);
                boolean isSuccess = mergeResponse.isSuccess();
                ciConsumer.consumer(context, message + (isSuccess ? " success " : mergeResponse.getMsg()));
                // 合并代码失败
                return isSuccess ? Response.ok() : Response.failure(mergeResponse.getMsg());
            }
        } catch (Exception e) {
            CiConsumer.empty(context.getCiProcessConsumer()).consumer(context, e.getMessage());
            return Response.failure(e.getMessage());
        }
        return Response.ok();
    }

}
