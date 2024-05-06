package com.tokoonline.demo.product.model.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductRequestDto {
    private String name;
    private String description;
    private Double price;
    private List<UUID> categoryIds;
    private Integer page;
    private Integer size;
}
