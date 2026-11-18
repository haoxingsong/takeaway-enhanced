package com.takeaway.service;

import com.takeaway.dto.OrdersCancelDTO;
import com.takeaway.dto.OrdersPageQueryDTO;
import com.takeaway.dto.OrdersSubmitDTO;
import com.takeaway.vo.OrderSubmitVO;
import com.takeaway.vo.OrderVO;
import com.takeaway.vo.PageResult;

/**
 * 订单服务
 */
public interface OrderService {

    /** C 端：提交订单（购物车 → 订单 + 明细，清空购物车） */
    OrderSubmitVO submit(OrdersSubmitDTO dto);

    /** C 端：分页查询我的订单 */
    PageResult<OrderVO> pageQuery(OrdersPageQueryDTO dto);

    /** 管理端：条件分页查询 */
    PageResult<OrderVO> conditionSearch(OrdersPageQueryDTO dto);

    /** C 端：订单详情（含明细） */
    OrderVO detail(Long id);

    /** C 端：模拟支付 */
    void pay(Long id);

    /** C 端：取消订单 */
    void cancel(OrdersCancelDTO dto);

    /** 管理端：商家取消订单 */
    void adminCancel(OrdersCancelDTO dto);

    /** 管理端：接单 */
    void confirm(Long id);

    /** 管理端：派送 */
    void delivery(Long id);

    /** 管理端：完成 */
    void complete(Long id);
}
