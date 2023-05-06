/*
 *  
 */
package com.cutefool.commons.core.cycle;

import com.cutefool.commons.core.DurationLibs;
import com.cutefool.commons.core.Operator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 周期时间计算
 * CronSequenceGenerator
 *
 * @author 271007729@qq.com
 * @date 9/3/21 2:09 PM
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Cycles {

    /**
     * 周期默认开始时间
     */
    public static LocalDateTime DEFAULT_TIME = LocalDateTime.of(2019, 1, 1, 0, 0, 0);

    public static boolean isOnce(String cycle) {
        return Objects.equals(Cycle.ONCE.getKey(), cycle);
    }

    /**
     * 计算下一次执行时间
     *
     * @param cycle 周期
     * @param time  开始时间
     * @return 时间
     */
    public static LocalDateTime next(String cycle, LocalDateTime time) {
        return next(Cycle.getCycle(cycle), 1L, time);
    }

    /**
     * 计算下一次执行时间
     *
     * @param cycle 周期
     * @param time  开始时间
     * @return 时间
     */
    public static LocalDateTime next(Cycle cycle, LocalDateTime time) {
        if (Objects.isNull(cycle)) {
            return null;
        }
        return next(cycle, 1L, time);
    }

    /**
     * 计算地times次执行时间
     *
     * @param cycle 周期
     * @param times 第几次
     * @param time  开始时间
     * @return 时间
     */
    public static LocalDateTime next(String cycle, Long times, LocalDateTime time) {
        return next(Cycle.getCycle(cycle), times, time);
    }

    /**
     * 计算地times次执行时间
     *
     * @param cycle 周期
     * @param times 第几次
     * @param time  开始时间
     * @return 时间
     */
    public static LocalDateTime next(Cycle cycle, Long times, LocalDateTime time) {
        if (Objects.isNull(cycle)) {
            return null;
        }
        return cycle.nextTimeFunction().apply(cycle, time, times);
    }

    public static DurationLibs<LocalDateTime, String> between(Cycle cycle, LocalDateTime time) {
        return cycle.betweenDurationFunction().apply(time);
    }

    public static List<DurationLibs<LocalDateTime, String>> durations(Cycle cycle, LocalDateTime before) {
        return durations(cycle, before, null);
    }

    public static List<DurationLibs<LocalDateTime, String>> durations(Cycle cycle, LocalDateTime before, Operator operator) {

        if (Objects.isNull(cycle)) {
            return Collections.emptyList();
        }

        LocalDateTime now = LocalDateTime.now();
        List<DurationLibs<LocalDateTime, String>> durations = cycle.durationFunction().apply(before, now);
        // 如果需要校验时间
        if (Objects.nonNull(operator)) {
            durations = durations.stream().filter(e -> {
                LocalDateTime first = e.getBefore();
                LocalDateTime after = e.getAfter();
                // if eq
                if (Objects.equals(Operator.LTE, operator)) {
                    return (first.isAfter(before) || first.isEqual(before)) && (after.isBefore(now) || after.isEqual(now));
                }
                if (Objects.equals(Operator.LT, operator)) {
                    // 需要再时间范围内
                    return (first.isAfter(before) || first.isEqual(before)) && (after.isBefore(now));
                }
                return true;
            }).collect(Collectors.toList());

        }
        durations.sort(Comparator.comparing(DurationLibs::getBefore));
        return durations;
    }

    public static Map<String, DurationLibs<LocalDateTime, String>> mapDurations(Cycle cycle, LocalDateTime before) {
        return mapDurations(cycle, before, null);
    }

    public static Map<String, DurationLibs<LocalDateTime, String>> mapDurations(Cycle cycle, LocalDateTime before, Operator operator) {
        return durations(cycle, before, operator).stream().collect(Collectors.toMap(DurationLibs::getName, e -> e));
    }

    public static String cycle(Cycle cycle, LocalDateTime time) {
        return cycle.formatFunction().apply(time);
    }

    public static void main(String[] args) {
        System.out.println(Cycles.durations(Cycle.MINUTES, LocalDateTime.now()));

    }
}
