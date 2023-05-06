/*
 *
 */
package com.cutefool.commons.orm.influx.support;

import com.influxdb.annotations.Column;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.extern.slf4j.Slf4j;
import com.cutefool.commons.core.util.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 11/5/21 12:12 PM
 */
@Slf4j
public class InfluxResults {

    private static final List<String> KEYS = Arrays.asList("result", "table", "_start", "_top", "_field", "_value", "_measurement");

    public static <E> List<E> elements(List<FluxTable> tables, Class<E> clazz) {
        if (Lists.isEmpty(tables)) {
            return Collections.emptyList();
        }

        Map<String, E> elements = new HashMap<>(16);
        Lists.empty(tables).stream()
                .map(FluxTable::getRecords)
                .flatMap(List::stream)
                .forEach(e -> {
                    Map<String, Object> values = e.getValues();
                    // 数据主键
                    String key = values.entrySet().stream()
                            .filter(v -> !KEYS.contains(v.getKey()))
                            .map(Map.Entry::getValue)
                            .map(String::valueOf)
                            .collect(Collectors.joining());
                    try {
                        E element = elements.getOrDefault(key, clazz.newInstance());
                        InfluxResults.apply(e, element);
                        elements.putIfAbsent(key, element);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

        return new ArrayList<>(elements.values());
    }


    @Nonnull
    public static <T> T apply(@Nonnull FluxRecord record, T obj) {

        Objects.requireNonNull(record, "Record is required");
        Objects.requireNonNull(obj, "obj  is required");

        try {

            Class<?> clazz = obj.getClass();
            while (clazz != null) {

                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    Column column = field.getAnnotation(Column.class);
                    String columnName = field.getName();
                    if (column != null && !column.name().isEmpty()) {
                        columnName = column.name();
                    }

                    Map<String, Object> recordValues = record.getValues();

                    String col = null;

                    if (recordValues.containsKey(columnName)) {
                        col = columnName;
                    } else if (recordValues.containsKey("_" + columnName)) {
                        col = "_" + columnName;
                    }

                    if (col != null) {
                        Object value = record.getValueByKey(col);
                        setFieldValue(obj, field, value);
                    }

                    // 如果没有找到列
                    if (Objects.isNull(col)) {
                        String fieldName = (String) recordValues.get("_field");
                        if (Objects.equals(fieldName, columnName)) {
                            Object value = recordValues.get("_value");
                            setFieldValue(obj, field, value);
                        }
                    }
                }

                clazz = clazz.getSuperclass();
            }
            return obj;
        } catch (Exception e) {
            throw new InfluxException(e);
        }
    }


    private static void setFieldValue(@Nonnull final Object object,
                                     @Nullable final Field field,
                                     @Nullable final Object value) {

        if (field == null || value == null) {
            return;
        }
        String msg =
                "Class '%s' field '%s' was defined with a different field type and caused a ClassCastException. "
                        + "The correct type is '%s' (current field value: '%s').";

        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Class<?> fieldType = field.getType();

            //the same type
            if (fieldType.equals(value.getClass())) {
                field.set(object, value);
                return;
            }

            //convert primitives
            if (double.class.isAssignableFrom(fieldType)) {
                field.setDouble(object, toDoubleValue(value));
                return;
            }
            if (long.class.isAssignableFrom(fieldType)) {
                field.setLong(object, toLongValue(value));
                return;
            }
            if (int.class.isAssignableFrom(fieldType)) {
                field.setInt(object, toIntValue(value));
                return;
            }
            if (boolean.class.isAssignableFrom(fieldType)) {
                field.setBoolean(object, Boolean.valueOf(String.valueOf(value)));
                return;
            }
            if (BigDecimal.class.isAssignableFrom(fieldType)) {
                field.set(object, toBigDecimalValue(value));
                return;
            }

            if (Byte.class.isAssignableFrom(fieldType)) {
                field.set(object, Byte.valueOf(String.valueOf(value)));
                return;
            }

            //enum
            if (fieldType.isEnum()) {
                //noinspection unchecked, rawtypes
                field.set(object, Enum.valueOf((Class<Enum>) fieldType, String.valueOf(value)));
                return;
            }
            field.set(object, value);

        } catch (ClassCastException | IllegalAccessException e) {

            throw new InfluxException(String.format(msg, object.getClass().getName(), field.getName(),
                    value.getClass().getName(), value));
        }
    }

    private static double toDoubleValue(final Object value) {

        if (double.class.isAssignableFrom(value.getClass()) || Double.class.isAssignableFrom(value.getClass())) {
            return (double) value;
        }

        return (Double) value;
    }

    private static long toLongValue(final Object value) {

        if (long.class.isAssignableFrom(value.getClass()) || Long.class.isAssignableFrom(value.getClass())) {
            return (long) value;
        }

        return ((Double) value).longValue();
    }

    private static int toIntValue(final Object value) {

        if (int.class.isAssignableFrom(value.getClass()) || Integer.class.isAssignableFrom(value.getClass())) {
            return (int) value;
        }

        return ((Double) value).intValue();
    }

    private static BigDecimal toBigDecimalValue(final Object value) {
        if (String.class.isAssignableFrom(value.getClass())) {
            return new BigDecimal((String) value);
        }

        if (double.class.isAssignableFrom(value.getClass()) || Double.class.isAssignableFrom(value.getClass())) {
            return BigDecimal.valueOf((double) value);
        }

        if (int.class.isAssignableFrom(value.getClass()) || Integer.class.isAssignableFrom(value.getClass())) {
            return BigDecimal.valueOf((int) value);
        }

        if (long.class.isAssignableFrom(value.getClass()) || Long.class.isAssignableFrom(value.getClass())) {
            return BigDecimal.valueOf((long) value);
        }

        String message = String.format("Cannot cast %s [%s] to %s.",
                value.getClass().getName(), value, BigDecimal.class);

        throw new ClassCastException(message);
    }
}
