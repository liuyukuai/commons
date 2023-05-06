package com.cutefool.commons.bulk;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 填充属性注解
 *
 * @author 271007729@qq.com
 * @date 2019-07-03 09:30
 */
@SuppressWarnings("unused")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Bulked {

    /**
     * 查询属性的对象
     *
     * @return 返回需要查询名称实现类
     */
    String value() default "";

    /**
     * 关联的属性值
     *
     * @return 返回需要查询名称实现类
     */
    String fields() default "";

    /**
     * 字段说明
     *
     * @return 字段说明
     */
    String remarks() default "";

    /**
     * 是否是多属性查询
     *
     * @return true or false
     */
    boolean multiple() default false;

    /**
     * 多值的时候映射
     *
     * @return 映射
     */
    BulkMapping[] mapping() default {};

}
