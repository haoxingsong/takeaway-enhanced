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
 * 订单实体（对应 orders 表）
 */
@Data
@TableName("orders")
public class Orders implements Serializable {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 订单号 */
    private String number;

    /** 订单状态：1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 */
    private Integer status;

    /** 下单用户ID */
    private Long userId;

    /** 收货地址ID */
    private Long addressBookId;

    /** 下单时间 */
    private LocalDateTime orderTime;

    /** 支付/结账时间 */
    private LocalDateTime checkoutTime;

    /** 支付方式：1微信 2支付宝（模拟） */
    private Integer payMethod;

    /** 支付状态：0未支付 1已支付 2已退款 */
    private Integer payStatus;

    /** 订单实收金额 */
    private BigDecimal amount;

    /** 备注 */
    private String remark;

    /** 收货手机号（快照） */
    private String phone;

    /** 收货地址（快照） */
    private String address;

    /** 收货人（快照） */
    private String consignee;

    /** 取消原因 */
    private String cancelReason;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
