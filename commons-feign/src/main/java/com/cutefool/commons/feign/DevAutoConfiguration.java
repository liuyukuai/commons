/*
 *
 */
package com.cutefool.commons.feign;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author 271007729@qq.com
 * @date 2022/5/7 11:28 PM
 */
@Configuration
@LoadBalancerClients(defaultConfiguration = DevRuleConfig.class)
public class DevAutoConfiguration {
}
