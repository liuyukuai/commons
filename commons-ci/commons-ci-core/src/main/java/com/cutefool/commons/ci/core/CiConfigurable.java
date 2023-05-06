/*
 *
 */
package com.cutefool.commons.ci.core;

/**
 * 可配置接口
 *
 * @author 271007729@qq.com
 * @date 7/11/21 10:06 PM
 */
public interface CiConfigurable<T extends CiConfig> {

    /**
     * 插件
     *
     * @return 工具配置
     */
    T config();
}
