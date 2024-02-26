package com.tokoonline.demo.product.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import com.tokoonline.demo.product.model.dto.ProductResponseDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String name;

    private String desctription;

    private BigDecimal price;

    private BigInteger stock;

    public ProductResponseDto convertToDto(){
        return ProductResponseDto.builder()
            .name(this.name)
            .desctription(this.desctription)
            .price(this.price)
            .stock(this.stock)
            .build();
    }
}
