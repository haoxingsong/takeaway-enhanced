package com.takeaway.dto;

import lombok.Data;

/**
 * 菜品分页查询入参
 */
@Data
public class DishPageQueryDTO {

    /** 页码 */
    private Integer page = 1;

    /** 每页条数 */
    private Integer pageSize = 10;

    /** 菜品名称（模糊查询） */
    private String name;

    /** 分类ID */
    private Long categoryId;

    /** 状态：1起售 0停售 */
    private Integer status;
}
