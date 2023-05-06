/*
 *
 */
package com.cutefool.commons.ci.git;

import com.cutefool.commons.ci.git.config.GitConfig;
import com.cutefool.commons.ci.git.libs.GitContext;
import com.cutefool.commons.ci.git.libs.GitLibs;
import com.cutefool.commons.ci.git.libs.GitMerge;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.core.util.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * git操作功能类
 *
 * @author 271007729@qq.com
 * @date 5/25/21 4:04 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Gits {
    private Gits() {
    }

    /**
     * 默认git地址目录
     */
    private static final String DEFAULT_PATH = System.getProperty("user.home");

    public static GitContext path(GitLibs libs, GitConfig config) {
        // 目录
        String dir = config.getDir();
        dir = StringUtils.isBlank(dir) ? DEFAULT_PATH : dir;
        File file = new File(dir + File.separator + "gits", libs.getName());
        return new GitContext(libs.getName(), libs.getAddress(), file);
    }

    public static GitContext clone(GitLibs libs, GitConfig config) {
        // 本地git地址
        GitContext gitContext = path(libs, config);
        try {
            cloneRepository(gitContext.getAddress(), gitContext.getLocalPath(), config);
        } catch (Exception e) {
            throw new BizException(e);
        }
        return gitContext;
    }

    public static Repository cloneRepository(String git, File dest, GitConfig config) throws IOException, GitAPIException {
        // 克隆前清理目录
        FileUtils.deleteDirectory(dest);
        CloneCommand command = Git.cloneRepository().setURI(git).setDirectory(dest);
        // 如果ssh目录有配置，ssh优先
        init(command, config);
        try (Git result = command.call()) {
            Repository repository = result.getRepository();

            StoredConfig storedConfig = repository.getConfig();
            // 不校验文件权限
            storedConfig.setBoolean("core", null, "filemode", false);

            String username = config.getUsername();
            username = StringUtils.isNotBlank(username) ? username : "devops";
            String email = config.getEmail();
            email = StringUtils.isNotBlank(email) ? email : "ci@cutefool.com";

            // 设置用户名
            storedConfig.setString("user", null, "name", username);
            storedConfig.setString("user", null, "email", email);

            storedConfig.save();
            return repository;
        }
    }

    public static Git open(String localRepository) {
        try {
            return Git.open(new File(localRepository));
        } catch (IOException e) {
            throw new BizException(e);
        }
    }

    public static void commit(Git git, GitConfig config) throws GitAPIException {
        CommitCommand commit = git.commit();
        commit.setAll(true).call();
    }

    public static void commit(Git git, GitConfig config, String message) throws GitAPIException {
        CommitCommand commit = git.commit().setMessage(message).setAll(true);
        commit.call();
    }

    public static void pull(Git git, GitConfig config) throws GitAPIException {
        pull(git, config, true);
    }

    public static void pull(Git git, GitConfig config, boolean reset) throws GitAPIException {
        PullCommand pull = git.pull().setRebase(true);
        init(pull, config);
        pull.call();
    }

    public static void push(Git git, GitConfig config) throws GitAPIException {
        PushCommand pushCommand = git.push().setPushAll();
        init(pushCommand, config);
        pushCommand.call();
        pushCommand = git.push().setPushTags().setPushAll();
        init(pushCommand, config);
        pushCommand.call();
    }

    public static void removeTag(Git git, String tagName, GitConfig config) throws GitAPIException {
        git.tagDelete().setTags(tagName).call();
        RefSpec refSpec = new RefSpec().setSource(null).setDestination("refs/tags/" + tagName);
        if (Objects.nonNull(refSpec)) {
            PushCommand push = git.push();
            init(push, config);
            push.setRefSpecs(refSpec).setRemote("origin").call();
        }
    }

    public static void removeBranch(Git git, GitConfig config, String name) throws GitAPIException {

        // master分支校验 TODO

        // 先切换到其他分支
        List<Ref> call = git.branchList().call();
        if (Lists.isEmpty(call)) {
            return;
        }
        Optional<String> optional = call.stream()
                                        .filter(e -> !Objects.equals(e.getName(), name))
                                        .findAny()
                                        .map(Ref::getName);

        if (optional.isPresent()) {
            git.checkout().setName(optional.get()).setCreateBranch(false).call();
            git.branchDelete().setBranchNames(name).setForce(true).call();
            RefSpec refSpec = new RefSpec().setSource(null).setDestination("refs/heads/" + name);
            if (Objects.nonNull(refSpec)) {
                PushCommand push = git.push();
                init(push, config);
                push.setRefSpecs(refSpec).setRemote("origin").call();
            }
        }
    }

    public static void createTag(Git git, String tagName, String message) throws GitAPIException {
        git.tag().setMessage(message).setName(tagName).call();
    }

    public static boolean tagExists(Git git, String tagName) throws GitAPIException {
        Optional<Ref> optional = git.tagList().call().stream().filter(e -> {
            String name = e.getName();
            return Objects.equals(tagName, "refs/tags/" + name);
        }).findAny();
        return optional.isPresent();
    }

    public static void create(Git git, String name) throws GitAPIException {
        try {
            // 检测本地分支是否存在
            git.branchList().setContains(name).call();
            git.checkout().setName(name).call();
        } catch (RefNotFoundException e) {
            git.checkout()
               .setCreateBranch(true)
               .setName(name)
               .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
               .setStartPoint("origin/" + name)
               .call();
        }

    }

    public static List<String> branch(Git git) throws GitAPIException {
        List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
        return Lists.empty(refs)
                    .stream()
                    .map(Ref::getName)
                    .map(e -> e.replace("refs/remotes/origin/", ""))
                    .collect(Collectors.toList());

    }

    public static Response<Boolean> merge(Git git, GitConfig config, GitMerge gitMerge) throws GitAPIException {
        // 更新代码
        PullCommand pull = git.pull();
        init(pull, config);
        pull.call();

        // 检测本地分支是否存在
        create(git, gitMerge.getFrom());
        create(git, gitMerge.getTo());
        // 需要合并的分支
        Ref from = git.checkout().setCreateBranch(false).setName(gitMerge.getFrom()).call();
        // checkout to
        Ref to = git.checkout().setCreateBranch(false).setName(gitMerge.getTo()).call();
        // 合并
        MergeResult result = git.merge().setMessage(gitMerge.getMessage()).include(from).call();

        boolean success = result.getMergeStatus().isSuccessful();

        if (success) {
            // 推送
            Gits.push(git, config);
            return Response.ok();
        }

        // 重置代码
        git.revert().call();
        return Response.failure(result.getConflicts().toString());
    }

    public static Status status(Git git) throws GitAPIException {
        return git.status().call();
    }

    public static void reset(Git git) throws GitAPIException {
        git.reset().setMode(ResetCommand.ResetType.HARD).call();
    }

    private static <C extends GitCommand, T> void init(TransportCommand<C, T> command, GitConfig config) {
// 如果ssh目录有配置，ssh优先
        if (StringUtils.isNotBlank(config.getSshHome())) {
            command.setTransportConfigCallback(transport -> {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(new SshdSessionFactory() {
                    @Override
                    public File getSshDirectory() {
                        return new File(config.getSshHome());
                    }
                });
            });
        }

        // 如果使用用户名和密码方式
        if (StringUtils.isNotBlank(config.getUsername()) && StringUtils.isNotBlank(config.getPassword())) {
            command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(config.getUsername(), config.getPassword()));
        }
    }
}
