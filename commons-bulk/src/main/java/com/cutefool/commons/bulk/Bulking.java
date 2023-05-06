package com.cutefool.commons.bulk;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 填充属性注解（控制层）
 *
 * @author 271007729@qq.com
 * @date 2019-07-03 09:30
 */
@SuppressWarnings("unused")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Bulking {

    /**
     * 包含的属性
     *
     * @return 包含的属性
     */
    String[] includes() default {};

    /**
     * 排除的属性
     *
     * @return 包含的属性
     */
    String[] excludes() default {};

    /**
     * 包含的填充属性
     *
     * @return 包含的属性
     */
    String[] includeBulks() default {};

    /**
     * 排除的填充属性
     *
     * @return 排除的属性
     */
    String[] excludeBulks() default {};
}
