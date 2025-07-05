package com.example.ecommercecartservice.service;

import com.example.ecommercecartservice.domain.Cart;
import com.example.ecommercecartservice.domain.dto.CartOrderInfoDto;
import com.example.ecommercecartservice.domain.dto.OrderInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        List<OrderInfoDto> dto = new ArrayList<>(List.of(new OrderInfoDto(1L, "code", 11)));
        cartService.sendOrderInfo(dto);
    }

    @Test
    void testSendCartOrderInfoWithinStock(){
        Cart cart = Cart.builder()
                .memberId(1L)
                .productCode("code")
                .qty(11)
                .build();
        cartService.save(cart);
        List<OrderInfoDto> dto = new ArrayList<>(List.of(new OrderInfoDto(1L, "code", 9)));
        cartService.sendOrderInfo(dto);
    }
}