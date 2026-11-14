package com.takeaway.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 员工信息出参（不含密码）
 */
@Data
public class EmployeeVO {

    /** 员工ID */
    private Long id;

    /** 姓名 */
    private String name;

    /** 登录账号 */
    private String username;

    /** 手机号 */
    private String phone;

    /** 性别（1男 0女） */
    private String sex;

    /** 身份证号 */
    private String idNumber;

    /** 角色 */
    private String role;

    /** 状态：1启用 0禁用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
