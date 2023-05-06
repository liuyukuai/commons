package com.cutefool.commons.orm;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
public class ColumnLibs implements Serializable {

    /**
     * 数据库
     */
    private String database;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 是否是视图
     */
    private boolean table;

    /**
     * 表格名称
     */
    private String name;

    /**
     * 属性名称
     */
    private String javaName;

    /**
     * 属性类型
     */
    private int type;

    /**
     * 属性名称
     */
    private String typeName;

    /**
     * 字段注释
     */
    private String comment;

    /**
     * 是否可以为空
     */
    private Boolean nullable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColumnLibs)) return false;
        ColumnLibs that = (ColumnLibs) o;
        return name.equals(that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
