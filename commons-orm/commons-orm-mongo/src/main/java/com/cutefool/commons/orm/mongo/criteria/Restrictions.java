package com.cutefool.commons.orm.mongo.criteria;

import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.orm.Transformation;
import com.cutefool.commons.orm.Transforms;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Restrictions<T> {

    private Query query;


    private Restrictions(Query query) {
        this.query = query;

    }

    public static Restrictions of() {
        return new Restrictions<>(new Query());
    }


    public <E> Restrictions where(E query) {
        Map<String, Transformation> fields = Transforms.fields(query);
        fields.forEach((k, v) -> parse(v));
        return this;
    }

    public Restrictions<T> eq(String name, Object value) {
        this.query.addCriteria(Criteria.where(name).is(value));
        return this;
    }

    public Restrictions<T> eq(String name, Object value, boolean ignoreEmpty) {
        boolean isIgnore = (Objects.isNull(value) || StringUtils.isBlank(value.toString())) && ignoreEmpty;
        if (isIgnore) {
            return this;
        }
        this.query.addCriteria(Criteria.where(name).is(value));
        return this;
    }


//    public Restrictions<T> hasMember(String name, Object value) {
//        this.query.addCriteria(Criteria.where(name).elemMatch());
//        return this;
//    }
//
//
//    public Restrictions<T> hasMember(String name, Object value, boolean ignoreEmpty) {
//        this.query.addCriteria(parse(this.query, name, value, ignoreEmpty, Operator.IS_MEMBER));
//        return this;
//    }


    public Restrictions<T> ne(String name, Object value) {
        this.query.addCriteria(Criteria.where(name).ne(value));
        return this;
    }

    public Restrictions<T> ne(String name, Object value, boolean ignoreEmpty) {
        boolean isIgnore = (Objects.isNull(value) || StringUtils.isBlank(value.toString())) && ignoreEmpty;
        if (isIgnore) {
            return this;
        }
        this.query.addCriteria(Criteria.where(name).ne(value));
        return this;
    }


    public Restrictions<T> like(String name, String value) {
        Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
        this.query.addCriteria(Criteria.where(name).regex(pattern));
        return this;
    }

    public Restrictions<T> like(String name, String value, boolean ignoreEmpty) {
        boolean isIgnore = (Objects.isNull(value) || StringUtils.isBlank(value)) && ignoreEmpty;
        if (isIgnore) {
            return this;
        }
        Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
        this.query.addCriteria(Criteria.where(name).regex(pattern));
        return this;
    }


    public Restrictions<T> gt(String name, Object value) {
        this.query.addCriteria(Criteria.where(name).gt(value));
        return this;
    }


    public Restrictions<T> gt(String name, Object value, boolean ignoreEmpty) {
        boolean isIgnore = (Objects.isNull(value) || StringUtils.isBlank(value.toString())) && ignoreEmpty;
        if (isIgnore) {
            return this;
        }
        this.query.addCriteria(Criteria.where(name).gt(value));
        return this;
    }

    public Restrictions<T> lt(String name, Object value) {
        this.query.addCriteria(Criteria.where(name).lt(value));
        return this;
    }

    public Restrictions<T> lt(String name, Object value, boolean ignoreEmpty) {
        boolean isIgnore = (Objects.isNull(value) || StringUtils.isBlank(value.toString())) && ignoreEmpty;
        if (isIgnore) {
            return this;
        }
        this.query.addCriteria(Criteria.where(name).lt(value));
        return this;
    }


    public Restrictions<T> lte(String name, Object value) {
        this.query.addCriteria(Criteria.where(name).lte(value));
        return this;
    }


    public Restrictions<T> lte(String name, Object value, boolean ignoreEmpty) {
        boolean isIgnore = (Objects.isNull(value) || StringUtils.isBlank(value.toString())) && ignoreEmpty;
        if (isIgnore) {
            return this;
        }
        this.query.addCriteria(Criteria.where(name).lte(value));
        return this;
    }

    public Restrictions<T> gte(String name, Object value) {
        this.query.addCriteria(Criteria.where(name).gte(value));
        return this;
    }

    public Restrictions<T> groupByKey(String key, String... fields) {
        GroupOperation as = Aggregation.group(fields).first(key).as(key);
        return this;
    }

    public Restrictions<T> gte(String name, Object value, boolean ignoreEmpty) {
        boolean isIgnore = (Objects.isNull(value) || StringUtils.isBlank(value.toString())) && ignoreEmpty;
        if (isIgnore) {
            return this;
        }
        this.query.addCriteria(Criteria.where(name).gte(value));
        return this;
    }


    public Restrictions<T> in(String name, Collection value) {
        this.query.addCriteria(Criteria.where(name).in(value));
        return this;

    }

    public Restrictions<T> in(String name, Collection value, boolean ignoreEmpty) {
        boolean isIgnore = (Objects.isNull(value) || StringUtils.isBlank(value.toString())) && ignoreEmpty;
        if (isIgnore) {
            return this;
        }
        this.query.addCriteria(Criteria.where(name).in(value));
        return this;

    }


    public Restrictions<T> notIn(String name, Collection value) {
        this.query.addCriteria(Criteria.where(name).nin(value));
        return this;

    }

    public Restrictions<T> notIn(String name, Collection value, boolean ignoreEmpty) {
        boolean isIgnore = (Objects.isNull(value) || StringUtils.isBlank(value.toString())) && ignoreEmpty;
        if (isIgnore) {
            return this;
        }
        this.query.addCriteria(Criteria.where(name).nin(value));
        return this;

    }


    public Restrictions<T> and(Criteria... criteria) {
        Criteria and = new Criteria();
        this.query.addCriteria(and.andOperator(criteria));
        return this;

    }

    public Restrictions<T> or(Criteria... criteria) {
        Criteria and = new Criteria();
        this.query.addCriteria(and.orOperator(criteria));
        return this;

    }


    public Query get() {
        return query;
    }


    private static boolean ignore(Object value, boolean ignoreEmpty) {
        if (Objects.isNull(value) && ignoreEmpty) {
            return true;
        }
        if (value instanceof String) {
            String v = (String) value;
            return StringUtils.isBlank(v) && ignoreEmpty;
        }

        if (value instanceof Collection) {
            Collection v = (Collection) value;
            return v.isEmpty() && ignoreEmpty;
        }
        return false;
    }

    private Criteria parse(String name, Object value, boolean ignoreEmpty, Operator operator) {
        switch (operator) {
            case EQ:
                return Criteria.where(name).is(value);
            case NE:
                return Criteria.where(name).ne(value);
            case LIKE:
                //完全匹配
                //Pattern pattern = Pattern.compile("^hzb$", Pattern.CASE_INSENSITIVE);
                //右匹配
                //Pattern pattern = Pattern.compile("^.*hzb$", Pattern.CASE_INSENSITIVE);
                //左匹配
                //Pattern pattern = Pattern.compile("^hzb.*$", Pattern.CASE_INSENSITIVE);
                Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
                return Criteria.where(name).regex(pattern);
            case LT:
                return Criteria.where(name).lt(value);
            case GT:
                return Criteria.where(name).gt(value);
            case LTE:
                return Criteria.where(name).lte(value);
            case GTE:
                return Criteria.where(name).gte(value);
            case IN:
                return Criteria.where(name).in(value);
            case NOT_IN:
                return Criteria.where(name).nin(value);
            default:
                throw new BizException("operator not support.");
        }
    }


    private void parse(Transformation transformation) {
        if (ignore(transformation.getValue(), transformation.isIgnoreEmpty())) {
            return;
        }
        String[] fields = transformation.getField();
        if (Array.getLength(fields) == 1) {
            Criteria parse = this.parse(fields[0], transformation.getValue(), transformation.isIgnoreEmpty(), transformation.getOperator());
            this.query.addCriteria(parse);
        } else {
            Criteria criteria = new Criteria();
            List<Criteria> criterion = Stream.of(fields)
                    .map(k -> this.parse(k, transformation.getValue(), transformation.isIgnoreEmpty(), transformation.getOperator()))
                    .collect(Collectors.toList());
            Criteria[] criteriaList = criterion.toArray(new Criteria[]{});
            if (Operator.OR == transformation.getRelation()) {
                this.query.addCriteria(criteria.orOperator(criteriaList));
            } else {
                this.query.addCriteria(criteria.orOperator(criteriaList));
            }
        }
    }

}
