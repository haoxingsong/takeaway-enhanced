package com.takeaway.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 菜品新增/编辑入参
 */
@Data
public class DishDTO {

    /** 主键（编辑时必填） */
    private Long id;

    /** 菜品名称 */
    @NotBlank(message = "菜品名称不能为空")
    private String name;

    /** 分类ID */
    @NotNull(message = "菜品分类不能为空")
    private Long categoryId;

    /** 价格 */
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    /** 图片地址 */
    private String image;

    /** 描述 */
    private String description;

    /** 库存 */
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于0")
    private Integer stock;

    /** 口味列表 */
    @Valid
    private List<DishFlavorDTO> flavors;
}
