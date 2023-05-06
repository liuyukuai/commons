package com.cutefool.commons.mybatis.dsl.condition;

import lombok.Getter;
import org.mybatis.dynamic.sql.AbstractSingleValueCondition;

import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class JsonArrayLike<T> extends AbstractSingleValueCondition<T> {
    /**
     * JSON要查询的属性名称
     */
    @Getter
    private String fields;

    private static final JsonArrayLike<?> EMPTY = new JsonArrayLike<Object>(null, null) {
        public boolean shouldRender() {
            return false;
        }
    };

    public static <T> JsonArrayLike<T> empty() {
        @SuppressWarnings("unchecked")
        JsonArrayLike<T> t = (JsonArrayLike<T>) EMPTY;
        return t;
    }

    protected JsonArrayLike(String fields, T value) {
        super(value);
        this.fields = fields;
    }

    public static <T> JsonArrayLike<T> of(String fields, T value) {
        return new JsonArrayLike<>(fields, value);
    }

    @Override
    public AbstractSingleValueCondition<T> filter(Predicate<? super T> predicate) {
        return this.filterSupport(predicate, JsonArrayLike::empty, this);
    }

    @Override
    public String renderCondition(String columnName, String placeholder) {
        return columnName + " -> '$[*]." + fields + "' LIKE " + placeholder;
    }

    public <R> JsonArrayLike<R> map(Function<? super T, ? extends R> mapper) {
        return this.mapSupport(mapper, v -> new JsonArrayLike<>(fields, v), JsonArrayLike::empty);
    }
}
