package com.tokoonline.demo.product.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import com.tokoonline.demo.product.model.dto.ProductResponseDto;

import jakarta.persistence.Column;
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

    @Column
    private String name;

    @Column
    private String desctription;

    @Column
    private BigDecimal price;

    @Column
    private BigInteger stock;

    public ProductResponseDto convertToDto(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ProductResponseDto.class);
    }
}
