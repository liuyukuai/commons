/*
 *
 */
package com.cutefool.commons.version;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 271007729@qq.com
 * @date 6/1/21 2:34 PM
 */
@Data
@NoArgsConstructor
@SuppressWarnings({"unused", "WeakerAccess"})
public class VersionRulesLibs {

    /**
     * 类型
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 开发版本号
     */
    private String snapshot;

    /**
     * 稳定版本
     */
    private String release;

    /**
     * Tag版本号
     */
    private String tag;

    public VersionRulesLibs(VersionRules rules) {
        this.type = rules.getType();
        this.release = rules.getRelease();
        this.tag = rules.getTag();
        this.name = rules.getName();
        this.snapshot = rules.getSnapshot();
    }
}
