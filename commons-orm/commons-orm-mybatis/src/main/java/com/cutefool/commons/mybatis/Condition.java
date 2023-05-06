/*
 *  
 */
package com.cutefool.commons.mybatis;

import lombok.Data;
import com.cutefool.commons.core.Operator;
import com.cutefool.commons.core.util.Strings;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 271007729@qq.com
 * @date 2021/9/7 12:20 AM
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class Condition {

    private String sql;

    public static Condition eq(MysqlColumn column, Object value) {
        return eq(column.getEscapedColumnName(), value);
    }

    public static Condition and(Condition... conditions) {
        String collect = Stream.of(conditions)
                               .map(Condition::getSql)
                               .collect(Collectors.joining(" " + Operator.AND.name() + " "));
        return init(Strings.LEFT_PARENTHESES + collect + Strings.RIGHT_PARENTHESES);
    }

    public static Condition or(Condition... conditions) {
        String collect = Stream.of(conditions)
                               .map(Condition::getSql)
                               .collect(Collectors.joining(" " + Operator.OR.name() + " "));
        return init(Strings.LEFT_PARENTHESES + collect + Strings.RIGHT_PARENTHESES);
    }

    public static Condition eq(String column, Object value) {
        String sql = column + " =" + Strings.sql(value) + "";
        return init(sql);
    }

    public static Condition isNull(MysqlColumn column) {
        return isNull(column.getEscapedColumnName());
    }

    public static Condition isNull(String column) {
        String sql = column + " is null ";
        return init(sql);
    }

    public static Condition isNotNull(MysqlColumn column) {
        return isNotNull(column.getEscapedColumnName());
    }

    public static Condition isNotNull(String column) {
        String sql = column + " is not null ";
        return init(sql);
    }

    public static Condition notEq(MysqlColumn column, Object value) {
        return notEq(column.getEscapedColumnName(), value);
    }

    public static Condition notEq(String column, Object value) {
        String sql = column + " <>" + Strings.sql(value) + "";
        return init(sql);
    }

    public static Condition between(MysqlColumn column, Object value1, Object value2) {
        return between(column.getEscapedColumnName(), value1, value2);
    }

    public static Condition between(String column, Object value1, Object value2) {
        String sql = column + " between " + Strings.sql(value1) + " and " + Strings.sql(value2);
        return init(sql);
    }

    public static Condition notIn(MysqlColumn column, Object value) {
        return notIn(column.getEscapedColumnName(), value);
    }

    @SuppressWarnings("unchecked")
    public static Condition notIn(String column, Object value) {
        if (value instanceof Collection) {
            value = "(" + Strings.sql((Collection) value) + ")";
        } else {
            value = "(" + Strings.sql(value) + ")";
        }
        String sql = column + " not in " + value + "";
        return init(sql);
    }

    public static Condition in(MysqlColumn column, Object value) {
        return in(column.getEscapedColumnName(), value);
    }

    @SuppressWarnings("unchecked")
    public static Condition in(String column, Object value) {
        if (value instanceof Collection) {
            value = "(" + Strings.sql((Collection) value) + ")";
        } else {
            String s = value.toString();
            List<String> values = Strings.split(s);
            value = "(" + Strings.sql(values) + ")";
        }
        String sql = column + " in " + value + "";
        return init(sql);
    }

    public static Condition find(MysqlColumn column, Object value) {
        return find(column.getEscapedColumnName(), value);
    }

    public static Condition like(MysqlColumn column, String value) {
        return like(column.getEscapedColumnName(), value);
    }

    public static Condition like(String column, String value) {
        String sql = column + " like '%" + value + "%' ";
        return init(sql);
    }

    public static Condition notLike(MysqlColumn column, String value) {
        return notLike(column.getEscapedColumnName(), value);
    }

    public static Condition notLike(String column, String value) {
        String sql = column + " not like '%" + value + "%' ";
        return init(sql);
    }

    public static Condition leftLike(MysqlColumn column, String value) {
        return leftLike(column.getEscapedColumnName(), value);
    }

    public static Condition leftLike(String column, String value) {
        String sql = column + " like '" + value + "%' ";
        return init(sql);
    }

    public static Condition rightLike(MysqlColumn column, String value) {
        return rightLike(column.getEscapedColumnName(), value);
    }

    public static Condition rightLike(String column, String value) {
        String sql = column + " like '%" + value + "' ";
        return init(sql);
    }

    public static Condition find(String column, Object value) {
        if (value instanceof Collection) {
            Collection list = (Collection) value;
            List<String> sqls = new ArrayList<>();
            for (Object o : list) {
                sqls.add("FIND_IN_SET(" + Strings.sql(o) + ", " + column + ")");
            }
            return init(StringUtils.join(sqls, " or "));
        }
        return init("FIND_IN_SET(" + Strings.sql(value) + ", " + column + ")");
    }

    private static Condition init(String sql) {
        Condition condition = new Condition();
        condition.setSql(sql);
        return condition;
    }
}
