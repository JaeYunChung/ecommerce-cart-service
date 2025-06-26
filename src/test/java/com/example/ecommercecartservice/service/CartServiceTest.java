package com.example.ecommercecartservice.service;

import com.example.ecommercecartservice.domain.Cart;
import com.example.ecommercecartservice.domain.dto.CartOrderInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;


@SpringBootTest
class CartServiceTest {

    @Autowired
    CartService cartService;

    @Test
    void testSendOrderInfoOverStockAndProcessError(){
        Cart cart = Cart.builder()
                .memberId(1L)
                .productCode("code")
                .qty(11)
                .build();
        cartService.save(cart);
        Map<String, Integer> orderMap = new HashMap<>();
        orderMap.put("code", 11);
        CartOrderInfoDto dto = new CartOrderInfoDto(1L, orderMap);
        cartService.sendOrderInfo(dto);
    }

    @Test
    void testSendNormalCartOrderInfoWithinStock(){
        Cart cart = Cart.builder()
                .memberId(1L)
                .productCode("code")
                .qty(11)
                .build();
        cartService.save(cart);
        Map<String, Integer> orderMap = new HashMap<>();
        orderMap.put("code", 9);
        CartOrderInfoDto dto = new CartOrderInfoDto(1L, orderMap);
        cartService.sendOrderInfo(dto);
    }
}