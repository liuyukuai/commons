package com.cutefool.commons.orm.rds;

import com.zaxxer.hikari.HikariDataSource;
import dm.jdbc.driver.DaMDriver;
import dm.jdbc.driver.DmDriver;
import com.cutefool.commons.core.util.JsonUtils;
import com.cutefool.commons.orm.rds.enums.RdsDbType;
import com.cutefool.commons.orm.rds.meta.RdsMetaOperations;

import java.sql.SQLException;

/**
 * 关系型数据库元数据获取类
 *
 * @author 271007729@qq.com
 */
@SuppressWarnings("unused")
public final class RdsMetaFactory {

    public static RdsMetaOperations operations(RdsDbType rdsDbType, HikariDataSource dataSource) {
        return rdsDbType.getFunction().apply(dataSource);
    }

    public static RdsMetaOperations mysql(HikariDataSource dataSource) {
        return RdsDbType.MYSQL.getFunction().apply(dataSource);
    }

    public static void main(String[] args) throws SQLException {
//        -Dspring.datasource.password=1qaz@WSX
//        -Dspring.datasource.username=root
//                -Dspring.datasource.db=e_auth
//                -Dspring.datasource.host=127.0.0.1
//        HikariDataSource mysqlSource = new HikariDataSource();
//        mysqlSource.setPassword("1qaz@WSX");
//        mysqlSource.setUsername("root");
//        mysqlSource.setDriverClassName(RdsDbType.MYSQL.getDriver());
//        mysqlSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/e_auth?useUnicode=true&characterEncoding=UTF-8&useInformationSchema=true");
//        mysqlSource.setMaximumPoolSize(1);
//        mysqlSource.setMinimumIdle(1);
//        mysqlSource.setConnectionTimeout(30000);
//
//        RdsMetaOperations mysqlRdsMetaOperations = RdsMetaOperations.rdsMetaOperations(mysqlSource);
//        System.out.println(JsonUtils.toJson(mysqlRdsMetaOperations.tables()));
//        System.out.println(JsonUtils.toJson(mysqlRdsMetaOperations.tablesBySchema("dataworks")));
//        System.out.println(JsonUtils.toJson(mysqlRdsMetaOperations.databases()));

//        Class<DaMDriver> dmDriverClass = DaMDriver.class;
//        HikariDataSource dmSource = new HikariDataSource();
//        dmSource.setPassword("Tsingyun@2022");
//        dmSource.setUsername("TSINGYUN");
//        String name = DmDriver.class.getName();
//        System.out.println(name);
//        dmSource.setDriverClassName(name);
//        dmSource.setJdbcUrl("jdbc:dm://172.24.0.98:5236/e_auth?useUnicode=true&characterEncoding=UTF-8&useInformationSchema=true");
//        dmSource.setMaximumPoolSize(1);
//        dmSource.setMinimumIdle(1);
//        dmSource.setConnectionTimeout(30000);
//
//        RdsMetaOperations dmRdsMetaOperations = RdsMetaOperations.rdsMetaOperations(dmSource);
//        System.out.println(JsonUtils.toJson(dmRdsMetaOperations.tables()));
////        System.out.println(JsonUtils.toJson(dmRdsMetaOperations.tablesBySchema("dataworks")));
//        System.out.println(JsonUtils.toJson(dmRdsMetaOperations.databases()));


        Class<DaMDriver> dmDriverClass = DaMDriver.class;
        HikariDataSource dmSource = new HikariDataSource();
        dmSource.setPassword("tsingyun2020");
        dmSource.setUsername("postgres");
        String name = DmDriver.class.getName();
        System.out.println(name);
        dmSource.setDriverClassName(RdsDbType.POSTGRESQL.getDriver());
        dmSource.setJdbcUrl("jdbc:postgresql://172.24.0.63:5432/tsingyun_gis_dev?useUnicode=true&characterEncoding=UTF-8&useInformationSchema=true");
        dmSource.setMaximumPoolSize(1);
        dmSource.setMinimumIdle(1);
        dmSource.setConnectionTimeout(30000);

        RdsMetaOperations dmRdsMetaOperations = RdsMetaOperations.rdsMetaOperations(dmSource);
        System.out.println(JsonUtils.toJson(dmRdsMetaOperations.tables()));
//        System.out.println(JsonUtils.toJson(dmRdsMetaOperations.tablesBySchema("dataworks")));
        System.out.println(JsonUtils.toJson(dmRdsMetaOperations.databases()));


    }
}
