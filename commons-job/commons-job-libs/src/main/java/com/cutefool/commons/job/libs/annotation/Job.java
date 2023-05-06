/*
 *  
 */
package com.cutefool.commons.job.libs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务注解
 *
 * @author 271007729@qq.com
 * @date 9/3/21 5:39 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Job {

    String cron() default "";

    String name() default "";

    int shardingCount() default 1;

    String description() default "";

    boolean disabled() default false;

    boolean overwrite() default true;

}
