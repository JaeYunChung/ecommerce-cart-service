package com.example.ecommercecartservice.controller;

import com.example.ecommercecartservice.domain.Cart;
import com.example.ecommercecartservice.domain.dto.CartInfoDto;
import com.example.ecommercecartservice.domain.dto.CartOrderInfoDto;
import com.example.ecommercecartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @PostMapping("/cart/save")
    public ResponseEntity<?> saveCartInfo(@RequestBody CartInfoDto dto){
        Cart cart = Cart.builder()
                .memberId(dto.memberId())
                .productCode(dto.productCode())
                .qty(dto.qty())
                .build();
        cartService.save(cart);
        return ResponseEntity.status(HttpStatus.OK)
                .body("성공적으로 상품을 장바구니에 넣었습니다.");
    }
    @PutMapping("/cart/order/rollback")
    public void rollbackByException(@RequestBody CartOrderInfoDto dto){
        cartService.rollbackCartProductInfoWhenOccurException(dto);
        log.info("장바구니 정보가 복원되었습니다.");
    }
    @PostMapping("/cart/order/product")
    public void orderCartProduct(@RequestBody CartOrderInfoDto dto){
        cartService.sendOrderInfo(dto);
        log.info("주문 정보를 카테고리 서버에 전송하였습니다.");
    }
    @PutMapping("/cart/{productCode}/update")
    public void updateCartProductQuantityPlusOne(@PathVariable(value = "productCode") String productCode,
                                                 @RequestParam(value = "memberId") Long memberId){
        cartService.updateCartProductQuantityPlusOne(memberId, productCode);
    }
}
