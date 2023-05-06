package com.cutefool.commons.orm.interfaces;

import com.cutefool.commons.orm.ColumnLibs;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface RdsOperations {

    /**
     * 获取表名称
     *
     * @return 表名称
     */
    String table();

    /**
     * 获取表注释
     *
     * @return 表名称
     */
    String remarks();

    /**
     * 获取所有属性（额外属性、代码中不包含）
     *
     * @return 属性集合
     */
    Map<String, String> extensibleFields();

    /**
     * 获取所有属性（额外属性、代码中不包含）
     *
     * @return 属性集合
     */
    List<ColumnLibs> listColumns();

    /**
     * 获取所有属性
     *
     * @return 属性集合
     */
    Map<String, String> fieldsRemarks();

    /**
     * 获取所有属性（可导出）
     *
     * @return 属性集合
     */
    Map<String, String> excelsRemarks();

    /**
     * 获取所有属性（带过滤）
     *
     * @return 属性集合
     */
    Map<String, String> fieldsRemarks(Predicate<Map.Entry<String, String>> predicate);
}
