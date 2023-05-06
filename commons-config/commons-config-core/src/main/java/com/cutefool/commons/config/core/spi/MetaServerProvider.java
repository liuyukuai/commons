/*
 *
 */
package com.cutefool.commons.config.core.spi;

import com.cutefool.commons.config.core.enmus.Env;

/**
 * meta address provider
 *
 * @author 271007729@qq.com
 * @date 2022/7/26 12:18 AM
 */
public interface MetaServerProvider extends Ordered,Named {

    String getMetaAddress(Env env);
}
