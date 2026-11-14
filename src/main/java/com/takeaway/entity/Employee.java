package com.takeaway.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体（对应 employee 表）
 */
@Data
@TableName("employee")
public class Employee implements Serializable {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 姓名 */
    private String name;

    /** 登录账号 */
    private String username;

    /** 密码（BCrypt 加密） */
    private String password;

    /** 手机号 */
    private String phone;

    /** 性别（1男 0女） */
    private String sex;

    /** 身份证号 */
    private String idNumber;

    /** 角色：ADMIN管理员 / EMPLOYEE普通员工 */
    private String role;

    /** 状态：1启用 0禁用 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 创建人 */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /** 更新人 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
