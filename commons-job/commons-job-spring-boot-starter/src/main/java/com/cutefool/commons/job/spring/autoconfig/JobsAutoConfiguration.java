package com.cutefool.commons.job.spring.autoconfig;

import com.cutefool.commons.job.spring.config.JobsConfiguration;
import com.cutefool.commons.job.worker.JobSpringApplication;
import com.cutefool.commons.core.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = "com.cutefool.commons.job")
@EnableConfigurationProperties(JobsConfiguration.class)
public class JobsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(JobSpringApplication.class)
    public JobSpringApplication jobSpringApplication(JobsConfiguration jobsConfiguration, ConfigurableEnvironment configurableEnvironment) {
        String appId = configurableEnvironment.getProperty(Constants.SPRING_APPLICATION_NAME);
        return new JobSpringApplication(jobsConfiguration.getAddress(), appId);
    }

}
