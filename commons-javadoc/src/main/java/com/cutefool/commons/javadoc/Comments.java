/*
 *
 */
package com.cutefool.commons.javadoc;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.stream.Stream;

/**
 * 查询指定的注释
 *
 * @author 271007729@qq.com
 * @date 2019-09-06 09:52
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Comments {

    public static String author(String comments) {
        if (StringUtils.isBlank(comments)) {
            return "";
        }
        return Stream.of(StringUtils.split(comments, "\n"))
                .filter(e -> e.toLowerCase().contains("author"))
                .map(e -> StringUtils.split(e, " "))
                .findFirst()
                .filter(e -> Array.getLength(e) > 1)
                .map(e -> e[1].trim()).orElse("");


    }

    public static Long id(String comments) {
        if (StringUtils.isBlank(comments)) {
            return null;
        }
        System.out.println(comments);
        return Stream.of(StringUtils.split(comments, "\n"))
                .filter(e -> e.toLowerCase().contains("@see"))
                .map(e -> StringUtils.split(e, " "))
                .findFirst()
                .filter(e -> Array.getLength(e) > 1)
                .map(e -> e[1].trim())
                .map(e -> Long.valueOf(e.replace(".", "")))
                .orElse(null);

    }


    public static String params(String name, String comments) {
        if (StringUtils.isBlank(comments)) {
            return "";
        }
        return Stream.of(StringUtils.split(comments, "\n"))
                .filter(e -> e.toLowerCase().contains(" @param " + name))
                .map(e -> StringUtils.split(e, " "))
                .findFirst()
                .filter(e -> Array.getLength(e) > 2)
                .map(e -> e[2].trim())
                .orElse("");

    }
}
