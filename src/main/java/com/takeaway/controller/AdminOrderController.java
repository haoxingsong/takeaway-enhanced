package com.takeaway.controller;

import com.takeaway.common.result.Result;
import com.takeaway.dto.OrdersCancelDTO;
import com.takeaway.dto.OrdersPageQueryDTO;
import com.takeaway.service.OrderService;
import com.takeaway.vo.OrderVO;
import com.takeaway.vo.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单管理（管理端）
 */
@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    /** 条件分页查询 */
    @GetMapping("/page")
    public Result<PageResult<OrderVO>> pageQuery(OrdersPageQueryDTO dto) {
        return Result.success(orderService.conditionSearch(dto));
    }

    /** 接单 */
    @PutMapping("/confirm/{id}")
    public Result<Void> confirm(@PathVariable Long id) {
        orderService.confirm(id);
        return Result.success();
    }

    /** 派送 */
    @PutMapping("/delivery/{id}")
    public Result<Void> delivery(@PathVariable Long id) {
        orderService.delivery(id);
        return Result.success();
    }

    /** 完成 */
    @PutMapping("/complete/{id}")
    public Result<Void> complete(@PathVariable Long id) {
        orderService.complete(id);
        return Result.success();
    }

    /** 商家取消订单 */
    @PutMapping("/cancel")
    public Result<Void> cancel(@Valid @RequestBody OrdersCancelDTO dto) {
        orderService.adminCancel(dto);
        return Result.success();
    }
}
