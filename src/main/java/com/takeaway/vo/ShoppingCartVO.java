package com.takeaway.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车条目出参
 */
@Data
public class ShoppingCartVO {

    /** 购物车条目ID */
    private Long id;

    /** 菜品ID */
    private Long dishId;

    /** 菜品名称 */
    private String name;

    /** 菜品图片 */
    private String image;

    /** 口味 */
    private String dishFlavor;

    /** 数量 */
    private Integer number;

    /** 金额 */
    private BigDecimal amount;

    /** 加入时间 */
    private LocalDateTime createTime;
}
