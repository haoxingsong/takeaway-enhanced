package com.takeaway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 员工新增/编辑入参
 */
@Data
public class EmployeeDTO {

    /** 主键（编辑时必填） */
    private Long id;

    /** 姓名 */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /** 登录账号 */
    @NotBlank(message = "账号不能为空")
    private String username;

    /** 手机号 */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /** 性别（1男 0女） */
    private String sex;

    /** 身份证号 */
    private String idNumber;
}
