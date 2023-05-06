/*
 *
 */
package com.cutefool.commons.core;

import com.cutefool.commons.core.util.Maps;

import java.util.Collection;
import java.util.Map;

/**
 * 名称查询接口
 *
 * @author 271007729@qq.com
 * @date 7/30/21 3:37 PM
 */
@SuppressWarnings("unused")
public interface Naming<ID> {

    default String name() {
        return "";
    }

    /**
     * 查询名称
     *
     * @param id id
     * @return 名称
     */
    default String names(ID id) {
        return "";
    }

    ;

    /**
     * 查询名称
     *
     * @param ids ids
     * @return 名称
     */
    default Map<ID, String> names(Collection<ID> ids) {
        return Maps.hashMap();
    }

    ;

    /**
     * 查询名称
     *
     * @param ids ids
     * @return 名称
     */
    default Map<ID, Map<String, Object>> namesMultiple(Collection<ID> ids) {
        return Maps.hashMap();
    }
}
