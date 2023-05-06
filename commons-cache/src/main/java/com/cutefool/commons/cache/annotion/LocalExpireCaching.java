package com.cutefool.commons.cache.annotion;

import com.cutefool.commons.cache.CachingConstants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Inherited
@Documented
@Cacheable(cacheManager = CachingConstants.LOCAL_CACHE_MANAGER)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalExpireCaching {

    @AliasFor(annotation = Cacheable.class)
    String[] value() default {};

    @AliasFor(value = "key", annotation = Cacheable.class)
    String key() default "";

    @AliasFor(value = "keyGenerator", annotation = Cacheable.class)
    String keyGenerator() default "";

    @AliasFor(value = "cacheManager", annotation = Cacheable.class)
    String cacheManager() default CachingConstants.LOCAL_CACHE_MANAGER;

    @AliasFor(value = "cacheResolver", annotation = Cacheable.class)
    String cacheResolver() default "";

    @AliasFor(value = "condition", annotation = Cacheable.class)
    String condition() default "";

    @AliasFor(value = "unless", annotation = Cacheable.class)
    String unless() default "";

    @AliasFor(value = "sync", annotation = Cacheable.class)
    boolean sync() default false;

    /**
     * 缓存过期时间（单位秒）
     *
     * @return 过期时间
     */
    long expireSecond() default 0;
}
