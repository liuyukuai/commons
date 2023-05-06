/*
 *  
 */
package com.cutefool.commons.feign;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.loadbalancer.support.SimpleObjectProvider;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * 开发转发规则配置
 *
 * @author 271007729@qq.com
 * @date 2019/11/25 11:58 AM
 */
public class DevRuleConfig {

    @Bean
    public ServiceInstanceListSupplier serviceInstanceListSupplier(ConfigurableApplicationContext context) {
        ServiceInstanceListSupplier supplier = ServiceInstanceListSupplier.builder()
                                                                          .withDiscoveryClient()
                                                                          .build(context);
        return new DevServiceInstanceListSupplier(supplier);
    }

    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorLoadBalancer(Environment environment, ServiceInstanceListSupplier serviceInstanceListSupplier) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RandomLoadBalancer(new SimpleObjectProvider<>(serviceInstanceListSupplier), name);
    }
}
