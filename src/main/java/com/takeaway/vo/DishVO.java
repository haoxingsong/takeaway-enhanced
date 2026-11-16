package com.takeaway.vo;

import com.takeaway.dto.DishFlavorDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜品信息出参
 */
@Data
public class DishVO {

    /** 主键 */
    private Long id;

    /** 菜品名称 */
    private String name;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;

    /** 价格 */
    private BigDecimal price;

    /** 图片地址 */
    private String image;

    /** 描述 */
    private String description;

    /** 状态：1起售 0停售 */
    private Integer status;

    /** 库存 */
    private Integer stock;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 口味列表 */
    private List<DishFlavorDTO> flavors;
}
