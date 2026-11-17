package com.takeaway.service;

import com.takeaway.dto.ShoppingCartDTO;
import com.takeaway.vo.ShoppingCartVO;

import java.util.List;

/**
 * 购物车服务
 */
public interface ShoppingCartService {

    /** 加购（已存在则数量+1） */
    void add(ShoppingCartDTO dto);

    /** 减购（数量减到0则删除） */
    void sub(ShoppingCartDTO dto);

    /** 购物车列表 */
    List<ShoppingCartVO> list();

    /** 清空购物车 */
    void clean();
}
