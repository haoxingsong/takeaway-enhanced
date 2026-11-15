package com.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeaway.common.constant.StatusConstant;
import com.takeaway.common.exception.BaseException;
import com.takeaway.common.result.ResultCode;
import com.takeaway.dto.CategoryDTO;
import com.takeaway.dto.CategoryPageQueryDTO;
import com.takeaway.entity.Category;
import com.takeaway.mapper.CategoryMapper;
import com.takeaway.service.CategoryService;
import com.takeaway.vo.CategoryVO;
import com.takeaway.vo.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品分类服务实现
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public void save(CategoryDTO dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        category.setId(null);
        category.setStatus(StatusConstant.ENABLE);
        categoryMapper.insert(category);
    }

    @Override
    public void update(CategoryDTO dto) {
        if (dto.getId() == null) {
            throw new BaseException(ResultCode.PARAM_ERROR);
        }
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        // 状态通过启停用接口修改
        category.setStatus(null);
        categoryMapper.updateById(category);
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public PageResult<CategoryVO> pageQuery(CategoryPageQueryDTO dto) {
        Page<Category> page = new Page<>(dto.getPage(), dto.getPageSize());
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<Category>()
                .like(StringUtils.hasText(dto.getName()), Category::getName, dto.getName())
                .eq(dto.getType() != null, Category::getType, dto.getType())
                .orderByAsc(Category::getSort);
        categoryMapper.selectPage(page, wrapper);
        List<CategoryVO> records = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), records);
    }

    @Override
    public List<CategoryVO> listByType(Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<Category>()
                .eq(type != null, Category::getType, type)
                .orderByAsc(Category::getSort);
        return categoryMapper.selectList(wrapper).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public void enableOrDisable(Long id, Integer status) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        categoryMapper.updateById(category);
    }

    private CategoryVO toVO(Category category) {
        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }
}
