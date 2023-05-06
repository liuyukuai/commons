/*
 *
 */
package com.cutefool.commons.ci.core;

import lombok.Getter;

import java.util.List;

/**
 * @author 271007729@qq.com
 * @date 8/14/21 4:26 PM
 */
@Getter
@SuppressWarnings({"unused", "unchecked", "unsafe"})
public class CiContext<T extends CiContext<T>> {

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * debug
     */
    private boolean debug;

    /**
     * 工具类型
     */
    private String type;

    /**
     * 分支
     */
    private String branch;

    /**
     * 项目本地目录
     */
    private String localRepository;

    /**
     * 项目配置文件
     */
    private String localSetting;


    /**
     * 需要合并到的目标分支
     */
    private List<String> merges;

    /**
     * 日志处理类
     */
    private CiConsumer<T> ciLogsConsumer;

    /**
     * 进程日志
     */
    private CiConsumer<T> ciProcessConsumer;

    public T id(Long id) {
        this.id = id;
        return (T) this;
    }

    public T type(String type) {
        this.type = type;
        return (T) this;
    }

    public T branch(String branch) {
        this.branch = branch;
        return (T) this;
    }

    public T localRepository(String localRepository) {
        this.localRepository = localRepository;
        return (T) this;

    }

    public T localSetting(String localSetting) {
        this.localSetting = localSetting;
        return (T) this;
    }

    public T merges(List<String> merges) {
        this.merges = merges;
        return (T) this;
    }

    @SuppressWarnings("rawtypes")
    public T ciLogsConsumer(CiConsumer ciLogsConsumer) {
        this.ciLogsConsumer = ciLogsConsumer;
        return (T) this;
    }

    @SuppressWarnings("rawtypes")
    public T ciProcessConsumer(CiConsumer ciProcessConsumer) {
        this.ciProcessConsumer = ciProcessConsumer;
        return (T) this;
    }
}
