/*
 *
 */
package com.cutefool.commons.job.core.job.enums;

/**
 * @author 271007729@qq.com
 * @date 9/6/21 5:15 PM
 */
public enum JobState {

    /**
     * 执行任务状态
     */
    WAIT((byte) 0, "待执行"),

    EXECUTING((byte) 1, "执行中"),

    FINISHED((byte) 2, "完成");

    private Byte key;
    private String name;

    JobState(Byte key, String name) {
        this.key = key;
        this.name = name;
    }

    public Byte getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
