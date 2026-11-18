package com.takeaway.vo;

import com.takeaway.entity.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单信息出参
 */
@Data
public class OrderVO {

    /** 订单ID */
    private Long id;

    /** 订单号 */
    private String number;

    /** 订单状态 */
    private Integer status;

    /** 下单用户ID */
    private Long userId;

    /** 下单时间 */
    private LocalDateTime orderTime;

    /** 支付/结账时间 */
    private LocalDateTime checkoutTime;

    /** 支付方式 */
    private Integer payMethod;

    /** 支付状态 */
    private Integer payStatus;

    /** 订单金额 */
    private BigDecimal amount;

    /** 备注 */
    private String remark;

    /** 收货手机号 */
    private String phone;

    /** 收货地址 */
    private String address;

    /** 收货人 */
    private String consignee;

    /** 取消原因 */
    private String cancelReason;

    /** 订单明细 */
    private List<OrderDetail> orderDetailList;
}
