package com.takeaway.dto;

import lombok.Data;

/**
 * 员工分页查询入参
 */
@Data
public class EmployeePageQueryDTO {

    /** 页码 */
    private Integer page = 1;

    /** 每页条数 */
    private Integer pageSize = 10;

    /** 姓名（模糊查询） */
    private String name;
}
