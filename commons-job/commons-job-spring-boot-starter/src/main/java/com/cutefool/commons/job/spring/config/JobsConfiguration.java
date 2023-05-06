package com.cutefool.commons.job.spring.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "commons.jobs")
public class JobsConfiguration {

    @Value("${commons.jobs.address:}")
    private String address;

}
