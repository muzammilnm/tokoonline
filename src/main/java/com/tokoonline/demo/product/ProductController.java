package com.tokoonline.demo.product;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tokoonline.demo.product.model.Product;
import com.tokoonline.demo.product.model.dto.ProductRequestDto;
import com.tokoonline.demo.product.model.dto.ProductResponseDto;
import com.tokoonline.demo.product.model.dto.SearchProductRequestDto;
import com.tokoonline.demo.product.model.dto.SearchProductResponseDto;
import com.tokoonline.demo.response.PageResponse;
import com.tokoonline.demo.response.ResponseSuccess;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ResponseSuccess> search(@RequestParam(value = "name", required = false) String name,
                                                            @RequestParam(value = "price", required = false) Double price,
                                                            @RequestParam(value = "categoryIds", required = false, defaultValue = "") List<UUID> categoryIds,
                                                            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){
        SearchProductRequestDto request = SearchProductRequestDto.builder()
            .page(page)
            .size(size)
            .name(name)
            .categoryIds(categoryIds)
            .price(price)
            .build();
        Page<Product> products = productService.search(request);
        List<SearchProductResponseDto> productResponse = products.getContent().stream().map(product -> product.convertToSearchDto()).toList();

        PageResponse pages = PageResponse.builder()
            .size(products.getSize())
            .total(products.getTotalElements())
            .totalPage(products.getTotalPages())
            .current(products.getNumberOfElements())
            .build();
        ResponseSuccess response = ResponseSuccess.builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.getReasonPhrase())
            .data(productResponse)
            .page(pages)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ResponseSuccess> fetchById(@PathVariable UUID id){
        Product product = this.productService.fetchById(id);
        ProductResponseDto productToDto = product.convertToDto();

        ResponseSuccess response = ResponseSuccess.builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.getReasonPhrase())
            .data(productToDto)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/products")
    public ResponseEntity<ResponseSuccess> add(@RequestBody ProductRequestDto productRequestDto){
        Product convertToProduct = productRequestDto.convertToEntity();
        productService.add(convertToProduct, productRequestDto.getCategoryIds());

        ResponseSuccess response = ResponseSuccess.builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.getReasonPhrase())
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ResponseSuccess> update(@RequestBody ProductRequestDto productRequestDto, @PathVariable UUID id){
        Product convertToProduct = productRequestDto.convertToEntity();
        Product product = Product.builder()
            .id(id)
            .name(convertToProduct.getName())
            .description(convertToProduct.getDescription())
            .price(convertToProduct.getPrice())
            .stock(convertToProduct.getStock())
            .build();
        productService.update(product);

        ResponseSuccess response = ResponseSuccess.builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.getReasonPhrase())
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ResponseSuccess> deleteById(@PathVariable UUID id){
        productService.deleteById(id);

        ResponseSuccess response = ResponseSuccess.builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.getReasonPhrase())
            .build();

        return ResponseEntity.ok().body(response);
    }
}
