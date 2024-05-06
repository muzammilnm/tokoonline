package com.tokoonline.demo.product.model.dto;

import java.math.BigInteger;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductResponseDto {
    private UUID id;

    private String name;

    private String description;

    private Double price;
}
