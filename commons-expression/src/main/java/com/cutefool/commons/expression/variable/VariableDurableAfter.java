/*
 *
 */
package com.cutefool.commons.expression.variable;

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
public enum VariableDurableAfter implements Variably, DurableBefore<LocalDateTime, Long>, DurableNow<LocalDateTime, Long> {

    /**
     * 分钟后
     */
    AFTER_MINUTES("${AFTER_MINUTES}", "分钟后", LocalDateTime::plusMinutes),

    /**
     * 小时后
     */
    AFTER_HOUR("${AFTER_HOUR}", "小时后", LocalDateTime::plusHours),
    /**
     * 天
     */
    AFTER_DAY("${AFTER_DAY}", "天后", LocalDateTime::plusDays),

    /**
     * 周后
     */
    AFTER_WEEK("${AFTER_WEEK}", "周后", LocalDateTime::plusWeeks),

    /**
     * 月后
     */
    AFTER_MONTH("${AFTER_MONTH}", "月后", LocalDateTime::plusMonths),
    /**
     * 年后
     */
    AFTER_YEAR("${AFTER_YEAR}", "年后", LocalDateTime::plusYears);

    private String name;

    private String expression;

    private BiFunction<LocalDateTime, Long, LocalDateTime> function;

    VariableDurableAfter(String expression, String name, BiFunction<LocalDateTime, Long, LocalDateTime> function) {

        this.name = name;
        this.expression = expression;
        this.function = function;
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

        if (Objects.equals(AFTER_MINUTES, this)) {
            return Times.firstTimeOfMinutes();
        }

        if (Objects.equals(AFTER_HOUR, this)) {
            return Times.firstTimeOfHour();
        }

        if (Objects.equals(VariableDurableAfter.AFTER_DAY, this)) {
            return Times.firstTimeOfDay();
        }

        if (Objects.equals(VariableDurableAfter.AFTER_WEEK, this)) {
            return Times.firstTimeOfWeek();
        }
        if (Objects.equals(VariableDurableAfter.AFTER_MONTH, this)) {
            return Times.firstTimeOfMonth();
        }

        if (Objects.equals(VariableDurableAfter.AFTER_YEAR, this)) {
            return Times.firstTimeOfYear();
        }
        throw new RuntimeException();
    }

    @Override
    public LocalDateTime before(Long times) {
        LocalDateTime before = this.before();
        return this.function.apply(before, times);
    }

    public static boolean isSelf(String expression) {
        return Stream.of(VariableDurableAfter.values()).anyMatch(e -> expression.contains(e.name()));
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDateTime now(Long times) {
        return this.function.apply(now(), times);
    }
}
