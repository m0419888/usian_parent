package com.usian.service;

import com.usian.pojo.TbItem;
import com.usian.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 购物车操作业务层
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisClient redisClient;

    @Value("${CART_REDIS_KEY}")
    private String CART_REDIS_KEY;

    /**
     * 根据用户 ID 查询用户购物车
     */
    @Override
    public Map<String, TbItem> selectCartByUserId(String userId) {
        return (Map<String, TbItem>) redisClient.hget(CART_REDIS_KEY,userId);
    }

    /**
     * 缓存购物车
     *
     * @param cart
     */
    @Override
    public Boolean insertCart(String userId, Map<String, TbItem> cart) {
        return redisClient.hset(CART_REDIS_KEY, userId, cart);
    }
}