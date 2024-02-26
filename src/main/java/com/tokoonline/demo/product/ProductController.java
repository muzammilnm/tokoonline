package com.tokoonline.demo.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tokoonline.demo.product.model.Product;
import com.tokoonline.demo.product.model.dto.ProductResponseDto;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ProductController {
    
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> fetchAll(){
        List<Product> productList = productService.fetchAll();
        List<ProductResponseDto> responseDtoList = productList.stream().map(Product::convertToDto).collect(Collectors.toList());

        return ResponseEntity.ok().body(responseDtoList);
    }

}
