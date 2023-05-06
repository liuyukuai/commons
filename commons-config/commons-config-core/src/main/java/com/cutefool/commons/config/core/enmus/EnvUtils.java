package com.cutefool.commons.config.core.enmus;

import org.apache.commons.lang3.StringUtils;

public final class EnvUtils {

    public static Env transformEnv(String envName) {
        if (StringUtils.isBlank(envName)) {
            return Env.UNKNOWN;
        }
        switch (envName.trim().toUpperCase()) {
            case "FAT":
                return Env.FAT;
            case "UAT":
                return Env.UAT;
            case "PRO":
                return Env.PRO;
            case "DEV":
                return Env.DEV;
            default:
                return Env.UNKNOWN;
        }
    }
}
