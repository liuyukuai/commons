/*
 *
 */
package com.cutefool.commons.core.cycle;

import com.cutefool.commons.core.Constants;
import com.cutefool.commons.core.DurationLibs;
import com.cutefool.commons.core.util.Durations;
import com.cutefool.commons.core.util.Times;
import com.cutefool.commons.core.function.ThirdFunction;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 周期
 *
 * @author 271007729@qq.com
 * @date 9/3/21 2:09 PM
 */
@SuppressWarnings("unused")
public enum Cycle {


    /**
     * 周期频率
     */
    MINUTES("minutes",
            "分钟",
            (cycle, time, times) -> {
                return time.plusSeconds(60 * times);
            },
            Durations::minutes,
            () -> Durations.minutes(Times.firstTimeOfMinutes(), Times.lastTimeOfMinutes()),
            (time) -> Times.format(time, Constants.DATE_MINUTES_PATTERN),
            (time) -> new DurationLibs<>(Times.firstTimeOfMinutes(time),Times.lastTimeOfMinutes(time))
    ),
    HOUR("hour",
            "小时",
            (cycle, time, times) -> {
                return time.plusMinutes(60 * times);
            },
            Durations::hours, () -> Durations.hours(Times.firstTimeOfHour(), Times.lastTimeOfHour()),
            (time) -> Times.format(time, Constants.DATE_HOUR_PATTERN),
            (time) -> new DurationLibs<>(Times.firstTimeOfHour(time),Times.lastTimeOfHour(time))
    ),
    DAY("day",
            "天",
            (cycle, time, times) -> {
                return time.plusDays(times);
            },
            (before, after) -> Durations.days(before, after, Constants.DATE_PATTERN),
            () -> Durations.hours(Times.firstTimeOfDay(), Times.lastTimeOfDay()),
            (time) -> Times.format(time, Constants.DATE_PATTERN),
            (time) -> new DurationLibs<>(Times.firstTimeOfDay(time.toLocalDate()),Times.lastTimeOfDay(time.toLocalDate()))
    ),

    WEEK("week",
            "周",
            (cycle, time, times) ->

            {
                return time.plusDays(7 * times);
            },
            Durations::weeks,
            () -> Durations.hours(Times.firstTimeOfWeek(), Times.lastTimeOfWeek()),
            Durations.WEEK_PATTERN,
            (time) -> new DurationLibs<>(Times.firstTimeOfWeek(time.toLocalDate()),Times.lastTimeOfWeek(time.toLocalDate()))
    ),
    MONTH("month",
            "月",
            (cycle, time, times) ->

            {
                return time.plusMonths(times);
            },
            Durations::months,
            () -> Durations.hours(Times.firstTimeOfMonth(), Times.lastTimeOfMonth()),
            (time) -> Times.format(time, Constants.MONTH_PATTERN),
            (time) -> new DurationLibs<>(Times.firstTimeOfMonth(time.toLocalDate()),Times.lastTimeOfMonth(time.toLocalDate()))
    ),

    QUARTER("quarter",
            "季度",
            (cycle, time, times) ->

            {
                return time.plusMonths(3 * times);
            },
            Durations::quarters,
            () -> Durations.hours(Times.firstTimeOfQuarter(), Times.lastTimeOfQuarter()),
            Durations.QUARTERS_PATTERN,
            (time) -> new DurationLibs<>(Times.firstTimeOfQuarter(time.toLocalDate()),Times.lastTimeOfQuarter(time.toLocalDate()))
    ),

    HALF_YEAR("halfYear",
            "半年度",
            (cycle, time, times) ->

            {
                return time.plusMonths(6 * times);
            },
            Durations::halfYears,
            () -> Durations.hours(Times.firstTimeOfHalfYear(), Times.lastTimeOfHalfYear()),
            Durations.HALF_YEAR_PATTERN,
            (time) -> new DurationLibs<>(Times.firstTimeOfHalfYear(time.toLocalDate()),Times.lastTimeOfHalfYear(time.toLocalDate()))
    ),

    YEAR("year",
            "年",
            (cycle, time, times) ->

            {
                return time.plusYears(times);
            },
            Durations::years,
            () -> Durations.hours(Times.firstTimeOfYear(), Times.lastTimeOfYear()),
            (time) -> Times.format(time, Constants.YEAR_PATTERN),
            (time) -> new DurationLibs<>(Times.firstTimeOfYear(time.toLocalDate()),Times.lastTimeOfHalfYear(time.toLocalDate()))
    ),

    ONCE("once",
            "一次",
            (cycle, time, times) -> time,
            (before, after) -> Collections.emptyList(),
            Collections::emptyList,
            Times::format,
            (time) -> new DurationLibs<>(time,time)
    );

    /**
     * key
     */
    private String key;
    /**
     * 名称
     */
    private String name;

    /**
     * 计算公式
     */
    private ThirdFunction<LocalDateTime, Cycle, LocalDateTime, Long> nextTimeFunction;

    private BiFunction<LocalDateTime, LocalDateTime, List<DurationLibs<LocalDateTime, String>>> durationFunction;

    private Supplier<List<DurationLibs<LocalDateTime, String>>> defaultDurationFunction;

    private Function<LocalDateTime, String> formatFunction;
    /**
     * 算指定时间的周期时间段
     */
    private Function<LocalDateTime, DurationLibs<LocalDateTime,String>> betweenDurationFunction;

    Cycle(String key, String name, ThirdFunction<LocalDateTime, Cycle, LocalDateTime, Long> nextTimeFunction,
          BiFunction<LocalDateTime, LocalDateTime, List<DurationLibs<LocalDateTime, String>>> durationFunction,
          Supplier<List<DurationLibs<LocalDateTime, String>>> defaultDurationFunction,
          Function<LocalDateTime, String> formatFunction,
         Function<LocalDateTime, DurationLibs<LocalDateTime,String>> betweenDurationFunction
    ) {
        this.key = key;
        this.name = name;
        this.nextTimeFunction = nextTimeFunction;
        this.durationFunction = durationFunction;
        this.defaultDurationFunction = defaultDurationFunction;
        this.formatFunction = formatFunction;
        this.betweenDurationFunction = betweenDurationFunction;

    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public ThirdFunction<LocalDateTime, Cycle, LocalDateTime, Long> nextTimeFunction() {
        return nextTimeFunction;
    }

    public BiFunction<LocalDateTime, LocalDateTime, List<DurationLibs<LocalDateTime, String>>> durationFunction() {
        return durationFunction;
    }

    public Supplier<List<DurationLibs<LocalDateTime, String>>> defaultDurationFunction() {
        return defaultDurationFunction;
    }

    public Function<LocalDateTime, DurationLibs<LocalDateTime,String>> betweenDurationFunction() {
        return betweenDurationFunction;
    }

    public Function<LocalDateTime, String> formatFunction() {
        return formatFunction;
    }

    public static Cycle getCycle(String key) {
        Cycle[] values = Cycle.values();
        for (Cycle value : values) {
            if (Objects.equals(value.key, key)) {
                return value;
            }
        }
        return null;
    }
}
