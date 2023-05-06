package com.cutefool.commons.mybatis.dsl.utils;

import com.cutefool.commons.mybatis.dsl.condition.FindInSet;
import com.cutefool.commons.mybatis.dsl.condition.MatchBool;
import com.cutefool.commons.mybatis.dsl.condition.MatchNatural;
import com.github.pagehelper.Page;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.DurationLibs;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.cycle.Cycle;
import com.cutefool.commons.core.page.PageResponse;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.ResponseCode;
import com.cutefool.commons.core.page.Sort;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.expression.ExpressionException;
import com.cutefool.commons.expression.Variables;
import com.cutefool.commons.orm.PageFunction;
import com.cutefool.commons.orm.PageRequest;
import org.apache.commons.text.StringEscapeUtils;
import org.mybatis.dynamic.sql.SortSpecification;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.VisitableCondition;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.select.SimpleSortSpecification;

import java.util.*;
import java.util.stream.Collectors;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class MybatisUtils {

    private static final PageFunction defaultFunction = (sorts -> sorts);

    private static final List<Operator> operators_in = Arrays.asList(Operator.EQ, Operator.IN);
    private static final List<Operator> operators_not_in = Arrays.asList(Operator.NE, Operator.NOT_IN);

    public static PageRequest of(Paging paging) {
        return of(paging, defaultFunction);
    }

    public static PageRequest of(Paging paging, List<Sort> defaultSorts) {
        return of(paging, defaultFunction, defaultSorts);
    }

    public static PageRequest of(Paging paging, PageFunction pageFunction) {
        return of(paging, pageFunction, Lists.newArrayList());
    }

    public static PageRequest of(Paging paging, PageFunction pageFunction, List<Sort> defaultSorts) {
        PageRequest pageRequest = paging.get(pageFunction);
        List<Sort> sorts = pageRequest.getSorts();
        if (Lists.isEmpty(sorts)) {
            pageRequest.setSorts(defaultSorts);
        }
        pageRequest.setPage(pageRequest.getPage() + 1);
        return pageRequest;
    }

    public static Collection<SortSpecification> of(Map<String, SqlColumn<?>> columnMap, Collection<Sort> sorts) {
        return Lists.empty(sorts)
                .stream()
                .map(e -> {
                    // 需要把null排到最后
                    List<SortSpecification> sortSpecifications = Lists.newArrayList();
                    boolean nullLast = e.isNullLast();
                    String name = e.getName();
                    SqlColumn<?> sqlColumn = columnMap.get(name);
                    if (Objects.isNull(sqlColumn)) {
                        return sortSpecifications;
                    }
                    if (nullLast) {
                        SimpleSortSpecification nullSort = SimpleSortSpecification.of(sqlColumn.name() + " is null");
                        sortSpecifications.add(nullSort);
                    }
                    if (Objects.equals(e.getDirection(), Sort.DESC)) {
                        sortSpecifications.add(sqlColumn.descending());
                    } else {
                        sortSpecifications.add(sqlColumn);
                    }
                    return sortSpecifications;
                })
                .filter(Lists::iterable)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    public static Collection<SortSpecification> of(Map<String, SqlColumn<?>> columnMap, Sort... sorts) {
        return of(columnMap, Arrays.asList(sorts));
    }


    public static SqlColumn<?> findByName(String name, Map<String, SqlColumn<?>> columnMap) {
        return Maps.empty(columnMap).get(name);
    }


    public static void apply(QueryExpressionDSL<SelectModel> dsl, PageRequest paging) {
        dsl.limit(paging.getSize()).offset((paging.getPage() - 1L) * paging.getSize());
    }

    public static <T> PageResponse<T> apply(Page<T> objects) {
        return PageResponse.apply(objects.getTotal(), objects.getResult());
    }


    @SuppressWarnings("all")
    public static VisitableCondition initCondition(Operator operator, Object value) {
        return initCondition(operator, value, null);
    }

    public static VisitableCondition initCondition(Operator operator, Object value, Cycle cycle) {
        // 特殊处理值包含，的查询 并且是EQ,NE,IN,NOT_IN的处理
        if (value instanceof String) {
            String v = Strings.empty(value);
            // EQ IN
            if (v.contains(Strings.COMMA) && operators_in.contains(operator)) {
                List<String> split = Strings.split(v);
                return isIn(split);
            }
            // NE NOT_IN
            if (v.contains(Strings.COMMA) && operators_not_in.contains(operator)) {
                List<String> split = Strings.split(v);
                return isNotIn(split);
            }
        }

        switch (operator) {
            case EQ:
                return isEqualTo(value);
            case NE:
                return isNotEqualTo(value);
            case LIKE:
                return isLike("%" + StringEscapeUtils.escapeXSI(value.toString()) + "%");
            case LT:
                return isLessThan(value);
            case GT:
                return isGreaterThan(value);
            case LTE:
                return isLessThanOrEqualTo(value);
            case GTE:
                return isGreaterThanOrEqualTo(value);
            case IN:
                return isIn(Lists.toCollection(value));
            case NOT_IN:
                return isNotIn(Lists.toCollection(value));
            case FIND_IN_SET:
                if (value instanceof Collection) {
                    return FindInSet.of((Collection<?>) value);
                }
                List<String> split = Strings.split(Strings.empty(value));
                return FindInSet.of(split);
            case MATCH_NATURAL:
                return MatchNatural.of(value);
            case MATCH_BOOL:
                return MatchBool.of(value);
            case BETWEEN:
                int betweenSize = 2;
                // 如果是between，使用逗号分隔
                List<String> values = Strings.split(Strings.empty(value));
                // 如果值为空
                if (Lists.isEmpty(values)) {
                    throw new ExpressionException(ResponseCode.EXPRESSION_ERROR);
                }
                // 如果值只有一个
                if (values.size() < betweenSize) {
                    DurationLibs<?, ?> durationLibs = Variables.parseBetween(Strings.empty(value), cycle, true);
                    return isBetween((Object) durationLibs.getBefore()).and(durationLibs.getAfter());
                }
                Object before = Variables.parseVariable(values.get(0), false);
                Object after = Variables.parseVariable(values.get(1), false);
                return isBetween(before).and(after);
            default:
                throw new BizException("operator not support.");
        }
    }
}
