/*
 *
 */
package com.cutefool.commons.version;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 271007729@qq.com
 * @date 7/14/21 3:17 PM
 */
@SuppressWarnings("unused")
public enum VersionRules {

    /**
     * 版本号规则
     */
    REVISION("revision",
            "修正版本号",
            "{major}.{minor}.{revision+1}-SNAPSHOT",
            "{major}.{minor}.{revision}",
            "v{major}.{minor}.{revision}"
    ),
    MINOR("minor",
            "子版本号",
            "{major}.{minor+1}.{1}-SNAPSHOT",
            "{major}.{minor+1}.{0}",
            "v{major}.{minor+1}.{0}"
    ),
    MAJOR("major",
            "主版本号",
            "{major+1}.{0}.{1}-SNAPSHOT",
            "{major+1}.{0}.{0}",
            "v{major+1}.{0}.{0}"
    );

    private String type;

    private String name;

    private String snapshot;

    private String release;

    private String tag;


    VersionRules(String type, String name, String snapshot, String release, String tag) {
        this.type = type;
        this.name = name;
        this.snapshot = snapshot;
        this.release = release;
        this.tag = tag;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public String getRelease() {
        return release;
    }

    public String getTag() {
        return tag;
    }

    public static VersionRules byType(String type) {
        for (VersionRules e : VersionRules.values()) {
            if (Objects.equals(e.getType(), type)) {
                return e;
            }
        }
        return null;
    }

    public static List<VersionRulesLibs> libs() {
        return Stream.of(VersionRules.values())
                .map(VersionRulesLibs::new)
                .collect(Collectors.toList());

    }

}
