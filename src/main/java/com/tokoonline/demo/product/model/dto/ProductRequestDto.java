package com.tokoonline.demo.product.model.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.modelmapper.ModelMapper;

import com.tokoonline.demo.product.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String name;

    private String description;

    private BigDecimal price;

    private BigInteger stock;

    public Product convertToEntity(){
        ModelMapper modelMappper = new ModelMapper();
        return modelMappper.map(this, Product.class);
    }
}
