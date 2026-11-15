package com.takeaway.dto;

import lombok.Data;

/**
 * 分类分页查询入参
 */
@Data
public class CategoryPageQueryDTO {

    /** 页码 */
    private Integer page = 1;

    /** 每页条数 */
    private Integer pageSize = 10;

    /** 分类名称（模糊查询） */
    private String name;

    /** 类型 */
    private Integer type;
}
