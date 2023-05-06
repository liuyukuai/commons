/*
 *
 */
package com.cutefool.commons.orm.influx.support;

import com.cutefool.commons.orm.influx.query.InfluxQuery;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.page.Paging;
import com.cutefool.commons.core.page.Sort;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.core.util.Times;
import com.cutefool.commons.orm.PageRequest;
import com.cutefool.commons.orm.PagingUtils;
import com.cutefool.commons.orm.Transformation;
import com.cutefool.commons.orm.Transforms;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 转换条件类
 *
 * @author 271007729@qq.com
 * @date 11/2/21 5:19 PM
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class Restrictions {
    /**
     * 需要排除的字段
     */
    private static final List<String> excludes = Arrays.asList("startTime", "endTime");

    private StringBuilder sb;


    private Restrictions(StringBuilder sb) {
        this.sb = sb;
    }

    public static Restrictions of(String bucket) {
        StringBuilder sb = new StringBuilder();
        sb.append("from(bucket:\"").append(bucket).append("\")\n");
        return new Restrictions(sb);
    }

    public <E extends InfluxQuery> Restrictions measurement(String measurement) {
        return this.measurement(measurement, new String[]{});
    }

    public <E extends InfluxQuery> Restrictions measurement(String measurement, String... fields) {
        sb.append(" |> filter( fn: (r) => r[\"_measurement\"] == \"").append(measurement);
        if (Objects.nonNull(fields) && fields.length > 0) {
            for (String field : fields) {
                sb.append(" and  r._field == ").append(field);
            }
        }
        sb.append("\")\n");
        return this;
    }

    public <E extends InfluxQuery> Restrictions range(LocalDateTime start) {
        return range(start, LocalDateTime.now());
    }

    public <E extends InfluxQuery> Restrictions range(LocalDateTime start, LocalDateTime endTime) {
        if (Objects.nonNull(start) && Objects.nonNull(endTime)) {
            sb.append(" |> range(start:").append(Times.from(start)).append(",stop:").append(Times.from(endTime)).append(")\n");
        }
        return this;
    }

    public <E extends InfluxQuery> Restrictions where(E query) {
        // 处理开始时间、结束时间
        sb.append(" |> range(start:").append(Times.from(query.getStartTime())).append(",stop:").append(Times.from(query.getEndTime())).append(")\n");
        Map<String, Transformation> fields = Transforms.fields(query, name -> !excludes.contains(name));
        fields.forEach((k, v) -> parse(v));
        return this;
    }

    public <E extends InfluxQuery> Restrictions count() {
        // 处理求总数
        sb.append(" |> count()");
        return this;
    }

    public <E extends InfluxQuery> Restrictions page(Paging paging) {
        if (Objects.nonNull(paging)) {
            PageRequest pageRequest = PagingUtils.of(paging);
            List<Sort> sorts = pageRequest.getSorts();
            // 排序
            if (Lists.iterable(sorts)) {

                String fields = sorts.stream().map(Sort::getName).collect(Collectors.joining(Strings.COMMA));
                String direction = sorts.get(0).getDirection();
                // 判断升序还是降序
                boolean isDesc = Objects.equals(Sort.DESC, direction);
                sb.append(" |> sort(columns:[").append(fields).append("],desc:").append(isDesc).append(")");
            }

            int page = pageRequest.getPage();
            int size = pageRequest.getSize();
            // 分页
            int offset = page * size;
            sb.append(" |> limit(n:").append(size).append(",offset:").append(offset).append(")");
        }
        return this;
    }


    public <E extends InfluxQuery> Restrictions filter(String name, Object value) {
        return filter(name, value, Operator.EQ);
    }

    public <E extends InfluxQuery> Restrictions filter(String name, Object value, Operator operator) {
        // 处理求总数
        sb.append(" |> filter( fn: (r) => ");
        sb.append(this.parse(name, value, operator));
        sb.append(")\n");
        return this;
    }

    public <E extends InfluxQuery> Restrictions last() {
        // 最后的值
        sb.append(" |> last(column:\"_time\")");
        return this;
    }

    public <E extends InfluxQuery> Restrictions group(String name) {
        /* 最后的值 */
        sb.append(" |> group(columns:[\"").append(name).append("\"])\n");
        return this;
    }

    public String get() {
        if (log.isDebugEnabled()) {
            log.debug("influx query = {}", sb.toString());
        }
        return this.sb.toString();
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


    private void parse(Transformation transformation) {
        if (ignore(transformation.getValue(), transformation.isIgnoreEmpty())) {
            return;
        }
        sb.append(" |> filter( fn: (r) => ");
        String[] fields = transformation.getField();
        Object value = transformation.getValue();
        if (Array.getLength(fields) == 1) {
            sb.append(this.parse(fields[0], value, transformation.getOperator()));
        } else {
            // 如果多个条件
            String flux = Stream.of(fields)
                    .map(e -> this.parse(e, transformation.getValue(), transformation.getOperator())).collect(Collectors.joining(" " + transformation.getRelation().name().toLowerCase() + " "));
            sb.append(flux);
        }
        sb.append(")\n");
    }

    private String condition(String name, Object value, String operator) {
        // r['name'] == 'aa'
        return "r[\"" + name + "\"] " + operator + " \"" + value + "\"";
    }

    private String parse(String name, Object value, Operator operator) {
        switch (operator) {
            case EQ:
                return this.condition(name, value, "==");
            case NE:
                return this.condition(name, value, "<>");
            case LIKE:
                return this.condition(name, "%" + value + "%", "like");
            case LT:
                return this.condition(name, value, "<");
            case GT:
                return this.condition(name, value, ">");
            case LTE:
                return this.condition(name, value, "<=");
            case GTE:
                return this.condition(name, value, ">=");
            case IN:
                // influx不支持in查询转换成or，只支持集合
                return this.in(name, value);
            case NOT_IN:
                return this.notIn(name, value);
            default:
                throw new BizException("operator not support.");
        }
    }

    @SuppressWarnings("unchecked")
    private String in(String name, Object value) {
        // influx不支持in查询转换成or，只支持集合
        Collection<Object> inValues = (Collection) value;
        return inValues.stream()
                .map(e -> this.parse(name, value, Operator.EQ))
                .collect(Collectors.joining(" " + Operator.OR.name().toLowerCase() + " "));
    }

    @SuppressWarnings("unchecked")
    private String notIn(String name, Object value) {
        // influx不支持in查询转换成or，只支持集合
        Collection<Object> notInValues = (Collection) value;
        return notInValues.stream()
                .map(e -> this.parse(name, value, Operator.NE))
                .collect(Collectors.joining(" and "));
    }


}
