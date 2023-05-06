package com.cutefool.commons.orm.rds.enums;

import lombok.Getter;
import com.cutefool.commons.core.Select;

import java.sql.JDBCType;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据库类型
 */
@SuppressWarnings("unused")
public enum RdsJdbcType {

    NONE(null, "未知", JDBCType.VARCHAR, RdsJdbcType::getVarcharSql),
    BIT(JDBCType.BIT, "数值(BIT)", JDBCType.BIGINT, (len, scale) -> getDefSql(JDBCType.BIGINT)),
    TINYINT(JDBCType.TINYINT, "数值（BIT）", JDBCType.BIGINT, (len, scale) -> getDefSql(JDBCType.BIGINT)),
    SMALLINT(JDBCType.SMALLINT, "数值（SMALLINT）", JDBCType.BIGINT, (len, scale) -> getDefSql(JDBCType.BIGINT)),
    INTEGER(JDBCType.INTEGER, "数值（INTEGER）", JDBCType.BIGINT, (len, scale) -> getDefSql(JDBCType.BIGINT)),
    BIGINT(JDBCType.BIGINT, "数值（BIGINT）", JDBCType.BIGINT, (len, scale) -> getDefSql(JDBCType.BIGINT)),
    FLOAT(JDBCType.FLOAT, "浮点数（FLOAT）", JDBCType.DECIMAL, (len, scale) -> getFloatSql(JDBCType.DECIMAL, len, scale)),
    REAL(JDBCType.REAL, "浮点数（REAL）", JDBCType.DECIMAL, (len, scale) -> getFloatSql(JDBCType.DECIMAL, len, scale)),
    DOUBLE(JDBCType.DOUBLE, "浮点数（DOUBLE）", JDBCType.DECIMAL, (len, scale) -> getFloatSql(JDBCType.DECIMAL, len, scale)),
    NUMERIC(JDBCType.NUMERIC, "浮点数（NUMERIC）", JDBCType.DECIMAL, (len, scale) -> getFloatSql(JDBCType.DECIMAL, len, scale)),
    DECIMAL(JDBCType.DECIMAL, "浮点数（DECIMAL）", JDBCType.DECIMAL, (len, scale) -> getFloatSql(JDBCType.DECIMAL, len, scale)),
    CHAR(JDBCType.CHAR, "字符串（CHAR）", JDBCType.VARCHAR, RdsJdbcType::getVarcharSql),
    VARCHAR(JDBCType.VARCHAR, "字符串（VARCHAR）", JDBCType.VARCHAR, RdsJdbcType::getVarcharSql),
    LONG_VARCHAR(JDBCType.LONGVARCHAR, "字符串（LONGVARCHAR）", JDBCType.VARCHAR, RdsJdbcType::getVarcharSql),
    DATE(JDBCType.DATE, "日期（DATE）", JDBCType.DATE, (len, scale) -> getDateSql()),
    TIME(JDBCType.TIME, "日期（TIME）", JDBCType.DATE, (len, scale) -> getDateSql()),
    TIMESTAMP(JDBCType.TIMESTAMP, "日期（TIMESTAMP）", JDBCType.DATE, (len, scale) -> getDateSql()),
    BINARY(JDBCType.BINARY, "二进制（BINARY）", JDBCType.CLOB, (len, scale) -> getDefSql(JDBCType.CLOB)),
    VARBINARY(JDBCType.VARBINARY, "字符串（VARBINARY）", JDBCType.CLOB, (len, scale) -> getDefSql(JDBCType.CLOB)),
    LONG_VARBINARY(JDBCType.LONGVARBINARY, "二进制（LONGVARBINARY）", JDBCType.CLOB, (len, scale) -> getDefSql(JDBCType.CLOB)),
    CLOB(JDBCType.LONGVARBINARY, "二进制（LONGVARBINARY）", JDBCType.BLOB, (len, scale) -> getDefSql(JDBCType.BLOB));

    /**
     * jdbc类型
     */
    @Getter
    private final JDBCType jdbcType;

    /**
     * 名称
     */
    @Getter
    private final String name;

    /**
     * 页面增加字段类型
     */
    @Getter
    private final JDBCType defType;


    private final BiFunction<Integer, Integer, String> sqlDef;

    RdsJdbcType(JDBCType jdbcType, String name, JDBCType defType, BiFunction<Integer, Integer, String> sqlDef) {
        this.jdbcType = jdbcType;
        this.name = name;
        this.defType = defType;
        this.sqlDef = sqlDef;
    }

    private static String getVarcharSql(Integer len, Integer scale) {
        len = Objects.isNull(len) || len <= 0 ? 500 : len;
        len = Math.min(len, 5000);
        return JDBCType.VARCHAR.name() + "(" + len + ") NULL";
    }

    private static String getDefSql(JDBCType jdbcType) {
        return jdbcType.name() + " NULL";
    }

    private static String getDateSql() {
        return " datetime NULL";
    }

    private static String getFloatSql(JDBCType jdbcType, Integer len, Integer scale) {
        len = Objects.isNull(len) || len <= 0 ? 10 : len;
        len = Math.min(len, 10);
        scale = Objects.isNull(scale) || scale <= 0 ? 2 : scale;
        scale = Math.min(scale, 10);
        return jdbcType.name() + "(" + len + "," + scale + ") NULL";
    }


    public static RdsJdbcType byType(int type) {
        return Stream.of(RdsJdbcType.values())
                .filter(e -> Objects.equals(e.getJdbcType(), JDBCType.valueOf(type))).findAny().orElse(RdsJdbcType.NONE);
    }

    public static String sqlByType(int type, Integer len, Integer scale) {
        return Stream.of(RdsJdbcType.values())
                .filter(e -> Objects.equals(e.getJdbcType(), JDBCType.valueOf(type)))
                .findAny()
                .orElse(RdsJdbcType.NONE)
                .sqlDef
                .apply(len, scale);
    }

    public static RdsJdbcType byName(String name) {
        return Stream.of(RdsJdbcType.values())
                .filter(e -> Objects.equals(e.getName(), name)).findAny().orElse(RdsJdbcType.NONE);
    }

    public static List<Select<Integer>> selects() {
        return Stream.of(RdsJdbcType.values()).map(e -> new Select<>(e.getName(), e.getDefType().getVendorTypeNumber())).collect(Collectors.toList());
    }


}
