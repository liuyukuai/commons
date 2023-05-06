/*
 *
 */
package com.cutefool.commons.feign;

import com.cutefool.commons.core.util.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 271007729@qq.com
 * @date 2022/5/6 6:22 PM
 */
public class DevServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {

    @Resource
    private DevRuleProperties devRuleProperties;

    public DevServiceInstanceListSupplier(ServiceInstanceListSupplier delegate) {
        super(delegate);
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return getDelegate().get().map(this::doInstances);
    }

    /**
     * 使用随机数获取服务
     *
     * @param instances instances
     * @return instances
     */
    private List<ServiceInstance> doInstances(List<ServiceInstance> instances) {
        if (Lists.isEmpty(instances)) {
            return instances;
        }

        if (!devRuleProperties.isEnabled()) {
            return instances;
        }
        // 多个ip
        List<String> ips = Arrays.asList(StringUtils.split(devRuleProperties.getIp(), ","));

        return instances.stream().filter(e -> ips.contains(e.getHost())).collect(Collectors.toList());
    }
}
