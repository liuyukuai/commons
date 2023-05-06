/*
 *
 */
package com.cutefool.commons.core.util;

import com.cutefool.commons.core.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author 271007729@qq.com
 * @date 8/13/21 5:14 PM
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class FilesUtils {

    public static String randomDir() {
        File file = new File(Constants.TEMP_DIR, UUID.randomUUID().toString());
        FilesUtils.mkdirs(file);
        return file.getAbsolutePath();
    }

    public static String suffix(File file) {
        if (Objects.isNull(file)) {
            return Strings.EMPTY;
        }

        return suffix(file.getName());
    }


    public static String suffix(String file) {
        int index = file.lastIndexOf(".");

        if (index != -1) {
            return file.substring(index + 1);
        }
        return Strings.EMPTY;
    }

    public static boolean mkdirs(String file) {
        try {
            return Files.createDirectories(Paths.get(file)).toFile().exists();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            return false;
        }
    }

    public static boolean mkdirs(File file) {
        if (Objects.isNull(file)) {
            return false;
        }
        return mkdirs(file.getAbsolutePath());
    }

    public static boolean delete(String dir) {
        try {
            Path path = Paths.get(dir);
            File file = path.toFile();
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (Objects.nonNull(files)) {
                    for (File f : files) {
                        boolean success = delete(f);
                        if (!success) {
                            return false;
                        }
                    }
                }
            }
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(File file) {
        if (Objects.isNull(file)) {
            return false;
        }
        return delete(file.getAbsolutePath());
    }

    public static void close(Closeable... closeables) {
        Stream.of(closeables)
                .filter(Objects::nonNull)
                .forEach(closeable -> {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                });
    }
}
