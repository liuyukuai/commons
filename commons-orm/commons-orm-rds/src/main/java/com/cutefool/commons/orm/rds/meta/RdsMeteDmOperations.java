package com.cutefool.commons.orm.rds.meta;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 达梦数据支持
 */
public class RdsMeteDmOperations implements RdsMetaOperations {

    private final HikariDataSource dataSource;

    public RdsMeteDmOperations(HikariDataSource dataSource) {
        this.dataSource = dataSource;
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
