package com.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeaway.common.constant.StatusConstant;
import com.takeaway.common.exception.BaseException;
import com.takeaway.common.result.ResultCode;
import com.takeaway.dto.DishDTO;
import com.takeaway.dto.DishFlavorDTO;
import com.takeaway.dto.DishPageQueryDTO;
import com.takeaway.entity.Category;
import com.takeaway.entity.Dish;
import com.takeaway.entity.DishFlavor;
import com.takeaway.mapper.CategoryMapper;
import com.takeaway.mapper.DishFlavorMapper;
import com.takeaway.mapper.DishMapper;
import com.takeaway.service.DishService;
import com.takeaway.vo.DishVO;
import com.takeaway.vo.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜品服务实现
 */
@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;
    private final DishFlavorMapper dishFlavorMapper;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DishDTO dto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        dish.setId(null);
        // 新增菜品默认停售，通过上下架接口开启
        dish.setStatus(StatusConstant.DISABLE);
        dishMapper.insert(dish);
        saveFlavors(dish.getId(), dto.getFlavors());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DishDTO dto) {
        if (dto.getId() == null) {
            throw new BaseException(ResultCode.PARAM_ERROR);
        }
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        // 上下架走独立接口，编辑不修改状态
        dish.setStatus(null);
        dishMapper.updateById(dish);
        // 口味整体覆盖：先删后插
        dishFlavorMapper.delete(new LambdaQueryWrapper<DishFlavor>()
                .eq(DishFlavor::getDishId, dto.getId()));
        saveFlavors(dto.getId(), dto.getFlavors());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Dish dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new BaseException(ResultCode.NOT_FOUND);
        }
        if (StatusConstant.ENABLE.equals(dish.getStatus())) {
            throw new BaseException("起售中的菜品不能删除");
        }
        dishMapper.deleteById(id);
        dishFlavorMapper.delete(new LambdaQueryWrapper<DishFlavor>()
                .eq(DishFlavor::getDishId, id));
    }

    @Override
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dto) {
        Page<Dish> page = new Page<>(dto.getPage(), dto.getPageSize());
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<Dish>()
                .like(StringUtils.hasText(dto.getName()), Dish::getName, dto.getName())
                .eq(dto.getCategoryId() != null, Dish::getCategoryId, dto.getCategoryId())
                .eq(dto.getStatus() != null, Dish::getStatus, dto.getStatus())
                .orderByDesc(Dish::getUpdateTime);
        dishMapper.selectPage(page, wrapper);

        List<Dish> dishes = page.getRecords();
        Map<Long, String> categoryNameMap = getCategoryNameMap(dishes);
        List<DishVO> records = dishes.stream()
                .map(dish -> {
                    DishVO vo = new DishVO();
                    BeanUtils.copyProperties(dish, vo);
                    vo.setCategoryName(categoryNameMap.get(dish.getCategoryId()));
                    return vo;
                })
                .collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), records);
    }

    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new BaseException(ResultCode.NOT_FOUND);
        }
        DishVO vo = new DishVO();
        BeanUtils.copyProperties(dish, vo);

        Category category = categoryMapper.selectById(dish.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
        List<DishFlavor> flavors = dishFlavorMapper.selectList(
                new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, id));
        vo.setFlavors(flavors.stream()
                .map(flavor -> {
                    DishFlavorDTO flavorDTO = new DishFlavorDTO();
                    BeanUtils.copyProperties(flavor, flavorDTO);
                    return flavorDTO;
                })
                .collect(Collectors.toList()));
        return vo;
    }

    @Override
    public void enableOrDisable(Long id, Integer status) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.updateById(dish);
    }

    /** 批量保存口味 */
    private void saveFlavors(Long dishId, List<DishFlavorDTO> flavors) {
        if (CollectionUtils.isEmpty(flavors)) {
            return;
        }
        for (DishFlavorDTO flavorDTO : flavors) {
            DishFlavor flavor = new DishFlavor();
            flavor.setDishId(dishId);
            flavor.setName(flavorDTO.getName());
            flavor.setValue(flavorDTO.getValue());
            dishFlavorMapper.insert(flavor);
        }
    }

    /** 批量查询分类名称，避免逐条查询 */
    private Map<Long, String> getCategoryNameMap(List<Dish> dishes) {
        Set<Long> categoryIds = dishes.stream()
                .map(Dish::getCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (categoryIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return categoryMapper.selectBatchIds(categoryIds).stream()
                .collect(Collectors.toMap(Category::getId, Category::getName, (a, b) -> a));
    }
}
