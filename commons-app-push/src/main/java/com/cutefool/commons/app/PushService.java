/*
 *
 */
package com.cutefool.commons.app;

/**
 * 推送接口
 *
 * @author 271007729@qq.com
 * @date 2020/4/23 11:38 AM
 */
@SuppressWarnings("unused")
public interface PushService {

    /**
     * 推送消息
     *
     * @param notice 消息
     */
    void push(AppNotice notice);
}
