package com.takeaway.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果
 *
 * @param <T> 记录类型
 */
@Data
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    /** 总记录数 */
    private Long total;

    /** 当前页记录 */
    private List<T> records;
}
