/*
 *  
 */
package com.cutefool.commons.ci.node;

import lombok.Builder;
import lombok.Data;

import java.util.function.Consumer;

/**
 * @author 271007729@qq.com
 * @date 8/13/21 5:51 PM
 */
@Data
@Builder
public class NodeContext {

    /**
     * npm
     */
    private String npm;

    /**
     * 镜像
     */
    private String registry;

    /**
     * 日志
     */
    private Consumer<String> consumer;

}
