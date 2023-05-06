package com.cutefool.commons.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 271007729@qq.com
 * @date 2019-07-03 09:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Exclude {
    /**
     * 需要排除的属性（只能作用于类）
     */
    String[] value() default {};
}
