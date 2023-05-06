/*
 *
 */
package com.cutefool.commons.http.progress;

/**
 * @author 271007729@qq.com
 * @date 2021/12/20 11:14 PM
 */
@FunctionalInterface
public interface ProgressListener {

    /**
     * 监听文件下载
     *
     * @param libs libs
     */
    void listen(ProgressLibs libs);
}
