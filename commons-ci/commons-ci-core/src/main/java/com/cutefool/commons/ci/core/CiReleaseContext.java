/*
 *
 */
package com.cutefool.commons.ci.core;

import lombok.Getter;

import java.io.File;

/**
 * CiContext
 *
 * @author 271007729@qq.com
 * @date 7/11/21 4:52 PM
 */
@Getter
@SuppressWarnings({"unused", "unchecked"})
public class CiReleaseContext<T extends CiReleaseContext<T>> extends CiDeployContext<T> {

    /**
     * 代码库分组
     */
    private String groupId;

    /**
     * 代码库骨架
     */
    private String artifactId;

    /**
     * 代码库tag号
     */
    private String tag;

    /**
     * 代码库稳定版本号
     */
    private String release;

    /**
     * 代码库开发版本号
     */
    private String snapshot;

    /**
     * 是否是组件
     */
    private Boolean component;

    /**
     * 文件路径
     */
    private File file;

    public T component(Boolean component) {
        this.component = component;
        return (T) this;
    }

    public T file(File file) {
        this.file = file;
        return (T) this;
    }

    public T groupId(String groupId) {
        this.groupId = groupId;
        return (T) this;
    }

    public T artifactId(String artifactId) {
        this.artifactId = artifactId;
        return (T) this;
    }

    public T tag(String tag) {
        this.tag = tag;
        return (T) this;
    }

    public T release(String release) {
        this.release = release;
        return (T) this;
    }

    public T snapshot(String snapshot) {
        this.snapshot = snapshot;
        return (T) this;
    }
}
