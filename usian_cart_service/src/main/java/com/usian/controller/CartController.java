package com.usian.controller;

import com.usian.pojo.TbItem;
import com.usian.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 购物车操作
 */
@RestController
@RequestMapping("/service/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 根据用户 ID 查询用户购物车
     */
    @RequestMapping("/selectCartByUserId")
    public Map<String, TbItem> selectCartByUserId(@RequestParam String userId){
        return this.cartService.selectCartByUserId(userId);
    }

    /**
     * 将购物车缓存到 redis 中
     */
    @RequestMapping("/insertCart")
    public Boolean insertCart(String userId, @RequestBody Map<String, TbItem> map) {
        return this.cartService.insertCart(userId, map);
    }
}