package com.cutefool.commons.core.util;

import com.cutefool.commons.core.Constants;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author 271007729@qq.com
 * @see Times
 * @see Durations
 */
@SuppressWarnings("all")
@Deprecated
public final class LocalDateTimeUtil {
    /**
     * default
     */
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";


    private LocalDateTimeUtil() {
    }

    /**
     * 本月第一天
     *
     * @return localDate
     */
    public static LocalDateTime firstDay() {
        return firstDay(LocalDateTime.now());
    }

    /**
     * 指定时间的当月第一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDateTime firstDay(LocalDateTime localDate) {
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), 1, 0, 0, 0);
    }


    /**
     * 指定时间的当月第一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDateTime firstDay(String localDate) {
        LocalDateTime parse = parse(localDate, DEFAULT_PATTERN);
        return LocalDateTime.of(parse.getYear(), parse.getMonth(), 1, 0, 0, 0);
    }

    /**
     * 本月最后一天
     *
     * @return localDate
     */
    public static LocalDateTime lastDay() {
        return lastDay(LocalDateTime.now());
    }

    /**
     * 本月最后一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDateTime lastDay(LocalDateTime localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }


    /**
     * 本月最后一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDateTime lastDay(String localDate) {
        LocalDateTime parse = parse(localDate, DEFAULT_PATTERN);
        return parse.with(TemporalAdjusters.lastDayOfMonth());
    }


    /**
     * addDays
     *
     * @param localDate 时间
     * @param days      天数
     * @return localDate
     */
    public static LocalDateTime addDays(String localDate, long days) {
        LocalDateTime parse = parse(localDate, DEFAULT_PATTERN);
        return parse.plusDays(days);
    }

    /**
     * 格式化时间
     *
     * @param localDate 时间
     * @return 时间字符串
     */
    public static String format(TemporalAccessor localDate) {
        return format(localDate, DEFAULT_PATTERN);
    }


    /**
     * 格式化时间
     *
     * @param localDate 时间
     * @param pattern   时间格式
     * @return 时间字符串
     */
    public static String format(TemporalAccessor localDate, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDate);
    }


    /**
     * 格式化时间
     *
     * @param localDate 时间
     * @return 时间字符串
     */
    public static LocalDateTime parse(String localDate) {
        return parse(localDate, DEFAULT_PATTERN);
    }

    /**
     * 格式化时间
     *
     * @param localDate 时间
     * @param pattern   时间格式
     * @return 时间字符串
     */
    public static LocalDateTime parse(String localDate, String pattern) {
        return LocalDateTime.from(DateTimeFormatter.ofPattern(pattern).parse(localDate));
    }


    public static List<String> days(LocalDateTime before, LocalDateTime after) {
        if (Objects.isNull(before) || Objects.isNull(after)) {
            throw new IllegalArgumentException("before or after time can not null.");
        }

        long days = ChronoUnit.DAYS.between(before, after);

        // 开始时间
        LocalDateTime temp = LocalDateTime.of(before.getYear(), before.getMonth(), before.getDayOfMonth(), 0, 0, 0);

        return LongStream.range(0, days + 1)
                .mapToObj(e -> {
                    LocalDateTime localDateTime = temp.plusDays(e);
                    return format(localDateTime, Constants.DATE_PATTERN);
                })
                .collect(Collectors.toList());
    }


    public static List<String> hours(LocalDateTime before, LocalDateTime after) {
        if (Objects.isNull(before) || Objects.isNull(after)) {
            throw new IllegalArgumentException("before or after time can not null.");
        }

        long hours = ChronoUnit.HOURS.between(before, after);

        return LongStream.range(0, hours + 1)
                .mapToObj(e -> {
                    LocalDateTime localDateTime = before.plusHours(e);
                    return format(localDateTime, "yyyy-MM-dd HH:00");
                })
                .collect(Collectors.toList());
    }


    public static List<String> minutes(LocalDateTime before, LocalDateTime after) {
        if (Objects.isNull(before) || Objects.isNull(after)) {
            throw new IllegalArgumentException("before or after time can not null.");
        }

        long hours = ChronoUnit.MINUTES.between(before, after) / 10;

        return LongStream.range(0, hours + 1)
                .mapToObj(e -> {
                    LocalDateTime localDateTime = before.plusMinutes(10 * e);
                    // 分钟取整
                    int minute = localDateTime.getMinute() / 10;
                    localDateTime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), localDateTime.getHour(), minute * 10, localDateTime.getSecond());
                    return format(localDateTime, "yyyy-MM-dd HH:mm:00");
                })
                .collect(Collectors.toList());
    }


    public static List<String> months(LocalDateTime before, LocalDateTime after) {
        if (Objects.isNull(before) || Objects.isNull(after)) {
            throw new IllegalArgumentException("before or after time can not null.");
        }

        long months = ChronoUnit.MONTHS.between(before, after);

        return LongStream.range(0, months + 1)
                .mapToObj(e -> {
                    LocalDateTime localDateTime = before.plusMonths(e);
                    return format(localDateTime, "yyyy-MM");
                })
                .collect(Collectors.toList());

    }

    public static List<WeekInterval> weeks(LocalDateTime before, LocalDateTime after) {
        long size = ChronoUnit.WEEKS.between(before, after) + 1;

        List<WeekInterval> weekIntervals = new ArrayList<>();
        for (long i = 0; i < size; i++) {
            // 判断开始时间在周几
            int value = before.getDayOfWeek().getValue();
            LocalDateTime sunday = before.plusDays(7 - value);
            if (sunday.isAfter(after)) {
                weekIntervals.add(new WeekInterval(before, after));
            } else {
                weekIntervals.add(new WeekInterval(before, sunday));
            }
            before = sunday.plusDays(1);
        }
        return weekIntervals;
    }


    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeekInterval {
        private LocalDateTime monday;
        private LocalDateTime sunday;

        public String format(Function<WeekInterval, String> function) {
            return function.apply(this);
        }

    }

}
