package com.cutefool.commons.orm.rds.enums;

import com.cutefool.commons.orm.rds.meta.RdsMetaMysqlOperations;
import com.cutefool.commons.orm.rds.meta.RdsMetaOperations;
import com.cutefool.commons.orm.rds.meta.RdsMetaPgOperations;
import com.cutefool.commons.orm.rds.meta.RdsMeteDmOperations;
import com.zaxxer.hikari.HikariDataSource;
import dm.jdbc.driver.DaMDriver;
import lombok.Data;
import lombok.Getter;
import com.cutefool.commons.orm.rds.functions.DMFunction;
import com.cutefool.commons.orm.rds.functions.Functioned;
import com.cutefool.commons.orm.rds.functions.KBFunction;
import com.cutefool.commons.orm.rds.functions.MysqlFunction;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据库类型
 */
@SuppressWarnings("unused")
public enum RdsDbType {
    /**
     * MYSQL
     */
    MYSQL(
            "com.mysql.cj.jdbc.Driver",
            "jdbc:mysql://{host}:{port}/{database}",
            RdsMetaMysqlOperations::new,
            new MysqlFunction()
    ),

    /**
     * ORACLE
     */
    ORACLE("oracle.jdbc.OracleDriver",
            "jdbc:oracle:thin:@//{host}:{port}/{database}",
            RdsMetaMysqlOperations::new,
            new MysqlFunction()
    ),

    /**
     * SQLSERVER
     */
    SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "jdbc:sqlserver://{host}:{port};DatabaseName={database}",
            (s) -> new RdsMetaMysqlOperations(s),
            new MysqlFunction()
    ),

    /**
     * CLICKHOUSE
     */
    CLICKHOUSE("ru.yandex.clickhouse.ClickHouseDriver",
            "jdbc:clickhouse://{host}:{port}/{database}",
            RdsMetaMysqlOperations::new,
            new MysqlFunction()),

    /**
     * HIVE
     */

    HIVE("org.apache.hive.jdbc.HiveDriver",
            "jdbc:hive2://{host}:{port}/{database}",
            RdsMetaMysqlOperations::new,
            new MysqlFunction()
    ),

    /**
     * PG
     */
    POSTGRESQL("org.postgresql.Driver",
            "jdbc:postgresql://{host}:{port}/{database}",
            RdsMetaPgOperations::new,
            new MysqlFunction()
    ),

    /**
     * 达梦
     */
    DM(DaMDriver.class.getName(),
            "jdbc:dm://{host}:{port}/{database}",
            RdsMeteDmOperations::new,
            new DMFunction()
    ),

    /**
     * 人大金仓
     */
    KB("com.kingbase8.Driver",
            "jdbc:kingbase8://{host}:{port}/{database}",
            RdsMetaMysqlOperations::new,
            new KBFunction()
    );


    @Getter
    private final String driver;

    @Getter
    private final String jdbc;

    @Getter
    private final Function<HikariDataSource, RdsMetaOperations> function;


    @Getter
    private final Functioned functioned;


    RdsDbType(String driver, String jdbc, Function<HikariDataSource, RdsMetaOperations> function, Functioned functioned) {
        this.driver = driver;
        this.jdbc = jdbc;
        this.function = function;
        this.functioned = functioned;
    }


    @Data
    public static class RdsDb {

        private String name;

        private String driver;

        private String jdbc;

    }

    public static RdsDbType byDriver(String driver) {
        return Stream.of(RdsDbType.values()).filter(e -> Objects.equals(driver, e.getDriver())).findAny().orElseThrow(() -> new RuntimeException("not support database type = " + driver));
    }

    public static List<RdsDb> selects() {
        return Stream.of(RdsDbType.values())
                .map(e -> {
                    RdsDb rdsDb = new RdsDb();
                    rdsDb.setJdbc(e.getJdbc());
                    rdsDb.setName(e.name());
                    rdsDb.setDriver(e.getDriver());
                    return rdsDb;
                }).collect(Collectors.toList());
    }

}
