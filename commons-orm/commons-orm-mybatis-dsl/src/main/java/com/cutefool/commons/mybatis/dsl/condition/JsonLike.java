package com.cutefool.commons.mybatis.dsl.condition;

import lombok.Getter;
import org.mybatis.dynamic.sql.AbstractSingleValueCondition;

import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class JsonLike<T> extends AbstractSingleValueCondition<T> {

    @Getter
    private String fields;

    private static final JsonLike<?> EMPTY = new JsonLike<Object>(null, null) {
        public boolean shouldRender() {
            return false;
        }
    };

    public static <T> JsonLike<T> empty() {
        @SuppressWarnings("unchecked")
        JsonLike<T> t = (JsonLike<T>) EMPTY;
        return t;
    }

    protected JsonLike(String fields, T value) {
        super(value);
        this.fields = fields;
    }

    public static <T> JsonLike<T> of(String fields, T value) {
        return new JsonLike<>(fields, value);
    }

    @Override
    public AbstractSingleValueCondition<T> filter(Predicate<? super T> predicate) {
        return this.filterSupport(predicate, JsonLike::empty, this);
    }

    @Override
    public String renderCondition(String columnName, String placeholder) {
        return columnName + " -> '$." + fields + "' LIKE " + placeholder;
    }

    public <R> JsonLike<R> map(Function<? super T, ? extends R> mapper) {
        return this.mapSupport(mapper, v -> new JsonLike<>(fields, v), JsonLike::empty);
    }
}
