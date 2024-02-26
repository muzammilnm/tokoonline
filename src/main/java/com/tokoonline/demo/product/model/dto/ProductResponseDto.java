package com.tokoonline.demo.product.model.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class ProductResponseDto {
    private String name;

    private String desctription;

    private BigDecimal price;

    private BigInteger stock;
}
