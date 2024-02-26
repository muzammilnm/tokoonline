package com.tokoonline.demo.product.model.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private String name;

    private String desctription;

    private BigDecimal price;

    private BigInteger stock;
}
