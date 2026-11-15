package com.takeaway.service;

import com.takeaway.dto.CategoryDTO;
import com.takeaway.dto.CategoryPageQueryDTO;
import com.takeaway.vo.CategoryVO;
import com.takeaway.vo.PageResult;

import java.util.List;

/**
 * 菜品分类服务
 */
public interface CategoryService {

    /** 新增分类 */
    void save(CategoryDTO dto);

    /** 编辑分类 */
    void update(CategoryDTO dto);

    /** 删除分类 */
    void delete(Long id);

    /** 分页查询 */
    PageResult<CategoryVO> pageQuery(CategoryPageQueryDTO dto);

    /** 按类型查询列表 */
    List<CategoryVO> listByType(Integer type);

    /** 启停用 */
    void enableOrDisable(Long id, Integer status);
}
