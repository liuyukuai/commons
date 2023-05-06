package com.cutefool.commons.core.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * 数字工具类
 *
 * @author 271007729@qq.com
 * @date 2019-08-20 02:54
 * ¬¬
 */
@SuppressWarnings("unused")
public class Numbers {

    private static final NumberFormat NF = NumberFormat.getPercentInstance();

    private static final String DEFAULT_FORMAT = "#.00";

    /**
     * 比较两个数字是否相等
     *
     * @param a 第一个数
     * @param b 第二个数
     * @return true：相等，false：不相等
     */
    @SuppressWarnings("ALL")
    public static boolean equals(Number a, Number b) {
        boolean equals = Objects.equals(a, b);
        // 如果相等
        if (equals) {
            return true;
        }

        // 如果其中一个值为null，两个数一定不相等
        if (Objects.isNull(a) || Objects.isNull(b)) {
            return false;
        }

        // 如果不相等一定有一个不为空
        try {

            // 如果为byte
            if (a instanceof Byte || b instanceof Byte) {
                return Objects.equals(a.byteValue(), b.byteValue());
            }

            // 如果为short
            if (a instanceof Short || b instanceof Short) {
                return Objects.equals(a.shortValue(), b.shortValue());
            }

            // 如果为int
            if (a instanceof Integer || b instanceof Integer) {
                return Objects.equals(a.intValue(), b.intValue());
            }

            // 如果为long
            if (a instanceof Long || b instanceof Long) {
                return Objects.equals(a.longValue(), b.longValue());
            }

            // 如果为float
            if (a instanceof Float || b instanceof Float) {
                return Objects.equals(a.floatValue(), b.floatValue());
            }

            // 如果为Double
            if (a instanceof Double || b instanceof Double) {
                return Objects.equals(a.doubleValue(), b.doubleValue());
            }

            return equals;
        } catch (Exception e) {
            e.printStackTrace();
            return equals;
        }
    }

    /**
     * 百分比
     *
     * @param num 求百分比的数字
     * @return 百分比
     */
    public static String percent(double num) {
        return percent(num, 2);
    }

    /**
     * 百分比
     *
     * @param num 求百分比的数字
     * @return 百分比
     */
    public static String percent(double num, int len) {
        NF.setMinimumFractionDigits(len);
        return NF.format(num);
    }

    public static Double format(Number source) {
        return format(source, 0.0);
    }

    public static Double format(Number source, String pattern) {
        return format(source, pattern, 0.0);
    }

    public static Double format(Number source, Double defaultValue) {
        return format(source, DEFAULT_FORMAT, defaultValue);
    }

    public static Double format(Number source, String pattern, Double defaultValue) {
        if (Objects.nonNull(source)) {
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            String format = decimalFormat.format(source);
            return Double.parseDouble(format);
        }
        return defaultValue;
    }

    public static Double format(String source) {
        return format(source, DEFAULT_FORMAT, 0.0);
    }

    public static Double format(String source, String pattern) {
        return format(source, pattern, 0.0);
    }

    public static Double format(String source, Double defaultValue) {
        if (StringUtils.isBlank(source)) {
            return defaultValue;
        }
        return format(Double.parseDouble(source), defaultValue);
    }

    public static Double format(String source, String pattern, Double defaultValue) {
        if (StringUtils.isBlank(source)) {
            return defaultValue;
        }
        return format(Double.parseDouble(source), pattern, defaultValue);
    }

