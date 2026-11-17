package com.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.takeaway.common.constant.StatusConstant;
import com.takeaway.common.context.CurrentUser;
import com.takeaway.common.context.UserContext;
import com.takeaway.common.exception.BaseException;
import com.takeaway.common.result.ResultCode;
import com.takeaway.dto.ShoppingCartDTO;
import com.takeaway.entity.Dish;
import com.takeaway.entity.ShoppingCart;
import com.takeaway.mapper.DishMapper;
import com.takeaway.mapper.ShoppingCartMapper;
import com.takeaway.service.ShoppingCartService;
import com.takeaway.vo.ShoppingCartVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 购物车服务实现
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;
    private final DishMapper dishMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ShoppingCartDTO dto) {
        Long userId = currentUserId();
        Dish dish = getAvailableDish(dto.getDishId());
        String flavor = normalizeFlavor(dto.getDishFlavor());

        ShoppingCart cart = findCart(userId, dish.getId(), flavor);
        if (cart != null) {
            cart.setNumber(cart.getNumber() + 1);
            cart.setAmount(calcAmount(dish.getPrice(), cart.getNumber()));
            shoppingCartMapper.updateById(cart);
            return;
        }
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUserId(userId);
        newCart.setDishId(dish.getId());
        newCart.setDishFlavor(flavor);
        newCart.setNumber(1);
        newCart.setAmount(dish.getPrice());
        shoppingCartMapper.insert(newCart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sub(ShoppingCartDTO dto) {
        Long userId = currentUserId();
        Dish dish = getAvailableDish(dto.getDishId());
        String flavor = normalizeFlavor(dto.getDishFlavor());

        ShoppingCart cart = findCart(userId, dish.getId(), flavor);
        if (cart == null) {
            throw new BaseException("购物车中不存在该菜品");
        }
        if (cart.getNumber() <= 1) {
            shoppingCartMapper.deleteById(cart.getId());
            return;
        }
        cart.setNumber(cart.getNumber() - 1);
        cart.setAmount(calcAmount(dish.getPrice(), cart.getNumber()));
        shoppingCartMapper.updateById(cart);
    }

    @Override
    public List<ShoppingCartVO> list() {
        Long userId = currentUserId();
        List<ShoppingCart> carts = shoppingCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId)
                .orderByAsc(ShoppingCart::getCreateTime));
        if (carts.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, Dish> dishMap = getDishMap(carts);
        return carts.stream()
                .map(cart -> {
                    ShoppingCartVO vo = new ShoppingCartVO();
                    BeanUtils.copyProperties(cart, vo);
                    Dish dish = dishMap.get(cart.getDishId());
                    if (dish != null) {
                        vo.setName(dish.getName());
                        vo.setImage(dish.getImage());
                    }
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void clean() {
        shoppingCartMapper.delete(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, currentUserId()));
    }

    /** 查询并校验菜品可售状态 */
    private Dish getAvailableDish(Long dishId) {
        Dish dish = dishMapper.selectById(dishId);
        if (dish == null) {
            throw new BaseException(ResultCode.NOT_FOUND);
        }
        if (!StatusConstant.ENABLE.equals(dish.getStatus())) {
            throw new BaseException("菜品已停售");
        }
        return dish;
    }

    /** 查询购物车中是否已存在同一菜品同一口味的条目 */
    private ShoppingCart findCart(Long userId, Long dishId, String flavor) {
        return shoppingCartMapper.selectOne(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId)
                .eq(ShoppingCart::getDishId, dishId)
                .eq(ShoppingCart::getDishFlavor, flavor));
    }

    /** 批量查询菜品信息，避免逐条查询 */
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

    private BigDecimal calcAmount(BigDecimal price, Integer number) {
        return price.multiply(BigDecimal.valueOf(number));
    }

    /** 口味统一空串处理，避免 null 参与等值比较失败 */
    private String normalizeFlavor(String flavor) {
        return flavor == null ? "" : flavor;
    }

    private Long currentUserId() {
        CurrentUser user = UserContext.get();
        if (user == null) {
            throw new BaseException(ResultCode.UNAUTHORIZED);
        }
        return user.getId();
    }
}
