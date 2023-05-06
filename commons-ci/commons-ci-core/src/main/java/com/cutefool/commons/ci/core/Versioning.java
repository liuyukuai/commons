/*
 *
 */
package com.cutefool.commons.ci.core;

import com.cutefool.commons.core.page.Response;
import com.cutefool.commons.version.Version;

import java.util.List;
import java.util.Map;

/**
 * 版本对象
 *
 * @author 271007729@qq.com
 * @date 7/21/21 1:31 PM
 */
@SuppressWarnings("unused")
public interface Versioning {

    /**
     * 查询版本号
     *
     * @param context context
     * @return version
     */
    <T extends CiContext<T>> Version version(T context);

    /**
     * 更新属性
     *
     * @param context context
     * @param props   props
     * @return response
     */
    <T extends CiContext<T>> Response<Boolean> updateProperties(T context, Map<String, String> props);

    /**
     * 更新依赖版本
     *
     * @param context    context
     * @param dependency dependency
     * @return response
     */
    <T extends CiContext<T>>  Response<Boolean> update(T context, Dependency dependency);

    /**
     * 更新依赖版本
     *
     * @param context      context
     * @param dependencies dependencies
     * @return response
     */
    <T extends CiContext<T>>  Response<Boolean> update(T context, List<Dependency> dependencies);

}
