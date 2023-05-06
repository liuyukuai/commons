/*
 *
 */
package com.cutefool.commons.ci.spring;

import com.cutefool.commons.ci.webpack.*;
import com.cutefool.common.ci.shell.ShellExecutor;
import com.cutefool.commons.ci.core.CiConstants;
import com.cutefool.commons.ci.maven.MavenIdentifying;
import com.cutefool.commons.ci.maven.MavenVersioning;
import com.cutefool.commons.ci.maven.executing.MavenDeployExecuting;
import com.cutefool.commons.ci.maven.executing.MavenExecutor;
import com.cutefool.commons.ci.maven.executing.MavenReleaseExecuting;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CiSpringConfiguration
 *
 * @author 271007729@qq.com
 * @date 7/11/21 5:42 PM
 */
@Configuration
public class CiSpringConfiguration {

    @Bean(name = PrefixConst.PREFIX_DEPLOY + CiConstants.MAVEN)
    public MavenDeployExecuting mavenDeploying() {
        return new MavenDeployExecuting();
    }

    @Bean(name = PrefixConst.PREFIX_DEPLOY + CiConstants.WEBPACK)
    public WebpackDeployExecuting webpackDeploying() {
        return new WebpackDeployExecuting();
    }

    @Bean(name = PrefixConst.PREFIX_VERSIONING + CiConstants.MAVEN)
    public MavenVersioning mavenVersioning() {
        return new MavenVersioning();
    }

    @Bean(name = PrefixConst.PREFIX_VERSIONING + CiConstants.WEBPACK)
    public WebpackVersioning webpackVersioning() {
        return new WebpackVersioning();
    }

    @Bean(name = PrefixConst.PREFIX_RELEASE + CiConstants.MAVEN)
    public MavenReleaseExecuting mavenRelease() {
        return new MavenReleaseExecuting();
    }

    @Bean(name = PrefixConst.PREFIX_RELEASE + CiConstants.WEBPACK)
    public WebpackReleaseExecuting webpackReleaseExecuting() {
        return new WebpackReleaseExecuting();
    }

    @Bean(name = PrefixConst.PREFIX_IDENTIFIER + CiConstants.MAVEN)
    public MavenIdentifying mavenIdentifying() {
        return new MavenIdentifying();
    }

    @Bean(name = PrefixConst.PREFIX_IDENTIFIER + CiConstants.WEBPACK)
    public WebpackIdentifying webpackIdentifying() {
        return new WebpackIdentifying();
    }

    @Bean(name = PrefixConst.PREFIX_DEPLOY + CiConstants.SHELL)
    public ShellExecutor shellExecutor() {
        return new ShellExecutor<>();
    }

    @Bean(name = PrefixConst.PREFIX_EXECUTOR + CiConstants.MAVEN)
    public MavenExecutor mavenExecutor() {
        return new MavenExecutor<>();
    }

    @Bean(name = PrefixConst.PREFIX_EXECUTOR + CiConstants.WEBPACK)
    public WebpackExecutor webpackExecutor() {
        return new WebpackExecutor<>();
    }

}
