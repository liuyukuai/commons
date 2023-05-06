package com.cutefool.commons.mybatis.dsl.condition;

import org.mybatis.dynamic.sql.AbstractSingleValueCondition;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 全文搜索（自然语言搜索方式）
 */
@SuppressWarnings("unused")
public class MatchNatural<T> extends AbstractSingleValueCondition<T> {

    private static final MatchNatural<?> EMPTY = new MatchNatural<Object>(null) {
        public boolean shouldRender() {
            return false;
        }
    };

    public static <T> MatchNatural<T> empty() {
        @SuppressWarnings("unchecked")
        MatchNatural<T> t = (MatchNatural<T>) EMPTY;
        return t;
    }

    protected MatchNatural(T value) {
        super(value);
    }

    public static <T> MatchNatural<T> of(T value) {
        return new MatchNatural<>(value);
    }

    @Override
    public AbstractSingleValueCondition<T> filter(Predicate<? super T> predicate) {
        return this.filterSupport(predicate, MatchNatural::empty, this);
    }

    @Override
    public String renderCondition(String columnName, String placeholder) {
        return "MATCH(" + columnName + ") AGAINST ('" + placeholder + "')";
    }

    public <R> MatchNatural<R> map(Function<? super T, ? extends R> mapper) {
        return this.mapSupport(mapper, MatchNatural::new, MatchNatural::empty);
    }
}
