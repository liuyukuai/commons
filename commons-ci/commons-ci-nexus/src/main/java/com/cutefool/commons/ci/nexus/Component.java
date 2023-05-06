/*
 *
 */
package com.cutefool.commons.ci.nexus;

import lombok.Data;

import java.io.Serializable;

/**
 * 组件
 *
 * @author 271007729@qq.com
 * @date 7/27/21 3:40 PM
 */
@Data
public class Component implements Comparable<Component>, Serializable {

    private String id;

    private String repository;

    private String group;

    private String name;

    private String version;

    private String format;

    @Override
    public int compareTo(Component o) {
        return this.version.compareTo(o.getVersion());
    }
}
