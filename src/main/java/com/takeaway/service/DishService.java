package com.takeaway.service;

import com.takeaway.dto.DishDTO;
import com.takeaway.dto.DishPageQueryDTO;
import com.takeaway.vo.DishVO;
import com.takeaway.vo.PageResult;

/**
 * 菜品服务
 */
public interface DishService {

    /** 新增菜品（含口味） */
    void save(DishDTO dto);

    /** 编辑菜品（含口味） */
    void update(DishDTO dto);

    /** 删除菜品（含口味） */
    void delete(Long id);

    /** 分页查询 */
    PageResult<DishVO> pageQuery(DishPageQueryDTO dto);

    /** 根据ID查询（含口味） */
    DishVO getById(Long id);

    /** 起售/停售 */
    void enableOrDisable(Long id, Integer status);
}
