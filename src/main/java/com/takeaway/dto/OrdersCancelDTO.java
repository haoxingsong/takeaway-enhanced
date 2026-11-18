package com.takeaway.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 取消订单入参
 */
@Data
public class OrdersCancelDTO {

    /** 订单ID */
    @NotNull(message = "订单ID不能为空")
    private Long id;

    /** 取消原因 */
    private String cancelReason;
}
