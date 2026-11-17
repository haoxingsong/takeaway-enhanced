package com.takeaway.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 购物车加购/减购入参
 */
@Data
public class ShoppingCartDTO {

    /** 菜品ID */
    @NotNull(message = "菜品ID不能为空")
    private Long dishId;

    /** 口味（可选，同一菜品不同口味视为不同条目） */
    private String dishFlavor;
}
