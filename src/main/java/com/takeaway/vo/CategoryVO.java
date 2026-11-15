package com.takeaway.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类信息出参
 */
@Data
public class CategoryVO {

    /** 主键 */
    private Long id;

    /** 类型：1菜品分类 */
    private Integer type;

    /** 分类名称 */
    private String name;

    /** 排序号 */
    private Integer sort;

    /** 状态：1启用 0禁用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
