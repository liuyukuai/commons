/*
 *  
 */
package com.cutefool.commons.core.util;

import com.cutefool.commons.core.Constants;
import com.cutefool.commons.core.DurationLibs;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 时间工具类
 *
 * @author 271007729@qq.com
 * @date 9/2/21 4:32 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Durations {


    public static final Function<LocalDateTime, String> WEEK_PATTERN = (time) -> {
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        return time.get(weekFields.weekBasedYear()) + "第" + time.get(weekFields.weekOfWeekBasedYear()) + "周";
    };

    public static final Function<LocalDateTime, String> QUARTERS_PATTERN = (time) -> "Q" + (time.getMonthValue() / 3 + 1) + "_" + time.getYear();

    public static final Function<LocalDateTime, String> HALF_YEAR_PATTERN = (time) -> "H" + (time.getMonthValue() / 6 + 1) + "_" + time.getYear();


    public static List<DurationLibs<LocalDateTime, String>> tenSeconds() {
        return tenSeconds(Times.firstTimeOfHour(), Times.lastTimeOfHour());
    }

    public static List<DurationLibs<LocalDateTime, String>> tenSeconds(LocalDateTime before, LocalDateTime after) {
        validate(before, after);
        long seconds = ChronoUnit.SECONDS.between(before, after) / 10;
        return tenSeconds(before, seconds);
    }

    public static List<DurationLibs<LocalDateTime, String>> tenSeconds(LocalDateTime before, long seconds) {
        AtomicReference<LocalDateTime> startTime = new AtomicReference<>(tenSeconds(before));
        // 开始时间
        return LongStream.range(0, seconds + 1)
                .mapToObj(e -> {
                    LocalDateTime localDateTime = startTime.get();
                    LocalDateTime endTime = localDateTime.plusSeconds(10);
                    DurationLibs<LocalDateTime, String> libs = new DurationLibs<>(localDateTime, endTime);
                    libs.setName(Times.format(libs.getBefore(), Constants.DATE_TIME_PATTERN));
                    startTime.set(endTime);
                    return libs;
                })
                .collect(Collectors.toList());
    }


    public static List<DurationLibs<LocalDateTime, String>> minutes() {
        return minutes(1);
    }

    public static List<DurationLibs<LocalDateTime, String>> minutes(int interval) {
        return minutes(Times.firstTimeOfHour(), Times.lastTimeOfHour(), interval);
    }

    public static List<DurationLibs<LocalDateTime, String>> minutes(LocalDateTime before, LocalDateTime after) {
        return minutes(before, after, 1);
    }

    public static List<DurationLibs<LocalDateTime, String>> minutes(LocalDateTime before, LocalDateTime after, int interval) {
        long minutes = ChronoUnit.MINUTES.between(before, after) / interval;
        return minutes(before, minutes, interval);
    }

    public static List<DurationLibs<LocalDateTime, String>> minutes(LocalDateTime before, long minutes) {
        return minutes(before, minutes, 1);
    }

    public static List<DurationLibs<LocalDateTime, String>> minutes(LocalDateTime before, long minutes, int interval) {

        // 开始时间
        return LongStream.range(0, minutes + 1)
                .mapToObj(e -> {
                    LocalDateTime localDateTime = before.plusMinutes(e * interval);
                    DurationLibs<LocalDateTime, String> libs = new DurationLibs<>(Times.firstTimeOfMinutes(localDateTime), Times.lastTimeOfMinutes(localDateTime));
                    libs.setName(Times.format(libs.getAfter(), Constants.DATE_MINUTES_PATTERN));
                    return libs;
                })
                .collect(Collectors.toList());
    }

    public static List<DurationLibs<LocalDateTime, String>> hours() {
        return hours(Times.firstTimeOfDay(), Times.lastTimeOfDay());
    }

    public static List<DurationLibs<LocalDateTime, String>> hours(LocalDateTime before, LocalDateTime after) {
        validate(before, after);
        long hours = ChronoUnit.HOURS.between(before, after);
        return hours(before, hours, Constants.DATE_HOUR_PATTERN);
    }

    public static List<DurationLibs<LocalDateTime, String>> hours(LocalDateTime before, LocalDateTime after, String pattern) {
        validate(before, after);
        long hours = ChronoUnit.HOURS.between(before, after);
        return hours(before, hours, pattern);
    }

    public static List<DurationLibs<LocalDateTime, String>> hours(LocalDateTime before, long hours) {
        return hours(before, hours, Constants.DATE_HOUR_PATTERN);
    }

    public static List<DurationLibs<LocalDateTime, String>> hours(LocalDateTime before, long hours, String pattern) {

        // 开始时间
        return LongStream.range(0, hours + 1)
                .mapToObj(e -> {
                    LocalDateTime localDateTime = before.plusHours(e);
                    DurationLibs<LocalDateTime, String> libs = new DurationLibs<>(Times.firstTimeOfHour(localDateTime), Times.lastTimeOfHour(localDateTime));
                    libs.setName(Times.format(libs.getAfter(), pattern));
                    return libs;
                })
                .collect(Collectors.toList());
    }

    public static List<DurationLibs<LocalDateTime, LocalDate>> days() {
        return days(Times.firstTimeOfYear(), Times.lastTimeOfYear());
    }

    public static List<DurationLibs<LocalDateTime, String>> days(String pattern) {
        return days(Times.firstTimeOfYear(), Times.lastTimeOfYear(), pattern);
    }

    public static List<DurationLibs<LocalDateTime, LocalDate>> days(LocalDateTime before, LocalDateTime after) {
        validate(before, after);
        // 天数
        long days = ChronoUnit.DAYS.between(before, after);
        // 开始时间
        return days(before, days);
    }

    public static List<DurationLibs<LocalDateTime, LocalDate>> days(LocalDate before, LocalDate after) {
        validate(before, after);
        long days = ChronoUnit.DAYS.between(before, after);
        return days(before, days);
    }

    public static List<DurationLibs<LocalDateTime, String>> days(LocalDateTime before, LocalDateTime after, String pattern) {
        validate(before, after);
        long days = ChronoUnit.DAYS.between(before, after);
        return days(before, days, pattern);
    }

    public static List<DurationLibs<LocalDateTime, String>> days(LocalDate before, LocalDate after, String pattern) {
        validate(before, after);
        long days = ChronoUnit.DAYS.between(before, after);
        return days(before, days, pattern);

    }

    public static List<DurationLibs<LocalDateTime, LocalDate>> days(LocalDateTime before, long days) {
        return days(before.toLocalDate(), days);

    }

    public static List<DurationLibs<LocalDateTime, LocalDate>> days(LocalDate before, long days) {
        // 开始时间
        return LongStream.range(0, days + 1)
                .mapToObj(e -> {
                    LocalDate localDate = before.plusDays(e);
                    DurationLibs<LocalDateTime, LocalDate> libs = new DurationLibs<>();
                    libs.setBefore(Times.firstTimeOfDay(localDate));
                    libs.setAfter(Times.lastTimeOfDay(localDate));
                    libs.setName(localDate);
                    return libs;
                })
                .collect(Collectors.toList());
    }

    public static List<DurationLibs<LocalDateTime, String>> days(LocalDateTime before, long days, String pattern) {
        return days(before.toLocalDate(), days, pattern);

    }

    public static List<DurationLibs<LocalDateTime, String>> days(LocalDate before, long days, String pattern) {
        // 开始时间
        return LongStream.range(0, days + 1)
                .mapToObj(e -> {
                    LocalDate localDate = before.plusDays(e);
                    DurationLibs<LocalDateTime, String> libs = new DurationLibs<>(Times.firstTimeOfDay(localDate), Times.lastTimeOfDay(localDate));
                    libs.setName(DateTimeFormatter.ofPattern(pattern).format(localDate));
                    return libs;
                })
                .collect(Collectors.toList());
    }

    public static List<DurationLibs<LocalDateTime, String>> weeks() {
        return weeks(Times.firstTimeOfYear(), Times.lastTimeOfYear());
    }

    public static List<DurationLibs<LocalDateTime, String>> weeks(LocalDateTime before, LocalDateTime after) {
        validate(before, after);
        long weeks = ChronoUnit.WEEKS.between(before, after) + 1;
        return weeks(before, weeks);
    }

    public static List<DurationLibs<LocalDateTime, String>> weeks(LocalDate before, LocalDate after) {
        validate(before, after);
        long weeks = ChronoUnit.WEEKS.between(before, after);
        return weeks(before, weeks);
    }

    public static List<DurationLibs<LocalDateTime, String>> weeks(LocalDateTime before, long weeks) {
        return weeks(before.toLocalDate(), weeks);

    }

    public static List<DurationLibs<LocalDateTime, String>> weeks(LocalDate before, long weeks) {

        AtomicReference<LocalDateTime> monday = new AtomicReference<>(Times.firstTimeOfWeek(before));

        // 开始时间
        return LongStream.range(0, weeks)
                .mapToObj(e -> {
                    DurationLibs<LocalDateTime, String> libs = new DurationLibs<>();
                    LocalDateTime localDateTime = monday.get();
                    LocalDateTime sunday = Times.lastTimeOfWeek(localDateTime.toLocalDate());
                    libs.setBefore(localDateTime);
                    libs.setAfter(sunday);
                    libs.setName(WEEK_PATTERN.apply(sunday));
                    monday.set(Times.firstTimeOfWeek(sunday.plusDays(1).toLocalDate()));
                    return libs;
                })
                .collect(Collectors.toList());
    }


    public static List<DurationLibs<LocalDateTime, String>> months() {
        return months(Constants.MONTH_PATTERN);
    }

    public static List<DurationLibs<LocalDateTime, String>> months(String pattern) {
        return months(Times.firstTimeOfYear(), Times.lastTimeOfYear(), pattern);
    }

    public static List<DurationLibs<LocalDateTime, String>> months(LocalDateTime before, LocalDateTime after) {
        return months(before, after, Constants.MONTH_PATTERN);
    }

    public static List<DurationLibs<LocalDateTime, String>> months(LocalDateTime before, LocalDateTime after, String pattern) {
        validate(before, after);
        return months(before.toLocalDate(), after.toLocalDate(), pattern);
    }

    public static List<DurationLibs<LocalDateTime, String>> months(LocalDate before, LocalDate after) {
        return months(before, after, Constants.MONTH_PATTERN);
    }

    public static List<DurationLibs<LocalDateTime, String>> months(LocalDate before, LocalDate after, String pattern) {
        validate(before, after);
        // 月数
        long months = ChronoUnit.MONTHS.between(before, after) + 1;
        // 开始时间
        return months(before, months, pattern);
    }

    public static List<DurationLibs<LocalDateTime, String>> months(LocalDateTime before, long months) {
        return months(before, months, Constants.MONTH_PATTERN);

    }

    public static List<DurationLibs<LocalDateTime, String>> months(LocalDateTime before, long months, String pattern) {
        return months(before.toLocalDate(), months);

    }

    public static List<DurationLibs<LocalDateTime, String>> months(LocalDate before, long months) {
        return months(before, months, Constants.MONTH_PATTERN);
    }

    public static List<DurationLibs<LocalDateTime, String>> months(LocalDate before, long months, String pattern) {
        // 开始时间
        return LongStream.range(0, months)
                .mapToObj(e -> {
                    LocalDateTime firstTime = Times.firstTimeOfMonth(before.plusMonths(e));
                    DurationLibs<LocalDateTime, String> libs = new DurationLibs<>();
                    LocalDateTime localTime = Times.lastTimeOfMonth(firstTime.toLocalDate());
                    libs.setBefore(firstTime);
                    libs.setAfter(localTime);
                    libs.setName(Times.format(firstTime, pattern));
                    return libs;
                })
                .collect(Collectors.toList());
    }


    public static List<DurationLibs<LocalDateTime, String>> quarters() {
        return quarters(Times.firstTimeOfYear(), Times.lastTimeOfYear());
    }

    public static List<DurationLibs<LocalDateTime, String>> quarters(LocalDateTime before, LocalDateTime after) {
        validate(before, after);
        return quarters(before.toLocalDate(), after.toLocalDate());
    }

    public static List<DurationLibs<LocalDateTime, String>> quarters(LocalDate before, LocalDate after) {
        validate(before, after);
        double quarters = Math.ceil(ChronoUnit.MONTHS.between(before, after) * 1.0 / 3);
        return quarters(before, (long) quarters);
    }

    public static List<DurationLibs<LocalDateTime, String>> quarters(LocalDateTime before, long quarters) {
        return quarters(before.toLocalDate(), quarters);

    }

    public static List<DurationLibs<LocalDateTime, String>> quarters(LocalDate before, long quarters) {

        // 开始时间
        return LongStream.range(0, quarters)
                .mapToObj(e -> {
                    LocalDateTime firstTime = Times.firstTimeOfQuarter(before.plusMonths(e * 3));
                    DurationLibs<LocalDateTime, String> libs = new DurationLibs<>();
                    LocalDateTime localTime = Times.lastTimeOfQuarter(firstTime.toLocalDate());
                    libs.setBefore(firstTime);
                    libs.setAfter(localTime);
                    libs.setName(QUARTERS_PATTERN.apply(firstTime));
                    return libs;
                })
                .collect(Collectors.toList());
    }

    public static List<DurationLibs<LocalDateTime, String>> halfYears() {
        return halfYears(Times.firstTimeOfYear(), Times.lastTimeOfYear());
    }

    public static List<DurationLibs<LocalDateTime, String>> halfYears(LocalDateTime before, LocalDateTime after) {
        validate(before, after);
        return halfYears(before.toLocalDate(), after.toLocalDate());
    }

    public static List<DurationLibs<LocalDateTime, String>> halfYears(LocalDate before, LocalDate after) {
        validate(before, after);
        double halfYears = Math.ceil(ChronoUnit.MONTHS.between(before, after) * 1.0 / 6);
        return halfYears(before, (long) halfYears);
    }

    public static List<DurationLibs<LocalDateTime, String>> halfYears(LocalDateTime before, long halfYears) {
        return halfYears(before.toLocalDate(), halfYears);

    }

    public static List<DurationLibs<LocalDateTime, String>> halfYears(LocalDate before, long halfYears) {

        // 开始时间
        return LongStream.range(0, halfYears)
                .mapToObj(e -> {
                    LocalDateTime firstTime = Times.firstTimeOfHalfYear(before.plusMonths(e * 6));
                    DurationLibs<LocalDateTime, String> libs = new DurationLibs<>();
                    LocalDateTime localTime = Times.lastTimeOfHalfYear(firstTime.toLocalDate());
                    libs.setBefore(firstTime);
                    libs.setAfter(localTime);
                    libs.setName(HALF_YEAR_PATTERN.apply(firstTime));
                    return libs;
                })
                .collect(Collectors.toList());
    }

    public static List<DurationLibs<LocalDateTime, String>> years() {
        return years(Times.firstTimeOfYear(), Times.lastTimeOfYear());
    }

    public static List<DurationLibs<LocalDateTime, String>> years(LocalDateTime before, LocalDateTime after) {
        validate(before, after);
        return years(before.toLocalDate(), after.toLocalDate());
    }

    public static List<DurationLibs<LocalDateTime, String>> years(LocalDate before, LocalDate after) {
        validate(before, after);
        long years = ChronoUnit.YEARS.between(before, after);
        return years(before, years);
    }

    public static List<DurationLibs<LocalDateTime, String>> years(LocalDateTime before, long years) {
        return years(before.toLocalDate(), years);

    }

    public static List<DurationLibs<LocalDateTime, String>> years(LocalDate before, long years) {

        // 开始时间
        return LongStream.range(0, years)
                .mapToObj(e -> {
                    LocalDateTime firstTime = Times.firstTimeOfYear(before.plusYears(e));
                    DurationLibs<LocalDateTime, String> libs = new DurationLibs<>();
                    LocalDateTime localTime = Times.lastTimeOfYear(firstTime.toLocalDate());
                    libs.setBefore(firstTime);
                    libs.setAfter(localTime);
                    libs.setName(Times.format(localTime, Constants.YEAR_PATTERN));
                    return libs;
                })
                .collect(Collectors.toList());
    }

    private static void validate(TemporalAccessor before, TemporalAccessor after) {
        if (Objects.isNull(before) || Objects.isNull(after)) {
            throw new IllegalArgumentException("before or after time can not null.");
        }
    }

    private static LocalDateTime tenSeconds(LocalDateTime time) {
        return LocalDateTime.of(time.toLocalDate(), LocalTime.of(time.getHour(), time.getMinute(), (time.getSecond() / 10) * 10));
    }

    public static void main(String[] args) {
//        minutes().forEach(System.out::println);
//        hours().forEach(System.out::println);
//        months(Constants.MONTH_PATTERN_CH).forEach(System.out::println);
//        quarters().forEach(System.out::println);
       /* LocalDateTime DEFAULT_TIME = LocalDateTime.of(2019, 1, 1, 0, 0, 0);
        halfYears(DEFAULT_TIME, LocalDateTime.now()).forEach(System.out::println);*/
//        years().forEach(System.out::println);

//        LocalDate date = LocalDate.of(2019, 1, 1);
//        LocalDate end = LocalDate.of(2019, 2, 2);

//
//        LocalDateTime start = LocalDateTime.now().plusHours(-10);
//        LocalDateTime endTime = LocalDateTime.now();
//        List<DurationLibs<LocalDateTime, String>> durationLibs = Durations.minutes(5);
////
//        System.out.println(durationLibs.stream().map(DurationLibs::getName).collect(Collectors.toList()));


//
//
//        List<DurationLibs<LocalDateTime, String>> days = days(date, end, "yyy-MM-dd");
//
//        // System.out.println(days.stream().map(DurationLibs::getName).collect(Collectors.toList()));
//
//
//        List<String> minutes = LocalDateTimeUtil.minutes(start, endTime);
//        //* System.out.println(minutes);
//        List<DurationLibs<LocalDateTime, String>> minutes1 = Durations.minutes(start, endTime);
//        System.out.println(minutes1.stream().map(DurationLibs::getName).collect(Collectors.toList()));*//*
//
//
//        System.out.println(LocalDateTimeUtil.hours(start, endTime));
//        System.out.println(hours(start, endTime, Constants.DATE_HOUR_ZERO_PATTERN).stream().map(DurationLibs::getName).collect(Collectors.toList()));


    }
}
