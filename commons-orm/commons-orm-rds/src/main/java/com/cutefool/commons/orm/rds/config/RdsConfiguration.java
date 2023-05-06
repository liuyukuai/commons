package com.cutefool.commons.orm.rds.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class RdsConfiguration {

    private String db;
}
