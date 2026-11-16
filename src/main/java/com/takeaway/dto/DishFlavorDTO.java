package com.takeaway.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 菜品口味入参
 */
@Data
public class DishFlavorDTO {

    /** 口味名称 */
    @NotBlank(message = "口味名称不能为空")
    private String name;

    /** 口味值（JSON数组字符串） */
    @NotBlank(message = "口味值不能为空")
    private String value;
}
