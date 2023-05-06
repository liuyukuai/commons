package com.cutefool.commons.orm.mybatis.extensible.autoconfig;

import com.cutefool.commons.orm.rds.autoconfigure.RdsMetaAutoConfiguration;
import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.cutefool.commons.orm.mybatis.extensible.interceptor.MybatisExtensibleInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean({SqlSessionFactory.class})
@AutoConfigureAfter({MybatisAutoConfiguration.class, RdsMetaAutoConfiguration.class})
@AutoConfigureBefore(PageHelperAutoConfiguration.class)
@Lazy(false)
public class MybatisExtensibleAutoConfiguration implements InitializingBean {

    private final List<SqlSessionFactory> sqlSessionFactoryList;

    public MybatisExtensibleAutoConfiguration(List<SqlSessionFactory> sqlSessionFactoryList) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
    }

    @Override
    public void afterPropertiesSet() {
        MybatisExtensibleInterceptor interceptor = new MybatisExtensibleInterceptor();
        for (SqlSessionFactory sqlSessionFactory : this.sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            if (!this.containsInterceptor(configuration, interceptor)) {
                configuration.addInterceptor(interceptor);
            }
        }
    }


    private boolean containsInterceptor(org.apache.ibatis.session.Configuration configuration, Interceptor interceptor) {
        try {
            return configuration.getInterceptors().stream().anyMatch((config) -> interceptor.getClass().isAssignableFrom(config.getClass()));
        } catch (Exception e) {
            return false;
        }
    }

}
