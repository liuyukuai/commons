package com.cutefool.commons.mybatis.dsl.condition;

import com.cutefool.commons.core.Operator;
import org.mybatis.dynamic.sql.AbstractListValueCondition;
import org.mybatis.dynamic.sql.Callback;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class FindInSet<T> extends AbstractListValueCondition<T> {

    private static final FindInSet<?> EMPTY = new FindInSet<>(Collections.emptyList());

    public static <T> FindInSet<T> empty() {
        @SuppressWarnings("unchecked")
        FindInSet<T> t = (FindInSet<T>) EMPTY;
        return t;
    }

    private <S> FindInSet<S> emptyWithCallBack() {
        return new FindInSet<>(Collections.emptyList(), emptyCallback);
    }

    protected FindInSet(Collection<T> values) {
        super(values);
    }

    protected FindInSet(Collection<T> values, Callback emptyCallback) {
        super(values, emptyCallback);
    }

    public static <T> FindInSet<T> of(Collection<T> values) {
        return new FindInSet<>(values);
    }

    @SafeVarargs
    public static <T> FindInSet<T> of(T... values) {
        return of(Arrays.asList(values));
    }

    @Override
    public AbstractListValueCondition<T> filter(Predicate<? super T> predicate) {
        return filterSupport(predicate, FindInSet::new, this, this::emptyWithCallBack);
    }

    @Override
    public AbstractListValueCondition<T> withListEmptyCallback(Callback callback) {
        return new FindInSet<>(values, callback);
    }

    @Override
    public String renderCondition(String columnName, Stream<String> placeholders) {
        return "(" + placeholders
                .map(e -> " FIND_IN_SET(" + e + "," + columnName + ") ")
                .collect(Collectors.joining(Operator.OR.name())) + ")";
    }

    public <R> FindInSet<R> map(Function<? super T, ? extends R> mapper) {
        return this.mapSupport(mapper, FindInSet::new, FindInSet::empty);
    }
}
