/*
 *  
 */
package com.cutefool.commons.http.progress;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 进度对象
 *
 * @author 271007729@qq.com
 * @date 2021/12/20 11:15 PM
 */
@Data
@AllArgsConstructor
public class ProgressLibs {

    /**
     * 文件名称
     */
    private String name;

    /**
     * 事件
     */
    private ProgressEvent event;

    /**
     * 速度
     */
    private double speed;

    /**
     * 百分比
     */
    private String percent;

    public static ProgressLibs start(String name) {
        return new ProgressLibs(name, ProgressEvent.START, 0D, "0");
    }

    public static ProgressLibs running(String name, double speed, String percent) {
        return new ProgressLibs(name, ProgressEvent.RUNNING, speed, percent);
    }

    public static ProgressLibs end(String name) {
        return new ProgressLibs(name, ProgressEvent.END, 0D, "100");
    }

    public static ProgressLibs error(String name) {
        return new ProgressLibs(name, ProgressEvent.ERROR, 0D, "");
    }


}
