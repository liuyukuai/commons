/*
 *  
 */
package com.cutefool.commons.expression.variable;

/**
 * 变量接口
 *
 * @author 271007729@qq.com
 * @date 9/8/21 6:36 PM
 */
public interface Variably {

    /**
     * 名称
     *
     * @return name
     */

    String name();


    /**
     * 表达式
     *
     * @return 表达式
     */
    String expression();


    /**
     * 获取名称
     *
     * @return 名称
     */
    String getName();

}
