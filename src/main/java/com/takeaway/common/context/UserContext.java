package com.takeaway.common.context;

/**
 * 用户上下文：基于 ThreadLocal 保存当前请求的登录用户
 * 由 JwtInterceptor 写入、清理，业务代码通过 {@link #get()} 获取
 */
public class UserContext {

    private static final ThreadLocal<CurrentUser> HOLDER = new ThreadLocal<>();

    public static void set(CurrentUser user) {
        HOLDER.set(user);
    }

    public static CurrentUser get() {
        return HOLDER.get();
    }

    public static void remove() {
        HOLDER.remove();
    }

    private UserContext() {
    }
}
