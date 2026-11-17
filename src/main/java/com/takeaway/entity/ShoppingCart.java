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
 * 购物车实体（对应 shopping_cart 表）
 */
@Data
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 菜品ID */
    private Long dishId;

    /** 口味（JSON，无口味时为空串） */
    private String dishFlavor;

    /** 数量 */
    private Integer number;

    /** 金额（单价 * 数量） */
    private BigDecimal amount;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
