package com.tokoonline.demo.product;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tokoonline.demo.product.model.Product;
import com.tokoonline.demo.product.model.dto.ProductRequestDto;
import com.tokoonline.demo.product.model.dto.ProductResponseDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> fetchAll(){
        List<Product> productList = productService.fetchAll();
        List<ProductResponseDto> responseDtoList = productList.stream().map(Product::convertToDto).collect(Collectors.toList());

        return ResponseEntity.ok().body(responseDtoList);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> fetchById(@PathVariable UUID id){
        Product product = productService.fetchById(id);

        return ResponseEntity.ok().body(product.convertToDto());
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> add(@RequestBody ProductRequestDto productRequestDto){
        Product convertToProduct = productRequestDto.convertToEntity();
        Product productSaved = productService.add(convertToProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(productSaved.convertToDto());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> update(@RequestBody ProductRequestDto productRequestDto, @PathVariable UUID id){
        Product convertToProduct = productRequestDto.convertToEntity();
        Product product = Product.builder()
            .id(id)
            .name(convertToProduct.getName())
            .desctription(convertToProduct.getDesctription())
            .price(convertToProduct.getPrice())
            .stock(convertToProduct.getStock())
            .build();
        Product updateProduct = productService.update(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(updateProduct.convertToDto());
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID id){
        Boolean isDeleteProduct = productService.deleteById(id);
        return ResponseEntity.ok().body(isDeleteProduct);
    }
}
