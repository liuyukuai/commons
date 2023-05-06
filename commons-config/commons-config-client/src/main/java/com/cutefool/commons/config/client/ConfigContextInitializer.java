/*
 *
 */
package com.cutefool.commons.config.client;

import com.cutefool.commons.config.apollo.constant.ApolloConstants;
import com.cutefool.commons.config.core.enmus.Env;
import com.cutefool.commons.config.core.Constants;
import com.cutefool.commons.config.core.spi.Initializer;
import com.cutefool.commons.config.core.spi.SpiLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.List;

/**
 * 配置加载类
 *
 * @author 271007729@qq.com
 * @date 2022/7/25 12:44 PM
 */
public class ConfigContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, EnvironmentPostProcessor, Ordered {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        this.initialize(environment);

    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        this.initialize(environment);
    }

    protected void initialize(ConfigurableEnvironment environment) {
        // 默认使用apollo
        String type = environment.getProperty(Constants.COMMONS_CONFIG_TYPE, ApolloConstants.APOLLO_NAME);
        System.setProperty(Constants.COMMONS_CONFIG_TYPE, type);

        // 命名空间
        String namespaces = environment.getProperty(Constants.CONFIG_NAMESPACE_KEY, "");
        System.setProperty(Constants.CONFIG_NAMESPACE_KEY, namespaces);

        // 清理其他配置中心的初始化配置
        List<Initializer> initializers = SpiLoader.loadExclude(type, Initializer.class);
        initializers.forEach(s -> s.clean(environment));

        Initializer initializer = SpiLoader.load(type, Initializer.class);
        // 获取env配置
        String env = environment.getProperty(Constants.CONFIG_ENV_KEY, Env.UAT.name());
        // 获取meta信息
        String metaAddress = MetaAddressProvider.getMetaAddress(type, Env.valueOf(env.toUpperCase()));
        System.setProperty(Constants.CONFIG_META_KEY, metaAddress);

        // 初始化配置
        initializer.initialize(environment);

        // 初始化默认必须的配置项
        ConfigDefaultInitializer.initialize(environment);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
