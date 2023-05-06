package com.cutefool.commons.orm.rds.meta;


import com.cutefool.commons.orm.rds.libs.TableLibs;
import com.zaxxer.hikari.HikariDataSource;
import com.cutefool.commons.core.BizException;
import com.cutefool.commons.core.util.Lists;
import com.cutefool.commons.core.util.Maps;
import com.cutefool.commons.core.util.Strings;
import com.cutefool.commons.orm.ColumnLibs;
import com.cutefool.commons.orm.rds.enums.RdsDbType;
import com.cutefool.commons.orm.rds.enums.RdsJdbcType;
import com.cutefool.commons.orm.utils.Encrypts;
import com.cutefool.commons.orm.utils.Remarks;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public interface RdsMetaOperations {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RdsMetaOperations.class);

    static RdsMetaOperations rdsMetaOperations(HikariDataSource dataSource) {
        return RdsDbType.byDriver(dataSource.getDriverClassName()).getFunction().apply(dataSource);
    }

    Connection connection();

    DataSource dataSource();

    default String[] types() {
        return new String[]{"TABLE", "VIEW"};
    }


    default boolean test() {
        Connection connection = connection();
        try {
            return connection.getMetaData().getDatabaseProductName().length() > 0;
        } catch (Exception e) {
            log.error("[test] --> "
                    + "the exception message is:" + e.getMessage());
            return false;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource());
        }
    }

    String dbName();


    default boolean isTable(String name) {
        return Objects.equals(name, "TABLE");
    }

    /**
     * 查询所有表
     *
     * @return tables
     */
    default List<String> doDatabases(ResultSet rs) throws SQLException {
        List<String> databases = Lists.newArrayList();
        ResultSetMetaData metaData = rs.getMetaData();
        while (rs.next()) {
            String tableType = rs.getString(this.dbName());
            databases.add(tableType);
        }
        return databases;
    }


    default List<String> loadDatabases(DatabaseMetaData databaseMetaData) {
        try (ResultSet rs = databaseMetaData.getCatalogs()) {
            return this.doDatabases(rs);
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    /**
     * 查询数据库
     *
     * @return 数据库
     */
    default List<String> databases() {
        Connection connection = this.connection();
        try {
            return this.loadDatabases(connection.getMetaData());
        } catch (Exception e) {
            throw new BizException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource());
        }
    }


    /**
     * 查询所有表
     *
     * @return tables
     */
    default List<TableLibs> doTables(ResultSet rs) throws SQLException {
        List<TableLibs> tableLibs = Lists.newArrayList();
        ResultSetMetaData metaData = rs.getMetaData();
        while (rs.next()) {
            TableLibs e = new TableLibs();
            String tableType = rs.getString("TABLE_TYPE");
            e.setName(rs.getString("TABLE_NAME"));
            e.setTable(this.isTable(tableType));
            e.setDatabase(rs.getString(this.dbName()));
            e.setRemarks(Remarks.remarks(rs.getString("REMARKS")));
            tableLibs.add(e);
        }
        return tableLibs;
    }

    default List<TableLibs> loadTables(DatabaseMetaData databaseMetaData, String schema) {
        try (ResultSet rs = databaseMetaData.getTables(schema, schema, "%", this.types())) {
            return this.doTables(rs);
        } catch (Exception e) {
            throw new BizException(e);
        }
    }


    /**
     * 查询所有表
     *
     * @return tables
     */
    default List<ColumnLibs> doColumns(ResultSet rs) throws SQLException {
        List<ColumnLibs> columnLibs = Lists.newArrayList();
        List<TableLibs> tableLibs = this.tables();
        Map<String, Map<String, Boolean>> tablesMap = Lists.empty(tableLibs).stream().collect(Collectors.groupingBy(TableLibs::getDatabase, Collectors.toMap(TableLibs::getName, TableLibs::isTable)));
        while (rs.next()) {
            ColumnLibs e = new ColumnLibs();
            e.setTableName(rs.getString("TABLE_NAME").toLowerCase());
            e.setName(rs.getString("COLUMN_NAME").toLowerCase());
            e.setComment(Remarks.remarks(rs.getString("REMARKS")));
            e.setType(rs.getInt("DATA_TYPE"));
            e.setTypeName(RdsJdbcType.byType(e.getType()).getName());
            e.setNullable(rs.getBoolean("IS_NULLABLE"));
            e.setJavaName(Strings.camel(e.getName()));
            e.setDatabase(rs.getString(this.dbName()).toLowerCase());
            Boolean orDefault = tablesMap.getOrDefault(e.getDatabase(), Maps.hashMap()).getOrDefault(e.getTableName(), false);
            e.setTable(orDefault);
            columnLibs.add(e);
        }
        return columnLibs;
    }

    default List<ColumnLibs> loadColumns(DatabaseMetaData databaseMetaData, String schema, String tableName, String columnName) {
        try (ResultSet rs = databaseMetaData.getColumns(schema, schema, tableName, columnName)) {
            return this.doColumns(rs);
        } catch (Exception e) {
            throw new BizException(e);
        }
    }


    /**
     * 查询所有表
     *
     * @return tables
     */
    default List<TableLibs> tables() {
        Connection connection = this.connection();
        try {
            return this.loadTables(connection.getMetaData(), null);
        } catch (Exception e) {
            throw new BizException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource());
        }
    }

    /**
     * 查询所有表（包含某字段）
     *
     * @param column 字段
     * @return tables
     */
    default List<TableLibs> tablesWithColumn(String column) {
        List<ColumnLibs> columnLibs = this.columnsWithColumn(column);
        Map<String, Map<String, Boolean>> tablesMap = Lists.empty(columnLibs).stream().collect(Collectors.groupingBy(ColumnLibs::getDatabase, Collectors.toMap(ColumnLibs::getTableName, e -> true)));
        List<TableLibs> tableLibs = this.tables();
        return Lists.empty(tableLibs)
                .stream()
                .filter(e -> tablesMap.getOrDefault(e.getDatabase(), Maps.hashMap()).containsKey(e.getName()))
                .collect(Collectors.toList());
    }

    /**
     * 查询所有表（指定数据库）
     *
     * @param schema 数据库
     * @return tables
     */
    default List<TableLibs> tablesBySchema(String schema) {
        Connection connection = this.connection();
        try {
            return this.loadTables(connection.getMetaData(), schema);
        } catch (Exception e) {
            throw new BizException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource());
        }
    }

    /**
     * 查询所有的列
     *
     * @return columns
     */

    default List<ColumnLibs> columns() {
        Connection connection = this.connection();
        try {
            return this.loadColumns(connection.getMetaData(), null, "%", "%");
        } catch (Exception e) {
            throw new BizException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource());
        }
    }


    /**
     * 查询所有的列（指定的列）
     *
     * @return columns
     */
    default List<ColumnLibs> columnsWithColumn(String column) {
        Connection connection = this.connection();
        try {
            return this.loadColumns(connection.getMetaData(), null, "%", column);
        } catch (Exception e) {
            throw new BizException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource());
        }
    }

    /**
     * 查询所有的列（指定数据库）
     *
     * @return columns
     */

    default List<ColumnLibs> columnsWithSchema(String schema) {
        Connection connection = this.connection();
        try {
            return this.loadColumns(connection.getMetaData(), schema, "%", "%");
        } catch (Exception e) {
            throw new BizException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource());
        }
    }

    /**
     * 查询所有的列（指定数据库）
     *
     * @param schema schema
     * @param table  table
     * @return columns
     */
    default List<ColumnLibs> columnBy(String schema, String table) {
        Connection connection = this.connection();
        try {
            return this.loadColumns(connection.getMetaData(), schema, table, "%");
        } catch (Exception e) {
            throw new BizException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource());
        }
    }


    /**
     * 查询所有的列（分组）
     *
     * @return columns
     */
    default Map<String, Map<String, List<ColumnLibs>>> groupBy() {
        List<ColumnLibs> columns = this.columns();
        return Lists.empty(columns).stream().collect(Collectors.groupingBy(ColumnLibs::getDatabase, Collectors.groupingBy(ColumnLibs::getTableName)));
    }


    /**
     * 分组（数据库实例，数据表，字段）
     *
     * @return 分组
     */
    default Map<String, List<ColumnLibs>> groupBySchema(String schema) {
        List<ColumnLibs> columns = this.columnsWithSchema(schema);
        return Lists.empty(columns).stream().collect(Collectors.groupingBy(ColumnLibs::getTableName));
    }

    ;

    /**
     * 分组（数据库实例，数据表，字段）
     *
     * @param column 字段
     * @return 分组
     */
    default Map<String, Map<String, List<ColumnLibs>>> groupByColumn(String column) {
        List<ColumnLibs> columns = this.columnsWithColumn(column);
        return Lists.empty(columns).stream().collect(Collectors.groupingBy(ColumnLibs::getDatabase, Collectors.groupingBy(ColumnLibs::getTableName)));
    }


    /**
     * 加密分组（数据库实例，数据表，字段）
     *
     * @return 分组
     */
    default Map<String, Map<String, List<ColumnLibs>>> encryptGroupBy() {
        return Lists.empty(columns()).stream()
                .collect(Collectors.groupingBy(e -> Encrypts.encode(e.getDatabase()),
                        Collectors.groupingBy(e -> Encrypts.encode(e.getTableName()), Collectors.mapping(e -> {
                            e.setDatabase(Encrypts.encode(e.getDatabase()));
                            e.setTableName(Encrypts.encode(e.getDatabase()));
                            return e;
                        }, Collectors.toList()))));
    }

    ;

    /**
     * 加密分组（数据库实例，数据表，字段）
     *
     * @param column 字段
     * @return 分组
     */
    default Map<String, Map<String, List<ColumnLibs>>> encryptGroupBy(String column) {
        return Lists.empty(columnsWithColumn(column))
                .stream().collect(Collectors.groupingBy(e -> Encrypts.encode(e.getDatabase()),
                        Collectors.groupingBy(e -> Encrypts.encode(e.getTableName()), Collectors.mapping(e -> {
                            e.setDatabase(Encrypts.encode(e.getDatabase()));
                            e.setTableName(Encrypts.encode(e.getDatabase()));
                            return e;
                        }, Collectors.toList()))));
    }

    ;

}
