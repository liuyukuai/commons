/*
 *
 */
package com.cutefool.commons.config.core.spi;

import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author 271007729@qq.com
 * @date 2022/7/26 1:06 AM
 */
public interface Initializer extends Named {

    void clean(ConfigurableEnvironment environment);

    void initialize(ConfigurableEnvironment environment);

}
