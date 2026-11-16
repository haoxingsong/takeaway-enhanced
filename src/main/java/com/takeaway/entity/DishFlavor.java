package com.takeaway.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜品口味实体（对应 dish_flavor 表）
 */
@Data
@TableName("dish_flavor")
public class DishFlavor implements Serializable {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 菜品ID */
    private Long dishId;

    /** 口味名称（如：辣度） */
    private String name;

    /** 口味值（JSON数组，如：["不辣","微辣"]） */
    private String value;
}
