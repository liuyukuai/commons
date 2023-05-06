package com.cutefool.commons.orm.rds.functions;

import com.cutefool.commons.orm.rds.enums.RdsDbType;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;

public class FunctionInitializer implements InitializingBean, Ordered {

    private final HikariDataSource hikariDataSource;


    public FunctionInitializer(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    @Override
    public void afterPropertiesSet() {
        String driverClassName = this.hikariDataSource.getDriverClassName();
        RdsDbType rdsDbType = RdsDbType.byDriver(driverClassName);
        Functioned functioned = rdsDbType.getFunctioned();
        functioned.doExecute(hikariDataSource);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
