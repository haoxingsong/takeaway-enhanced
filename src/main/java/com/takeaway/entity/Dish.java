package com.takeaway.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品实体（对应 dish 表）
 */
@Data
@TableName("dish")
public class Dish implements Serializable {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 菜品名称 */
    private String name;

    /** 分类ID */
    private Long categoryId;

    /** 价格 */
    private BigDecimal price;

    /** 图片地址 */
    private String image;

    /** 描述 */
    private String description;

    /** 状态：1起售 0停售 */
    private Integer status;

    /** 库存（扣减预留在 Redis，此字段为兜底） */
    private Integer stock;

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
