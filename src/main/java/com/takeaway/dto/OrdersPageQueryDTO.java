package com.takeaway.dto;

import lombok.Data;

/**
 * 订单分页查询入参（C 端用状态筛选，管理端可加订单号）
 */
@Data
public class OrdersPageQueryDTO {

    /** 页码 */
    private Integer page = 1;

    /** 每页条数 */
    private Integer pageSize = 10;

    /** 订单状态 */
    private Integer status;

    /** 订单号 */
    private String number;
}
