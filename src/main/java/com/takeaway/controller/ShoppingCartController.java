package com.takeaway.controller;

import com.takeaway.common.result.Result;
import com.takeaway.dto.ShoppingCartDTO;
import com.takeaway.service.ShoppingCartService;
import com.takeaway.vo.ShoppingCartVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 购物车（C 端）
 */
@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    /** 加购 */
    @PostMapping("/add")
    public Result<Void> add(@Valid @RequestBody ShoppingCartDTO dto) {
        shoppingCartService.add(dto);
        return Result.success();
    }

    /** 减购 */
    @PostMapping("/sub")
    public Result<Void> sub(@Valid @RequestBody ShoppingCartDTO dto) {
        shoppingCartService.sub(dto);
        return Result.success();
    }

    /** 购物车列表 */
    @GetMapping("/list")
    public Result<List<ShoppingCartVO>> list() {
        return Result.success(shoppingCartService.list());
    }

    /** 清空购物车 */
    @DeleteMapping("/clean")
    public Result<Void> clean() {
        shoppingCartService.clean();
        return Result.success();
    }
}
