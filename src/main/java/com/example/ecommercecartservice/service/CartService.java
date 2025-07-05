package com.example.ecommercecartservice.service;

import com.example.ecommercecartservice.domain.Cart;
import com.example.ecommercecartservice.domain.dto.OrderInfoDto;
import com.example.ecommercecartservice.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final StreamBridge streamBridge;
    public void save(Cart cart){
        cartRepository.save(cart);
    }
    @Transactional
    public void sendOrderInfo(List<OrderInfoDto> dto){
        if (dto.isEmpty()){
            throw new IllegalArgumentException("주문할 물건이 없습니다.");
        }
        streamBridge.send("sendOrderInfo-out-0",
                MessageBuilder.withPayload(dto).build());
        Long memberId = dto.getFirst().memberId();
        Set<String> deleteProductList = dto.stream().map(OrderInfoDto::productCode).collect(Collectors.toSet());
        cartRepository.deleteAllByMemberIdAndProductCodeSet(memberId, deleteProductList);
    }
    @Transactional
    public void rollbackCartProductInfoWhenOccurException(List<OrderInfoDto> dto){
        List<Cart> cartList = new ArrayList<>();
        for (OrderInfoDto order : dto){
            Cart cart = Cart.builder()
                    .memberId(order.memberId())
                    .productCode(order.productCode())
                    .qty(order.qty())
                    .build();
            cartList.add(cart);
        }
        cartRepository.saveAll(cartList);
    }
    @Transactional
    public void increaseQuantity(Long memberId, String productCode){
        cartRepository.updateQuantityByMemberIdAndProductCode(memberId, productCode);
    }
}
