package com.takeaway.controller;

import com.takeaway.common.result.Result;
import com.takeaway.dto.EmployeeDTO;
import com.takeaway.dto.EmployeePageQueryDTO;
import com.takeaway.service.EmployeeService;
import com.takeaway.vo.EmployeeVO;
import com.takeaway.vo.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/api/admin/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /** 新增员工 */
    @PostMapping
    public Result<Void> save(@Valid @RequestBody EmployeeDTO dto) {
        employeeService.save(dto);
        return Result.success();
    }

    /** 编辑员工 */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody EmployeeDTO dto) {
        employeeService.update(dto);
        return Result.success();
    }

    /** 分页查询 */
    @GetMapping("/page")
    public Result<PageResult<EmployeeVO>> pageQuery(EmployeePageQueryDTO dto) {
        return Result.success(employeeService.pageQuery(dto));
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public Result<EmployeeVO> getById(@PathVariable Long id) {
        return Result.success(employeeService.getById(id));
    }

    /** 启停用 */
    @PostMapping("/status/{status}")
    public Result<Void> enableOrDisable(@PathVariable Integer status, @RequestParam Long id) {
        employeeService.enableOrDisable(id, status);
        return Result.success();
    }
}
