package com.example.ecommercecartservice.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CartOrderInfoDto {
    private String memberId;
    private String productCode;
    private int qty;
}
