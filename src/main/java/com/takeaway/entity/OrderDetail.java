package com.takeaway.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细实体（对应 order_detail 表）
 */
@Data
@TableName("order_detail")
public class OrderDetail implements Serializable {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 订单ID */
    private Long orderId;

    /** 菜品ID */
    private Long dishId;

    /** 菜品名称（快照） */
    private String name;

    /** 菜品图片（快照） */
    private String image;

    /** 口味（快照） */
    private String dishFlavor;

    /** 数量 */
    private Integer number;

    /** 金额（单价 * 数量） */
    private BigDecimal amount;
}
