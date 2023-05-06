package com.cutefool.commons.web.autoconfigure;

import com.cutefool.commons.web.advice.ReqBodyAdvice;
import com.cutefool.commons.orm.rds.config.RdsConfiguration;
import com.cutefool.commons.web.advice.ResBodyAdvice;
import com.cutefool.commons.web.web.RdsController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 关系数据库自动装配类
 */
@Configuration
@ComponentScan
public class ResAdviceAutoConfiguration {

    @Bean
    public ResBodyAdvice respBodyAdvice(RdsConfiguration rdsConfiguration) {
        return new ResBodyAdvice(rdsConfiguration);
    }

    @Bean
    public ReqBodyAdvice reqBodyAdvice() {
        return new ReqBodyAdvice();
    }


    @Bean
    public RdsController rdsController() {
        return new RdsController();
    }


}
