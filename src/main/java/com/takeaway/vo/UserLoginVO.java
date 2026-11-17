package com.takeaway.vo;

import lombok.Data;

/**
 * C 端用户登录出参
 */
@Data
public class UserLoginVO {

    /** 用户ID */
    private Long id;

    /** 微信 openid */
    private String openid;

    /** 昵称 */
    private String name;

    /** JWT token */
    private String token;
}
