/*
 *  
 */
package com.cutefool.commons.mybatis;

/**
 * 数据库列映射
 *
 * @author 271007729@qq.com
 * @date 9/6/21 6:33 PM
 */
@FunctionalInterface
public interface MysqlColumn {

    /**
     * 查询数据库字段名称
     *
     * @return 字段名称
     */
    String getEscapedColumnName();
}
