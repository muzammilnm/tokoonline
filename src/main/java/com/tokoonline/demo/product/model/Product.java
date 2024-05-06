package com.tokoonline.demo.product.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import com.tokoonline.demo.category.model.Category;
import com.tokoonline.demo.product.model.dto.ProductResponseDto;
import com.tokoonline.demo.product.model.dto.SearchProductResponseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    private String description;

    @Column
    private Double price;

    @Column
    private BigInteger stock;

    @ManyToMany
    @JoinTable(
        name = "product_category",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public ProductResponseDto convertToDto(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ProductResponseDto.class);
    }

    public SearchProductResponseDto convertToSearchDto(){
        ModelMapper modelMappper = new ModelMapper();
        return modelMappper.map(this, SearchProductResponseDto.class);
    }
}
