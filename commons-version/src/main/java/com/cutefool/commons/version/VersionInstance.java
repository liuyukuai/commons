/*
 *
 */
package com.cutefool.commons.version;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.cutefool.commons.expression.Expressions;

/**
 * 版本对象
 *
 * @author 271007729@qq.com
 * @date 7/21/21 1:31 PM
 */
@Data
@NoArgsConstructor
@SuppressWarnings({"unused"})
public class VersionInstance {

    /**
     * 当前版本号
     */
    private String version;

    /**
     * 下一个稳定版本
     */
    private String release;

    /**
     * 下一个开发版本
     */
    private String snapshot;

    /**
     * 下一个tag版本
     */
    private String tag;

    public VersionInstance(Version version, VersionRulesLibs versionRulesLibs) {
        this.setVersion(version.getVersion());
        String snapshot = Expressions.parseString(version, versionRulesLibs.getSnapshot());
        String tag = Expressions.parseString(version, versionRulesLibs.getTag());
        String release = Expressions.parseString(version, versionRulesLibs.getRelease());
        this.setSnapshot(snapshot);
        this.setRelease(release);
        this.setTag(tag);
    }

    public VersionInstance(Version version, VersionRules versionRules) {
        this.setVersion(version.getVersion());
        String snapshot = Expressions.parseString(version, versionRules.getSnapshot());
        String tag = Expressions.parseString(version, versionRules.getTag());
        String release = Expressions.parseString(version, versionRules.getRelease());
        this.setSnapshot(snapshot);
        this.setRelease(release);
        this.setTag(tag);
    }
}
