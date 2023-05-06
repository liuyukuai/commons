package com.cutefool.commons.mybatis.dsl.condition;

import org.mybatis.dynamic.sql.AbstractSingleValueCondition;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 全文搜索（布尔查找）
 *
 */
@SuppressWarnings("unused")
public class MatchBool<T> extends AbstractSingleValueCondition<T> {

    private static final MatchBool<?> EMPTY = new MatchBool<Object>(null) {
        public boolean shouldRender() {
            return false;
        }
    };

    public static <T> MatchBool<T> empty() {
        @SuppressWarnings("unchecked")
        MatchBool<T> t = (MatchBool<T>) EMPTY;
        return t;
    }

    protected MatchBool(T value) {
        super(value);
    }

    public static <T> MatchBool<T> of(T value) {
        return new MatchBool<>(value);
    }

    @Override
    public AbstractSingleValueCondition<T> filter(Predicate<? super T> predicate) {
        return this.filterSupport(predicate, MatchBool::empty, this);
    }

    @Override
    public String renderCondition(String columnName, String placeholder) {
        return "MATCH(" + columnName + ") AGAINST ('" + placeholder + "' IN BOOLEAN MODE)";
    }

    public <R> MatchBool<R> map(Function<? super T, ? extends R> mapper) {
        return this.mapSupport(mapper, MatchBool::new, MatchBool::empty);
    }
}
