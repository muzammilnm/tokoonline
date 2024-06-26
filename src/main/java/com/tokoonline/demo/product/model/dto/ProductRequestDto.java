package com.tokoonline.demo.product.model.dto;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import com.tokoonline.demo.product.model.Product;

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
public class ProductRequestDto {
    private String name;

    private String description;

    private Double price;

    private BigInteger stock;

    private List<UUID> categoryIds;

    public Product convertToEntity(){
        ModelMapper modelMappper = new ModelMapper();
        return modelMappper.map(this, Product.class);
    }
}
