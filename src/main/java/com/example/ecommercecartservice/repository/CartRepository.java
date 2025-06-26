package com.example.ecommercecartservice.repository;


import com.example.ecommercecartservice.domain.Cart;
import com.example.ecommercecartservice.domain.dto.CartOrderInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Set;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Modifying
    @Query("delete from Cart where memberId = :memberId and productCode in :productSet")
    void deleteAllByMemberIdAndProductSet(@Param("memberId") Long memberId,
                                           @Param("productSet") Set<String> productSet);


    @Modifying
    @Query("update Cart set qty=qty+1 where memberId=:memberId and productCode=:productCode")
    void updateQuantityByMemberIdAndProductCode(@Param("memberId") Long memberId,
                                                @Param("productCode") String productCode);
}
