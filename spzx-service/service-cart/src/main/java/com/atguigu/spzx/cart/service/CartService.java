package com.atguigu.spzx.cart.service;

import org.springframework.stereotype.Service;

@Service
public interface CartService {
    void addToCart(Long skuId, Integer skuNum);
}
