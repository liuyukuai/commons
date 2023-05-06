/*
 *
 */
package com.cutefool.commons.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 字符串工具类
 *
 * @author 271007729@qq.com
 * @date 2019/12/3 12:55 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Strings {
    /**
     * 逗号常量
     */
    public static final String COMMA = ",";

    /**
     * 点
     */
    public static final String POINT = ".";

    /**
     * 分号
     */
    public static final String SEMICOLON = ";";

    /**
     * 冒号常量
     */
    public static final String COLON = ":";

    /**
     * 斜线
     */
    public static final String SLASH = "/";

    /**
     * 空字符
     */
    public static final String EMPTY = "";

    /**
     * =
     */
    public static final String EQUALS = "=";

    /**
     * #
     */
    public static final String HASH_TAG = "#";

    /**
     * _
     */
    public static final String UNDERLINE = "_";

    /**
     * -
     */
    public static final String LINE_THROUGH = "-";

    /**
     * ?
     */
    public static final String LINE_QUESTION_MARK = "?";

    /**
     * (
     */
    public static final String LEFT_PARENTHESES = "(";

    /**
     * )
     */
    public static final String RIGHT_PARENTHESES = ")";

    /**
     * [
     */
    public static final String LEFT_BRACKETS = "[";

    /**
     * ]
     */
    public static final String RIGHT_BRACKETS = "]";

    /**
     * {
     */
    public static final String LEFT_BRACES = "{";

    /**
     * }
     */
    public static final String RIGHT_BRACES = "}";

    public static <T> String empty(Object s) {
        return Objects.isNull(s) ? Strings.EMPTY : s.toString();
    }

    public static <T> String join(List<T> strings) {
        return join(strings, COMMA);
    }

    public static <T> String join(List<T> strings, String separator) {
        return Lists.iterable(strings) ? StringUtils.join(strings, separator) : EMPTY;
    }

    public static <T> String sql(Collection<T> strings) {
        if (Objects.isNull(strings) || strings.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return strings.stream().filter(Objects::nonNull).map(e -> {
            boolean creatable = NumberUtils.isCreatable(e.toString());
            if (creatable) {
                return e.toString();
            }
            return "'" + StringEscapeUtils.escapeXSI(e.toString()) + "'";
        }).collect(Collectors.joining(COMMA));

    }

    public static <T> String sql(Object value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException();
        }
        boolean creatable = NumberUtils.isCreatable(value.toString());

        if (creatable) {
            return value.toString();
        }
        return "'" + StringEscapeUtils.escapeXSI(value.toString()) + "'";
    }

    public static List<String> split(String strings) {
        return split(strings, COMMA);
    }

    public static List<String> split(String strings, String separator) {
        return StringUtils.isNotBlank(strings) ? Stream.of(StringUtils.split(strings, separator))
                .collect(Collectors.toList()) : Lists.newArrayList();
    }

    public static List<Long> split2Long(String strings) {
        return split2Long(strings, COMMA);
    }

    public static List<Long> split2Long(String strings, String separator) {
        return StringUtils.isNotBlank(strings) ? Arrays.stream(StringUtils.split(strings, separator))
                .map(Long::valueOf)
                .collect(Collectors.toList()) : Lists.newArrayList();
    }

    public static List<Integer> split2Int(String strings) {
        return split2Int(strings, COMMA);
    }

    public static List<Integer> split2Int(String strings, String separator) {
        return StringUtils.isNotBlank(strings) ? Arrays.stream(StringUtils.split(strings, separator))
                .map(Integer::parseInt)
                .collect(Collectors.toList()) : Lists.newArrayList();
    }

    public static String firstLower(String name) {
        char c = name.charAt(0);
        String firstLetter = name.substring(0, 1);
        return name.replaceFirst(firstLetter, firstLetter.toLowerCase());
    }

    public static String firstUpper(String name) {
        char c = name.charAt(0);
        String firstLetter = name.substring(0, 1);
        return name.replaceFirst(firstLetter, firstLetter.toUpperCase());
    }

    public static String underscore(String name) {
        return underscore(name, Strings.UNDERLINE);
    }

    public static String underscore(String name, String separator) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(name);
        StringBuffer sb = new StringBuffer();

        AtomicLong counter = new AtomicLong(0);
        while (matcher.find()) {
            matcher.appendReplacement(sb, (counter.getAndIncrement() > 0 ? separator : "") + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static boolean isNull(Object name) {
        if (Objects.isNull(name)) {
            return true;
        }
        if (name instanceof String) {
            return StringUtils.isBlank((String) name);
        }
        return false;
    }

    public static boolean isNotNull(Object name) {
        return !isNull(name);
    }

    public static String camel(String name) {
        return camel(name, Strings.UNDERLINE);
    }

    public static String camel(String name, String separator) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        name = name.toLowerCase();
        Pattern linePattern = Pattern.compile(separator + "(\\w)");
        Matcher matcher = linePattern.matcher(name);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
