/*
 *
 */
package com.cutefool.commons.core;

import java.util.Objects;

/**
 * 访问模式
 *
 * @author 271007729@qq.com
 * @date 2022/5/16 3:13 PM
 */
@SuppressWarnings("unused")
public enum AccessMode {
    /**
     * 子域名
     */
    SUBDOMAIN,
    /**
     * 地址后缀拼接的方式
     */
    DOMAIN_SUFFIX;

    public boolean isSubdomain() {
        return Objects.equals(this, SUBDOMAIN);
    }

    public boolean isDomainSuffix() {
        return Objects.equals(this, DOMAIN_SUFFIX);
    }
}
