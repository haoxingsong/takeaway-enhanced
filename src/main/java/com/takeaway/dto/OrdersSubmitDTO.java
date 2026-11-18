package com.takeaway.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 提交订单入参
 */
@Data
public class OrdersSubmitDTO {

    /** 收货地址ID */
    @NotNull(message = "收货地址不能为空")
    private Long addressBookId;

    /** 支付方式：1微信 2支付宝（模拟） */
    private Integer payMethod;

    /** 备注 */
    private String remark;
}
