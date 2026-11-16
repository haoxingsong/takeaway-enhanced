package com.takeaway.controller;

import com.takeaway.common.result.Result;
import com.takeaway.dto.DishDTO;
import com.takeaway.dto.DishPageQueryDTO;
import com.takeaway.service.DishService;
import com.takeaway.vo.DishVO;
import com.takeaway.vo.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/api/admin/dish")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    /** 新增菜品（含口味） */
    @PostMapping
    public Result<Void> save(@Valid @RequestBody DishDTO dto) {
        dishService.save(dto);
        return Result.success();
    }

    /** 编辑菜品（含口味） */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody DishDTO dto) {
        dishService.update(dto);
        return Result.success();
    }

    /** 删除菜品 */
    @DeleteMapping
    public Result<Void> delete(@RequestParam Long id) {
        dishService.delete(id);
        return Result.success();
    }

    /** 分页查询 */
    @GetMapping("/page")
    public Result<PageResult<DishVO>> pageQuery(DishPageQueryDTO dto) {
        return Result.success(dishService.pageQuery(dto));
    }

    /** 根据ID查询（含口味） */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        return Result.success(dishService.getById(id));
    }

    /** 起售/停售 */
    @PostMapping("/status/{status}")
    public Result<Void> enableOrDisable(@PathVariable Integer status, @RequestParam Long id) {
        dishService.enableOrDisable(id, status);
        return Result.success();
    }
}
