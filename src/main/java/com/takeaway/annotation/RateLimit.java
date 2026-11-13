package com.takeaway.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流注解（预留）
 * 基于 Redis + Lua 脚本实现，切面在后续阶段实现
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /** 时间窗口内最大请求次数 */
    int limit() default 10;

    /** 时间窗口，单位秒 */
    int window() default 60;
}
