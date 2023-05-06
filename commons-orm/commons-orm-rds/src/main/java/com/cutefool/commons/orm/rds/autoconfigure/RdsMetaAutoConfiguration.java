package com.cutefool.commons.orm.rds.autoconfigure;

import com.cutefool.commons.orm.rds.config.RdsConfiguration;
import com.cutefool.commons.orm.rds.config.RdsDatabaseConfiguration;
import com.cutefool.commons.orm.rds.functions.FunctionInitializer;
import com.cutefool.commons.orm.rds.meta.RdsMetaOperations;
import com.cutefool.commons.orm.rds.template.RdsTemplate;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 关系数据库自动装配类
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({RdsConfiguration.class, RdsDatabaseConfiguration.class})
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@AutoConfigureBefore({FlywayAutoConfiguration.class, FlywayAutoConfiguration.FlywayConfiguration.class})
public class RdsMetaAutoConfiguration {
    public RdsMetaAutoConfiguration() {
        log.info("RdsMetaAutoConfiguration init ");
    }

    @Bean
    public RdsMetaOperations rdsMetaOperations(HikariDataSource dataSource) {
        return RdsMetaOperations.rdsMetaOperations(dataSource);
    }

    @Bean
    public RdsTemplate rdsTemplate(JdbcTemplate jdbcTemplate) {
        return new RdsTemplate(jdbcTemplate);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public FunctionInitializer functionInitializer(HikariDataSource hikariDataSource) {
        log.info("functionInitializer init .");
        return new FunctionInitializer(hikariDataSource);
    }

    @Bean
    @Primary
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @DependsOn("functionInitializer")
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway,
                                                        ObjectProvider<FlywayMigrationStrategy> migrationStrategy) {
        return new FlywayMigrationInitializer(flyway, migrationStrategy.getIfAvailable());
    }
}
