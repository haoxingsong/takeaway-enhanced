package com.takeaway.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提交订单出参
 */
@Data
public class OrderSubmitVO {

    /** 订单ID */
    private Long id;

    /** 订单号 */
    private String orderNumber;

    /** 订单金额 */
    private BigDecimal orderAmount;

    /** 下单时间 */
    private LocalDateTime orderTime;
}
