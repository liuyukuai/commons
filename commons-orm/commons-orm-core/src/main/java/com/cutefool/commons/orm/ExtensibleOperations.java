package com.cutefool.commons.orm;

import java.util.Map;

/**
 * 可扩展的对象
 */
public interface ExtensibleOperations {

    /**
     * 获取扩展属性
     *
     * @return maps
     */
    Map<String, String> extensibleFields();


    default Class<?> domainClass() {
        return Void.class;
    }

    ;

}
