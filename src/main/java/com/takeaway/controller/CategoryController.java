package com.takeaway.controller;

import com.takeaway.common.result.Result;
import com.takeaway.dto.CategoryDTO;
import com.takeaway.dto.CategoryPageQueryDTO;
import com.takeaway.service.CategoryService;
import com.takeaway.vo.CategoryVO;
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

import java.util.List;

/**
 * 菜品分类管理
 */
@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /** 新增分类 */
    @PostMapping
    public Result<Void> save(@Valid @RequestBody CategoryDTO dto) {
        categoryService.save(dto);
        return Result.success();
    }

    /** 编辑分类 */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody CategoryDTO dto) {
        categoryService.update(dto);
        return Result.success();
    }

    /** 删除分类 */
    @DeleteMapping
    public Result<Void> delete(@RequestParam Long id) {
        categoryService.delete(id);
        return Result.success();
    }

    /** 分页查询 */
    @GetMapping("/page")
    public Result<PageResult<CategoryVO>> pageQuery(CategoryPageQueryDTO dto) {
        return Result.success(categoryService.pageQuery(dto));
    }

    /** 按类型查询列表 */
    @GetMapping("/list")
    public Result<List<CategoryVO>> list(@RequestParam(required = false) Integer type) {
        return Result.success(categoryService.listByType(type));
    }

    /** 启停用 */
    @PostMapping("/status/{status}")
    public Result<Void> enableOrDisable(@PathVariable Integer status, @RequestParam Long id) {
        categoryService.enableOrDisable(id, status);
        return Result.success();
    }
}
