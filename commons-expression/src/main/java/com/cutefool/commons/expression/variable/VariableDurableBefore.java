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
public enum VariableDurableBefore implements Variably, DurableAfter<LocalDateTime, Long>, DurableNow<LocalDateTime, Long> {

    /**
     * 分钟前
     */
    BEFORE_MINUTES("${BEFORE_MINUTES}", "分钟前", LocalDateTime::minusMinutes),

    /**
     * 小时前
     */
    BEFORE_HOUR("${BEFORE_HOUR}", "小时前", LocalDateTime::minusHours),
    /**
     * 天
     */
    BEFORE_DAY("${BEFORE_DAY}", "天前", LocalDateTime::minusDays),

    /**
     * 周前
     */
    BEFORE_WEEK("${BEFORE_WEEK}", "周前", LocalDateTime::minusWeeks),

    /**
     * 月前
     */
    BEFORE_MONTH("${BEFORE_MONTH}", "月前", LocalDateTime::minusMonths),
    /**
     * 年前
     */
    BEFORE_YEAR("${BEFORE_YEAR}", "年前", LocalDateTime::minusYears);

    private String name;

    private String expression;

    private BiFunction<LocalDateTime, Long, LocalDateTime> function;

    VariableDurableBefore(String expression, String name, BiFunction<LocalDateTime, Long, LocalDateTime> function) {

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
    public LocalDateTime after() {

        if (Objects.equals(BEFORE_MINUTES, this)) {
            return Times.firstTimeOfMinutes();
        }

        if (Objects.equals(BEFORE_HOUR, this)) {
            return Times.firstTimeOfHour();
        }

        if (Objects.equals(VariableDurableBefore.BEFORE_DAY, this)) {
            return Times.firstTimeOfDay();
        }

        if (Objects.equals(VariableDurableBefore.BEFORE_WEEK, this)) {
            return Times.firstTimeOfWeek();
        }
        if (Objects.equals(VariableDurableBefore.BEFORE_MONTH, this)) {
            return Times.firstTimeOfMonth();
        }

        if (Objects.equals(VariableDurableBefore.BEFORE_YEAR, this)) {
            return Times.firstTimeOfYear();
        }
        throw new RuntimeException();
    }

    @Override
    public LocalDateTime after(Long times) {
        LocalDateTime before = this.after();
        return this.function.apply(before, times);
    }

    public static boolean isSelf(String expression) {
        return Stream.of(VariableDurableBefore.values()).anyMatch(e -> expression.contains(e.name()));
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
