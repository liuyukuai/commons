/*
 *
 */
package com.cutefool.commons.orm.rds.libs;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据库表
 *
 * @author 271007729@qq.com
 * @date 2020-12-09 17:39
 */
@Data
public class TableLibs implements Serializable {

    /**
     * 表格名称
     */
    private String name;

    /**
     * 数据库
     */
    private String database;

    /**
     * 是否是视图
     */
    private boolean table;

    /**
     * 总行数
     */
    private String remarks;

}