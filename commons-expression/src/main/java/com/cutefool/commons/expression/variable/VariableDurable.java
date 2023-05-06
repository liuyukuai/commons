/*
 *
 */
package com.cutefool.commons.expression.variable;

import com.cutefool.commons.core.DurationLibs;
import com.cutefool.commons.core.util.Times;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * 持续时间变量
 *
 * @author 271007729@qq.com
 * @date 9/8/21 6:36 PM
 */
public enum VariableDurable implements Variably, Durable<LocalDateTime, Long> {

    /**
     * 当前小时
     */
    CURRENT_DURABLE_MINUTES(
            "${CURRENT_DURABLE_MINUTES}",
            "当前分钟内",
            LocalDateTime::plusMinutes,
            LocalDateTime::plusMinutes
    ),

    /**
     * 当前小时
     */
    CURRENT_DURABLE_HOUR(
            "${CURRENT_DURABLE_HOUR}",
            "当前小时内",
            LocalDateTime::plusHours,
            LocalDateTime::plusHours
    ),

    /**
     * 当前天
     */
    CURRENT_DURABLE_DAY("${CURRENT_DURABLE_DAY}",
            "当天内",
            LocalDateTime::plusDays,
            LocalDateTime::plusDays
    ),

    /**
     * 当前周
     */
    CURRENT_DURABLE_WEEK("${CURRENT_DURABLE_WEEK}",
            "当周内",
            LocalDateTime::plusWeeks,
            LocalDateTime::plusWeeks
    ),

    /**
     * 当前月
     */
    CURRENT_DURABLE_MONTH("${CURRENT_DURABLE_MONTH}",
            "当月内",
            LocalDateTime::plusMonths,
            LocalDateTime::plusMonths
    ),
    /**
     * 当前年
     */
    CURRENT_DURABLE_YEAR("${CURRENT_DURABLE_YEAR}", "当年内",
            LocalDateTime::plusYears,
            LocalDateTime::plusYears
    );

    private String name;

    private String expression;

    private BiFunction<LocalDateTime, Long, LocalDateTime> afterFunction;

    private BiFunction<LocalDateTime, Long, LocalDateTime> beforeFunction;

    VariableDurable(String expression,
                    String name,
                    BiFunction<LocalDateTime, Long, LocalDateTime> afterFunction,
                    BiFunction<LocalDateTime, Long, LocalDateTime> beforeFunction) {

        this.name = name;
        this.expression = expression;
        this.afterFunction = afterFunction;
        this.beforeFunction = beforeFunction;
    }

    @Override
    public String expression() {
        return expression;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public LocalDateTime before() {
        if (Objects.equals(CURRENT_DURABLE_MINUTES, this)) {
            return Times.firstTimeOfMinutes();
        }

        if (Objects.equals(CURRENT_DURABLE_HOUR, this)) {
            return Times.firstTimeOfHour();
        }

        if (Objects.equals(CURRENT_DURABLE_DAY, this)) {
            return Times.firstTimeOfDay();
        }

        if (Objects.equals(VariableDurable.CURRENT_DURABLE_WEEK, this)) {
            return Times.firstTimeOfWeek();
        }
        if (Objects.equals(VariableDurable.CURRENT_DURABLE_MONTH, this)) {
            return Times.firstTimeOfMonth();
        }

        if (Objects.equals(VariableDurable.CURRENT_DURABLE_YEAR, this)) {
            return Times.firstTimeOfYear();
        }
        throw new RuntimeException();
    }

    @Override
    public LocalDateTime before(Long times) {
        LocalDateTime before = this.before();
        return this.beforeFunction.apply(before, times);
    }

    @Override
    public LocalDateTime after() {

        if (Objects.equals(CURRENT_DURABLE_MINUTES, this)) {
            return Times.lastTimeOfMinutes();
        }

        if (Objects.equals(CURRENT_DURABLE_HOUR, this)) {
            return Times.lastTimeOfHour();
        }

        if (Objects.equals(CURRENT_DURABLE_DAY, this)) {
            return Times.lastTimeOfDay();
        }

        if (Objects.equals(VariableDurable.CURRENT_DURABLE_WEEK, this)) {
            return Times.lastTimeOfWeek();
        }
        if (Objects.equals(VariableDurable.CURRENT_DURABLE_MONTH, this)) {
            return Times.lastTimeOfMonth();
        }

        if (Objects.equals(VariableDurable.CURRENT_DURABLE_YEAR, this)) {
            return Times.lastTimeOfYear();
        }
        throw new RuntimeException();
    }

    @Override
    public LocalDateTime after(Long times) {
        LocalDateTime after = this.after();
        return this.afterFunction.apply(after, times);
    }

    public static boolean isSelf(String expression) {
        return Stream.of(VariableDurable.values()).anyMatch(e -> expression.contains(e.name()));
    }

    @Override
    public DurationLibs<LocalDateTime, String> duration() {
        return new DurationLibs<>(this.before(), this.after());
    }}
