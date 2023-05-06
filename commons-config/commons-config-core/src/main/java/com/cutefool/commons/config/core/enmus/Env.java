package com.cutefool.commons.config.core.enmus;

public enum Env {
    DEV, FAT, UAT, PRO, UNKNOWN;

    public static Env fromString(String env) {
        return EnvUtils.transformEnv(env);
    }
}
