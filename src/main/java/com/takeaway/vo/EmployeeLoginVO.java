package com.takeaway.vo;

import lombok.Data;

/**
 * 员工登录出参
 */
@Data
public class EmployeeLoginVO {

    /** 员工ID */
    private Long id;

    /** 姓名 */
    private String name;

    /** 登录账号 */
    private String username;

    /** JWT token */
    private String token;
}
