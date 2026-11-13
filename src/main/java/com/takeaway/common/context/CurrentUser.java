package com.takeaway.common.context;

import lombok.Data;

/**
 * 当前登录用户信息（从 JWT 解析后放入 ThreadLocal）
 */
@Data
public class CurrentUser {

    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 角色 */
    private String role;
}
