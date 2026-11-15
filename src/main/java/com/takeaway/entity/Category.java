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
 * 菜品分类实体（对应 category 表）
 */
@Data
@TableName("category")
public class Category implements Serializable {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 类型：1菜品分类 */
    private Integer type;

    /** 分类名称 */
    private String name;

    /** 排序号（越小越靠前） */
    private Integer sort;

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
