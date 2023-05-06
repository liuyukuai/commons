/*
 *
 */
package com.cutefool.commons.core.util;

import com.cutefool.commons.core.Constants;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * 时间工具类
 *
 * @author 271007729@qq.com
 * @date 9/2/21 4:32 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Times {

    public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

    public static long getTime() {
        return getTime(DEFAULT_ZONE_OFFSET);
    }

    public static long getTime(ZoneOffset offset) {
        return getTime(LocalDateTime.now(), offset);
    }

    public static long getTime(LocalDateTime time) {
        return getTime(time, DEFAULT_ZONE_OFFSET);
    }

    public static long getTime(LocalDateTime time, ZoneOffset offset) {
        return time.toInstant(offset).toEpochMilli();
    }

    public static LocalDateTime getTime(Long time) {
        return getTime(time, DEFAULT_ZONE_OFFSET);
    }

    public static LocalDateTime getTime(Instant instant) {
        return getTime(instant.toEpochMilli());
    }

    public static LocalDateTime getTime(Long time, ZoneOffset offset) {
        Instant instant = Instant.ofEpochMilli(time);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static Instant from(LocalDateTime time) {
        long timeInSeconds = time.toEpochSecond(ZoneOffset.UTC);
        return Instant.ofEpochSecond(timeInSeconds);
    }

    public static String format(Long times) {
        return format(times / Constants.SECONDS_OF_DAY, (times % Constants.SECONDS_OF_DAY) / Constants.SECONDS_OF_HOUR, (times % Constants.SECONDS_OF_HOUR) / Constants.SECONDS_OF_MINUTES, times % Constants.SECONDS_OF_MINUTES);
    }

    public static String format(long day, long hours, long minutes, long seconds) {
        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(String.format("%dd", day));
        }
        if (hours > 0) {
            sb.append(String.format("%dh", hours));
        }
        if (minutes > 0) {
            sb.append(String.format("%dm", minutes));
        }
        sb.append(String.format("%ds", seconds));
        return sb.toString();
    }

    public static String formatZh(Long times) {
        return formatZh(times / Constants.SECONDS_OF_DAY, (times % Constants.SECONDS_OF_DAY) / Constants.SECONDS_OF_HOUR, (times % Constants.SECONDS_OF_HOUR) / Constants.SECONDS_OF_MINUTES, times % Constants.SECONDS_OF_MINUTES);
    }

    public static String formatZh(long day, long hours, long minutes, long seconds) {
        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(String.format("%d天", day));
        }
        if (hours > 0) {
            sb.append(String.format("%d小时", hours));
        }
        if (minutes > 0) {
            sb.append(String.format("%d分钟", minutes));
        }
        sb.append(String.format("%d秒", seconds));
        return sb.toString();
    }

    public static String format(LocalDateTime time) {
        if (Objects.isNull(time)) {
            return Strings.EMPTY;
        }

        return DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN).format(time);
    }

    public static String format(LocalDate time) {
        if (Objects.isNull(time)) {
            return Strings.EMPTY;
        }
        return DateTimeFormatter.ofPattern(Constants.DATE_PATTERN).format(time);
    }

    public static String format(TemporalAccessor localDate, String pattern) {
        if (Objects.isNull(localDate)) {
            return Strings.EMPTY;
        }
        return DateTimeFormatter.ofPattern(pattern).format(localDate);
    }

    public static String format(Timestamp timestamp) {
        return format(timestamp, Constants.DATE_TIME_PATTERN);
    }

    public static String format(Timestamp timestamp, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return Optional.ofNullable(timestamp).map(e -> simpleDateFormat.format(timestamp)).orElse(Strings.EMPTY);
    }

    public static String format(Date date) {
        return format(date, Constants.DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return Optional.ofNullable(date).map(e -> simpleDateFormat.format(date)).orElse(Strings.EMPTY);
    }

    public static LocalDateTime parse(String time) {
        return parse(time, Constants.DATE_TIME_PATTERN);
    }

    public static LocalDateTime parse(String time, String pattern) {
        return LocalDateTime.from(DateTimeFormatter.ofPattern(pattern).parse(time));
    }

    public static LocalDate parseDate(String time) {
        return parseDate(time, Constants.DATE_PATTERN);
    }

    public static LocalDate parseDate(String time, String pattern) {
        return LocalDate.from(DateTimeFormatter.ofPattern(pattern).parse(time));
    }

    public static boolean isCurrentYear(LocalDateTime time) {
        return Objects.equals(LocalDateTime.now().getYear(), time.getYear());
    }

    public static boolean isCurrentYear(LocalDate time) {
        return Objects.equals(LocalDateTime.now().getYear(), time.getYear());
    }

    public static boolean isCurrentMonth(LocalDateTime time) {
        return isCurrentYear(time) && Objects.equals(LocalDateTime.now().getMonthValue(), time.getMonthValue());
    }

    public static boolean isCurrentMonth(LocalDate time) {
        return isCurrentYear(time) && Objects.equals(LocalDate.now().getMonthValue(), time.getMonthValue());
    }

    public static boolean isCurrentDay(LocalDateTime time) {
        return isCurrentMonth(time) && Objects.equals(LocalDateTime.now().getDayOfMonth(), time.getDayOfMonth());
    }

    public static boolean isCurrentDay(LocalDate time) {
        return isCurrentMonth(time) && Objects.equals(LocalDate.now().getDayOfMonth(), time.getDayOfMonth());
    }

    public static LocalDateTime firstTimeOfMinutes() {
        return firstTimeOfMinutes(LocalDateTime.now());
    }

    public static LocalDateTime lastTimeOfMinutes() {
        return lastTimeOfMinutes(LocalDateTime.now());
    }

    public static LocalDateTime firstTimeOfMinutes(LocalDateTime time) {
        return LocalDateTime.of(time.toLocalDate(), LocalTime.MIN.withHour(time.getHour()).withMinute(time.getMinute()));
    }

    public static LocalDateTime lastTimeOfMinutes(LocalDateTime time) {
        return LocalDateTime.of(time.toLocalDate(), LocalTime.MAX.withHour(time.getHour()).withMinute(time.getMinute()));
    }

    public static LocalDateTime firstTimeOfHour() {
        return firstTimeOfHour(LocalDateTime.now());
    }

    public static LocalDateTime lastTimeOfHour() {
        return lastTimeOfHour(LocalDateTime.now());
    }

    public static LocalDateTime firstTimeOfHour(LocalDateTime time) {
        return LocalDateTime.of(time.toLocalDate(), LocalTime.MIN.withHour(time.getHour()));
    }

    public static LocalDateTime lastTimeOfHour(LocalDateTime time) {
        return LocalDateTime.of(time.toLocalDate(), LocalTime.MAX.withHour(time.getHour()));
    }

    public static LocalDateTime firstTimeOfDay() {
        return firstTimeOfDay(LocalDate.now());
    }

    public static LocalDateTime lastTimeOfDay() {
        return lastTimeOfDay(LocalDate.now());
    }

    public static LocalDateTime firstTimeOfDay(LocalDate time) {
        return LocalDateTime.of(time, LocalTime.MIN);
    }

    public static LocalDateTime lastTimeOfDay(LocalDate time) {
        return LocalDateTime.of(time, LocalTime.MAX);
    }

    public static LocalDateTime firstTimeOfWeek() {
        return firstTimeOfWeek(LocalDate.now());
    }

    public static LocalDateTime lastTimeOfWeek() {
        return lastTimeOfWeek(LocalDate.now());
    }

    public static LocalDateTime firstTimeOfWeek(LocalDate time) {
        return LocalDateTime.of(time, LocalTime.MIN).with(DayOfWeek.MONDAY);
    }

    public static LocalDateTime lastTimeOfWeek(LocalDate time) {
        return LocalDateTime.of(time, LocalTime.MAX).with(DayOfWeek.SUNDAY);
    }

    public static LocalDateTime firstTimeOfMonth() {
        return firstTimeOfMonth(LocalDate.now());
    }

    public static LocalDateTime lastTimeOfMonth() {
        return lastTimeOfMonth(LocalDate.now());
    }

    public static LocalDateTime firstTimeOfMonth(LocalDate time) {
        return LocalDateTime.of(time, LocalTime.MIN).with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDateTime lastTimeOfMonth(LocalDate time) {
        return LocalDateTime.of(time, LocalTime.MAX).with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDateTime firstTimeOfQuarter() {
        return firstTimeOfQuarter(LocalDate.now());
    }

    public static LocalDateTime lastTimeOfQuarter() {
        return lastTimeOfQuarter(LocalDate.now());
    }

    public static LocalDateTime firstTimeOfQuarter(LocalDate time) {
        Month firstMonthOfQuarter = time.getMonth().firstMonthOfQuarter();
        LocalDate date = LocalDate.of(time.getYear(), firstMonthOfQuarter, 1);
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    public static LocalDateTime lastTimeOfQuarter(LocalDate time) {
        Month firstMonthOfQuarter = time.getMonth().firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        LocalDate date = LocalDate.of(time.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(time.isLeapYear()));
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    public static LocalDateTime firstTimeOfHalfYear() {
        return firstTimeOfHalfYear(LocalDate.now());
    }

    public static LocalDateTime lastTimeOfHalfYear() {
        return lastTimeOfHalfYear(LocalDate.now());
    }

    public static LocalDateTime firstTimeOfHalfYear(LocalDate time) {
        int monthValue = time.getMonthValue();
        if (monthValue <= Constants.MONTH_OF_HALF_YEAR) {
            return firstTimeOfYear(time);
        }
        return firstTimeOfYear(time).plusMonths(Constants.MONTH_OF_HALF_YEAR);
    }

    public static LocalDateTime lastTimeOfHalfYear(LocalDate time) {
        int monthValue = time.getMonthValue();
        if (monthValue <= Constants.MONTH_OF_HALF_YEAR) {
            return lastTimeOfYear(time).plusMonths(-Constants.MONTH_OF_HALF_YEAR);
        }
        return lastTimeOfYear(time);
    }

    public static LocalDateTime firstTimeOfYear() {
        return firstTimeOfYear(LocalDate.now());
    }

    public static LocalDateTime lastTimeOfYear() {
        return lastTimeOfYear(LocalDate.now());
    }

    public static LocalDateTime firstTimeOfYear(LocalDate time) {
        return LocalDateTime.of(time, LocalTime.MIN).with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDateTime lastTimeOfYear(LocalDate time) {
        return LocalDateTime.of(time, LocalTime.MAX).with(TemporalAdjusters.lastDayOfYear());
    }
}
