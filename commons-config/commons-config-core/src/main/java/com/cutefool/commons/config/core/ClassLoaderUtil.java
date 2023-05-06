package com.cutefool.commons.config.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.net.URLDecoder;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@Slf4j
public class ClassLoaderUtil {

    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private static String classPath = "";

    static {
        if (loader == null) {
            log.warn("Using system class loader");
            loader = ClassLoader.getSystemClassLoader();
        }

        try {
            URL url = loader.getResource("");
            // get class path
            if (url != null) {
                classPath = url.getPath();
                classPath = URLDecoder.decode(classPath, "utf-8");
            }

            // 如果是jar包内的，则返回当前路径
            if (StringUtils.isBlank(classPath) || classPath.contains(".jar!")) {
                classPath = System.getProperty("user.dir");
            }
        } catch (Throwable ex) {
            classPath = System.getProperty("user.dir");
            log.warn("Failed to locate class path, fallback to user.dir: {}", classPath, ex);
        }
    }

    public static ClassLoader getLoader() {
        return loader;
    }
}
