package com.example.ecommercecartservice.service;

import com.example.ecommercecartservice.domain.Cart;
import com.example.ecommercecartservice.domain.dto.CartOrderInfoDto;
import com.example.ecommercecartservice.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final StreamBridge streamBridge;
    public void save(Cart cart){
        cartRepository.save(cart);
    }
    @Transactional
    public void sendOrderInfo(CartOrderInfoDto dto){
        streamBridge.send("sendOrderInfo-out-0",
                MessageBuilder.withPayload(dto).build());
        Long memberId = dto.getMemberId();
        Set<String> deleteProductList = dto.getOrderMap().keySet();
        cartRepository.deleteAllByMemberIdAndProductSet(memberId, deleteProductList);
    }
    @Transactional
    public void rollbackCartProductInfoWhenOccurException(CartOrderInfoDto dto){
        List<Cart> cartList = new ArrayList<>();
        Map<String, Integer> orderMap = dto.getOrderMap();
        for (String productCode:orderMap.keySet()){
            Cart cart = Cart.builder()
                    .memberId(dto.getMemberId())
                    .productCode(productCode)
                    .qty(orderMap.get(productCode))
                    .build();
            cartList.add(cart);
        }
        cartRepository.saveAll(cartList);
    }
    @Transactional
    public void updateCartProductQuantityPlusOne(Long memberId, String productCode){
        cartRepository.updateQuantityByMemberIdAndProductCode(memberId, productCode);
    }
}
