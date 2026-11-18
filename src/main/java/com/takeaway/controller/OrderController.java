package com.takeaway.controller;

import com.takeaway.common.result.Result;
import com.takeaway.dto.OrdersCancelDTO;
import com.takeaway.dto.OrdersPageQueryDTO;
import com.takeaway.dto.OrdersSubmitDTO;
import com.takeaway.service.OrderService;
import com.takeaway.vo.OrderSubmitVO;
import com.takeaway.vo.OrderVO;
import com.takeaway.vo.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单（C 端）
 */
@RestController
@RequestMapping("/api/user/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /** 提交订单 */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@Valid @RequestBody OrdersSubmitDTO dto) {
        return Result.success(orderService.submit(dto));
    }

    /** 我的订单分页查询 */
    @GetMapping("/page")
    public Result<PageResult<OrderVO>> pageQuery(OrdersPageQueryDTO dto) {
        return Result.success(orderService.pageQuery(dto));
    }

    /** 订单详情 */
    @GetMapping("/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        return Result.success(orderService.detail(id));
    }

    /** 模拟支付 */
    @PutMapping("/pay/{id}")
    public Result<Void> pay(@PathVariable Long id) {
        orderService.pay(id);
        return Result.success();
    }

    /** 取消订单 */
    @PutMapping("/cancel")
    public Result<Void> cancel(@Valid @RequestBody OrdersCancelDTO dto) {
        orderService.cancel(dto);
        return Result.success();
    }
}
