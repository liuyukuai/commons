package com.cutefool.commons.orm.rds.libs;

import lombok.Data;

import java.io.Serializable;

@Data
public class RdsFieldsLibs implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * dbName
     */
    private String dbName;
    /**
     * 表名
     */
    private String tableName;

    /**
     * 表名（加密）
     */
    private String tableKey;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 属性名称
     */
    private String javaName;

    /**
     * 字段注释
     */
    private String remarks;
    /**
     * 类型
     */
    private int type;

    /**
     * 类型
     */
    private String typeName;

    /**
     * 字段文案
     */
    private String label;

    /**
     * 是否表单显示
     */
    private Byte showForm;

    /**
     * 是否列表显示
     */
    private Byte showList;

    /**
     * 是否可以禁用
     */
    private Boolean nullable;

    /**
     * 长度
     */
    private Integer len;

    /**
     * 小数
     */
    private Integer scale;

    /**
     * 是否新增字段
     */
    private Byte newed;

    /**
     * 是否删除
     */
    private Byte deleted;
}
