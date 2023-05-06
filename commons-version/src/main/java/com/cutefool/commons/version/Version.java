/*
 *
 */
package com.cutefool.commons.version;

import lombok.Data;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Strings;

import java.util.List;
import java.util.Objects;

/**
 * 版本对象
 *
 * @author 271007729@qq.com
 * @date 7/21/21 1:31 PM
 */
@Data
@SuppressWarnings({"all"})
public class Version implements Comparable<Version> {

    /**
     * 主版本号
     */
    private Long major;


    /**
     * 子版本号
     */
    private Long minor;

    /**
     * 修正版本号
     */
    private Long revision;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 版本分组
     */
    private String group;

    /**
     * 版本号
     */
    private String version;

    /**
     * 版本前缀
     */
    private String prefix;

    public Version() {
    }

    public Version(String version) {
        this.version = version;
        List<String> versions = Strings.split(version, "-");
        if (Lists.iterable(versions)) {
            if (versions.size() == 2) {
                this.suffix = versions.get(1);
            }
            versions = Strings.split(versions.get(0), ".");
            if (Lists.iterable(versions)) {
                int size = versions.size();
                for (int i = size - 1; i >= 0; i--) {

                    if (i == size - 1) {
                        this.revision = Long.valueOf(versions.get(i));
                    }

                    if (i == size - 2) {
                        this.minor = Long.valueOf(versions.get(i));
                    }

                    if (i == size - 3) {
                        this.major = Long.valueOf(versions.get(i));
                    }
                }
            }
        }
        this.prefix = String.join(".", String.valueOf(this.major), String.valueOf(this.minor));
        this.group = String.join(".", String.valueOf(this.major), String.valueOf(this.minor), "x");
    }

    /**
     * 版本前缀
     *
     * @param version 版本
     * @return 版本前缀
     */
    public static String prefix(String version) {
        Version v = new Version(version);
        return String.join(".", String.valueOf(v.major), String.valueOf(v.minor));
    }

    @Override
    public int compareTo(Version version) {
        // 如果相等
        if (Objects.equals(version, this)) {
            return 0;
        }
        Long major1 = this.major;
        Long major2 = version.major;

        if (!Objects.equals(major1, major2)) {
            return major1.compareTo(major2);
        }

        Long minor1 = this.minor;
        Long minor2 = version.minor;

        if (!Objects.equals(minor1, minor2)) {
            return minor1.compareTo(minor2);
        }

        Long revision1 = this.revision;
        Long revision2 = version.revision;

        if (!Objects.equals(revision1, revision2)) {
            return revision1.compareTo(revision2);
        }

        return 0;
    }
}
