package com.cutefool.commons.orm.rds.cache.autoconfigure;

import com.cutefool.commons.orm.rds.autoconfigure.RdsMetaAutoConfiguration;
import com.cutefool.commons.orm.rds.cache.RdsCache;
import com.cutefool.commons.orm.rds.cache.context.RdsContext;
import com.cutefool.commons.orm.rds.config.RdsConfiguration;
import com.cutefool.commons.orm.rds.config.RdsDatabaseConfiguration;
import com.cutefool.commons.orm.rds.meta.RdsMetaOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 关系数据库自动装配类
 */
@Slf4j
@AutoConfigureAfter({RdsMetaAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
public class RdsCacheAutoConfiguration {
    public RdsCacheAutoConfiguration() {
        log.info("RdsCacheAutoConfiguration init ");
    }


    @Bean
    public RdsCache rdsCache(RdsMetaOperations rdsMetaOperations) {
        return new RdsCache(rdsMetaOperations);
    }

    @Bean
    public RdsContext rdsContext(RdsCache rdsCache, RdsConfiguration rdsConfiguration, RdsDatabaseConfiguration rdsDatabaseConfiguration) {
        return new RdsContext(rdsCache, rdsConfiguration, rdsDatabaseConfiguration);
    }

}