    /**
     * 保留两位小数点
     *
     * @param source the source
     * @return double
     */
    public static Double reserveTwoPoint(String source) {
        if (StringUtils.isNotBlank(source)) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String format = decimalFormat.format(Double.parseDouble(source));
            return Double.parseDouble(format);
        }
        return 0.0;
    }

    /**
     * 保留一位小数点
     *
     * @param source the source
     * @return double
     */
    public static Double reserveOnePoint(String source) {
        if (StringUtils.isNotBlank(source)) {
            DecimalFormat decimalFormat = new DecimalFormat("#.0");
            String format = decimalFormat.format(Double.parseDouble(source));
            return Double.parseDouble(format);
        }
        return 0.0;
    }

    public static Long sum(Long... nums) {
        return Stream.of(nums).filter(Objects::nonNull).mapToLong(e -> e).sum();
    }

    public static Double sum(Double... nums) {
        return Stream.of(nums).filter(Objects::nonNull).mapToDouble(e -> e).sum();
    }

    public static Integer sum(Integer... nums) {
        return Stream.of(nums).filter(Objects::nonNull).mapToInt(e -> e).sum();
    }

    public static BigDecimal sum(BigDecimal... nums) {
        return Stream.of(nums).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Double radio(Long a, Long b) {
        if (Objects.isNull(a) || Objects.isNull(b) || Objects.equals(b, 0L)) {
            return 0D;
        }
        return a * 1.0 / b;
    }

    public static Double radio(Integer a, Integer b) {
        if (Objects.isNull(a) || Objects.isNull(b) || Objects.equals(b, 0)) {
            return 0D;
        }
        return a * 1.0 / b;
    }

    public static Double radio(Double a, Double b) {
        if (Objects.isNull(a) || Objects.isNull(b) || Objects.equals(b, (double) 0)) {
            return 0D;
        }
        return a * 1.0 / b;
    }

    public static Double radio(BigDecimal a, BigDecimal b) {
        if (Objects.isNull(a) || Objects.isNull(b) || Objects.equals(b, BigDecimal.ZERO)) {
            return 0D;
        }
        return a.doubleValue() / b.doubleValue();
    }

    public static Long max(Long... nums) {
        OptionalLong max = Stream.of(nums).filter(Objects::nonNull).mapToLong(e -> e).max();
        if (max.isPresent()) {
            return max.getAsLong();
        }
        return null;
    }

    public static Double max(Double... nums) {
        OptionalDouble max = Stream.of(nums).filter(Objects::nonNull).mapToDouble(e -> e).max();
        if (max.isPresent()) {
            return max.getAsDouble();
        }
        return null;
    }

    public static BigDecimal max(BigDecimal... nums) {
        Optional<BigDecimal> max = Stream.of(nums).filter(Objects::nonNull).max(BigDecimal::compareTo);
        return max.orElse(null);
    }

    public static Integer max(Integer... nums) {
        OptionalInt max = Stream.of(nums).filter(Objects::nonNull).mapToInt(e -> e).max();
        if (max.isPresent()) {
            return max.getAsInt();
        }
        return null;
    }

    public static Long min(Long... nums) {
        OptionalLong min = Stream.of(nums).filter(Objects::nonNull).mapToLong(e -> e).min();
        if (min.isPresent()) {
            return min.getAsLong();
        }
        return null;
    }

    public static Double min(Double... nums) {
        OptionalDouble min = Stream.of(nums).filter(Objects::nonNull).mapToDouble(e -> e).min();
        if (min.isPresent()) {
            return min.getAsDouble();
        }
        return null;
    }

    public static BigDecimal min(BigDecimal... nums) {
        Optional<BigDecimal> min = Stream.of(nums).filter(Objects::nonNull).min(BigDecimal::compareTo);
        return min.orElse(null);
    }

    public static Integer min(Integer... nums) {
        OptionalInt min = Stream.of(nums).filter(Objects::nonNull).mapToInt(e -> e).min();
        if (min.isPresent()) {
            return min.getAsInt();
        }
        return null;
    }

    public static int compare(Long a, Long b) {
        if (Objects.equals(a, b) || Objects.isNull(a) || Objects.isNull(b)) {
            return 0;
        }
        return a.compareTo(b);
    }

    public static int compare(Integer a, Integer b) {
        if (Objects.equals(a, b) || Objects.isNull(a) || Objects.isNull(b)) {
            return 0;
        }
        return a.compareTo(b);
    }

    public static int compare(BigDecimal a, BigDecimal b) {
        if (Objects.equals(a, b) || Objects.isNull(a) || Objects.isNull(b)) {
            return 0;
        }
        return a.compareTo(b);
    }

    public static int compare(Double a, Double b) {
        if (Objects.equals(a, b) || Objects.isNull(a) || Objects.isNull(b)) {
            return 0;
        }
        return a.compareTo(b);
    }
}
