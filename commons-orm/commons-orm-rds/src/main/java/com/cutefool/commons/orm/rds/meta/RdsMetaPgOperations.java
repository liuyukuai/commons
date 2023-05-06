package com.cutefool.commons.orm.rds.meta;


import com.zaxxer.hikari.HikariDataSource;
import com.cutefool.commons.core.util.Lists;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * MYSQL数据支持
 */
public class RdsMetaPgOperations implements RdsMetaOperations {

    private final HikariDataSource dataSource;

    public RdsMetaPgOperations(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<String> doDatabases(ResultSet rs) throws SQLException {
        List<String> databases = Lists.newArrayList();
        ResultSetMetaData metaData = rs.getMetaData();
        while (rs.next()) {
            String tableType = rs.getString("TABLE_CAT");
            databases.add(tableType);
        }
        return databases;
    }

    @Override
    public Connection connection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public String dbName() {
        return "TABLE_SCHEM";
    }
}
