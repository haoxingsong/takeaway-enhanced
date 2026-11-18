package com.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeaway.common.constant.OrderConstant;
import com.takeaway.common.constant.StatusConstant;
import com.takeaway.common.context.CurrentUser;
import com.takeaway.common.context.UserContext;
import com.takeaway.common.exception.BaseException;
import com.takeaway.common.result.ResultCode;
import com.takeaway.dto.OrdersCancelDTO;
import com.takeaway.dto.OrdersPageQueryDTO;
import com.takeaway.dto.OrdersSubmitDTO;
import com.takeaway.entity.AddressBook;
import com.takeaway.entity.Dish;
import com.takeaway.entity.OrderDetail;
import com.takeaway.entity.Orders;
import com.takeaway.entity.ShoppingCart;
import com.takeaway.mapper.AddressBookMapper;
import com.takeaway.mapper.DishMapper;
import com.takeaway.mapper.OrderDetailMapper;
import com.takeaway.mapper.OrdersMapper;
import com.takeaway.mapper.ShoppingCartMapper;
import com.takeaway.service.OrderService;
import com.takeaway.vo.OrderSubmitVO;
import com.takeaway.vo.OrderVO;
import com.takeaway.vo.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 订单服务实现
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final DateTimeFormatter ORDER_NO_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final OrdersMapper ordersMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final DishMapper dishMapper;
    private final AddressBookMapper addressBookMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submit(OrdersSubmitDTO dto) {
        Long userId = currentUserId();

        AddressBook addressBook = addressBookMapper.selectById(dto.getAddressBookId());
        if (addressBook == null || !userId.equals(addressBook.getUserId())) {
            throw new BaseException("收货地址无效");
        }

        List<ShoppingCart> carts = shoppingCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId));
        if (carts.isEmpty()) {
            throw new BaseException("购物车为空，无法下单");
        }

        Map<Long, Dish> dishMap = getDishMap(carts);
        for (ShoppingCart cart : carts) {
            Dish dish = dishMap.get(cart.getDishId());
            if (dish == null) {
                throw new BaseException("购物车中存在已删除的菜品，请先移除");
            }
            if (!StatusConstant.ENABLE.equals(dish.getStatus())) {
                throw new BaseException("菜品「" + dish.getName() + "」已停售，请先移除");
            }
        }

        BigDecimal amount = carts.stream()
                .map(ShoppingCart::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Orders orders = new Orders();
        orders.setNumber(generateOrderNumber());
        orders.setStatus(OrderConstant.PENDING_PAYMENT);
        orders.setUserId(userId);
        orders.setAddressBookId(addressBook.getId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(OrderConstant.UN_PAID);
        orders.setPayMethod(dto.getPayMethod());
        orders.setAmount(amount);
        orders.setRemark(dto.getRemark());
        // 地址快照：下单后地址被修改/删除不影响历史订单
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(buildAddress(addressBook));
        ordersMapper.insert(orders);

        for (ShoppingCart cart : carts) {
            Dish dish = dishMap.get(cart.getDishId());
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(orders.getId());
            detail.setDishId(cart.getDishId());
            detail.setName(dish.getName());
            detail.setImage(dish.getImage());
            detail.setDishFlavor(cart.getDishFlavor());
            detail.setNumber(cart.getNumber());
            detail.setAmount(cart.getAmount());
            orderDetailMapper.insert(detail);
        }

        // 下单成功后清空购物车
        shoppingCartMapper.delete(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId));

        OrderSubmitVO vo = new OrderSubmitVO();
        vo.setId(orders.getId());
        vo.setOrderNumber(orders.getNumber());
        vo.setOrderAmount(orders.getAmount());
        vo.setOrderTime(orders.getOrderTime());
        return vo;
    }

    @Override
    public PageResult<OrderVO> pageQuery(OrdersPageQueryDTO dto) {
        return pageQuery(dto, currentUserId());
    }

    @Override
    public PageResult<OrderVO> conditionSearch(OrdersPageQueryDTO dto) {
        return pageQuery(dto, null);
    }

    @Override
    public OrderVO detail(Long id) {
        Orders orders = getOwnedOrder(id, currentUserId());
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(orders, vo);
        vo.setOrderDetailList(orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderId, id)));
        return vo;
    }

    @Override
    public void pay(Long id) {
        Orders orders = getOwnedOrder(id, currentUserId());
        if (!OrderConstant.PENDING_PAYMENT.equals(orders.getStatus())) {
            throw new BaseException("当前订单状态不允许支付");
        }
        Orders update = new Orders();
        update.setId(id);
        update.setStatus(OrderConstant.TO_BE_CONFIRMED);
        update.setPayStatus(OrderConstant.PAID);
        update.setCheckoutTime(LocalDateTime.now());
        ordersMapper.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(OrdersCancelDTO dto) {
        Orders orders = getOwnedOrder(dto.getId(), currentUserId());
        if (!OrderConstant.PENDING_PAYMENT.equals(orders.getStatus())
                && !OrderConstant.TO_BE_CONFIRMED.equals(orders.getStatus())) {
            throw new BaseException("当前订单状态不允许取消");
        }
        doCancel(orders, dto.getCancelReason());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminCancel(OrdersCancelDTO dto) {
        Orders orders = getOrder(dto.getId());
        if (!OrderConstant.PENDING_PAYMENT.equals(orders.getStatus())
                && !OrderConstant.TO_BE_CONFIRMED.equals(orders.getStatus())) {
            throw new BaseException("当前订单状态不允许取消");
        }
        doCancel(orders, dto.getCancelReason());
    }

    @Override
    public void confirm(Long id) {
        changeStatus(id, OrderConstant.TO_BE_CONFIRMED, OrderConstant.CONFIRMED, "接单");
    }

    @Override
    public void delivery(Long id) {
        changeStatus(id, OrderConstant.CONFIRMED, OrderConstant.DELIVERY_IN_PROGRESS, "派送");
    }

    @Override
    public void complete(Long id) {
        changeStatus(id, OrderConstant.DELIVERY_IN_PROGRESS, OrderConstant.COMPLETED, "完成");
    }

    /** 分页查询；userId 为 null 表示管理端（不限制用户） */
    private PageResult<OrderVO> pageQuery(OrdersPageQueryDTO dto, Long userId) {
        Page<Orders> page = new Page<>(dto.getPage(), dto.getPageSize());
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                .eq(userId != null, Orders::getUserId, userId)
                .eq(dto.getStatus() != null, Orders::getStatus, dto.getStatus())
                .like(StringUtils.hasText(dto.getNumber()), Orders::getNumber, dto.getNumber())
                .orderByDesc(Orders::getOrderTime);
        ordersMapper.selectPage(page, wrapper);

        List<Orders> ordersList = page.getRecords();
        Map<Long, List<OrderDetail>> detailMap = getDetailMap(ordersList);
        List<OrderVO> records = ordersList.stream()
                .map(orders -> {
                    OrderVO vo = new OrderVO();
                    BeanUtils.copyProperties(orders, vo);
                    vo.setOrderDetailList(detailMap.getOrDefault(orders.getId(), Collections.emptyList()));
                    return vo;
                })
                .collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), records);
    }

    /** 取消订单：状态置为已取消，已支付则同步标记退款 */
    private void doCancel(Orders orders, String cancelReason) {
        Orders update = new Orders();
        update.setId(orders.getId());
        update.setStatus(OrderConstant.CANCELLED);
        update.setCancelReason(cancelReason);
        if (OrderConstant.PAID.equals(orders.getPayStatus())) {
            update.setPayStatus(OrderConstant.REFUNDED);
        }
        ordersMapper.updateById(update);
    }

    /** 校验并流转订单状态 */
    private void changeStatus(Long id, Integer expectedStatus, Integer targetStatus, String actionName) {
        Orders orders = getOrder(id);
        if (!expectedStatus.equals(orders.getStatus())) {
            throw new BaseException("当前订单状态不允许" + actionName);
        }
        Orders update = new Orders();
        update.setId(id);
        update.setStatus(targetStatus);
        ordersMapper.updateById(update);
    }

    private Orders getOrder(Long id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders == null) {
            throw new BaseException(ResultCode.NOT_FOUND);
        }
        return orders;
    }

    /** 查询订单并校验归属，防止越权操作他人订单 */
    private Orders getOwnedOrder(Long id, Long userId) {
        Orders orders = getOrder(id);
        if (!userId.equals(orders.getUserId())) {
            throw new BaseException(ResultCode.FORBIDDEN);
        }
        return orders;
    }

    /** 批量查询订单明细并按订单分组 */
    private Map<Long, List<OrderDetail>> getDetailMap(List<Orders> ordersList) {
        if (ordersList.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Long> orderIds = ordersList.stream().map(Orders::getId).collect(Collectors.toList());
        return orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
                        .in(OrderDetail::getOrderId, orderIds))
                .stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId));
    }

    private Map<Long, Dish> getDishMap(List<ShoppingCart> carts) {
        Set<Long> dishIds = carts.stream()
                .map(ShoppingCart::getDishId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (dishIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return dishMapper.selectBatchIds(dishIds).stream()
                .collect(Collectors.toMap(Dish::getId, dish -> dish, (a, b) -> a));
    }

    /** 拼接地址快照文本 */
    private String buildAddress(AddressBook addressBook) {
        return Stream.of(addressBook.getProvince(), addressBook.getCity(),
                        addressBook.getDistrict(), addressBook.getDetail())
                .filter(StringUtils::hasText)
                .collect(Collectors.joining());
    }

    /** 订单号：时间戳 + 4 位随机数 */
    private String generateOrderNumber() {
        return LocalDateTime.now().format(ORDER_NO_FORMATTER)
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }

    private Long currentUserId() {
        CurrentUser user = UserContext.get();
        if (user == null) {
            throw new BaseException(ResultCode.UNAUTHORIZED);
        }
        return user.getId();
    }
}
