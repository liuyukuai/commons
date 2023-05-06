/*
 *  
 */
package com.cutefool.commons.ci.git.listener;


import com.cutefool.commons.ci.git.event.Event;

/**
 * @author 271007729@qq.com
 * @date 5/26/21 11:53 AM
 */
public interface GitListener {

    /**
     * git事件监听器
     *
     * @param event 事件
     */
    void onEvent(Event event);
}
