/*
 *
 */
package com.cutefool.commons.ci.core;

/**
 * ciContext factory
 *
 * @author 271007729@qq.com
 * @date 8/14/21 4:31 PM
 */
@SuppressWarnings("unused")
public class CiContextFactory {

    public static CiContext ciContext() {
        return new CiContext<>();
    }

    public static CiDeployContext deploy() {
        return new CiDeployContext<>();
    }

    public static CiReleaseContext release() {
        return new CiReleaseContext<>();
    }
}
