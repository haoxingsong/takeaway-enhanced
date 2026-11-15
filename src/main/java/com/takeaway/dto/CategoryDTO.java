package com.takeaway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分类新增/编辑入参
 */
@Data
public class CategoryDTO {

    /** 主键（编辑时必填） */
    private Long id;

    /** 类型：1菜品分类 */
    @NotNull(message = "分类类型不能为空")
    private Integer type;

    /** 分类名称 */
    @NotBlank(message = "分类名称不能为空")
    private String name;

    /** 排序号 */
    @NotNull(message = "排序号不能为空")
    private Integer sort;
}
