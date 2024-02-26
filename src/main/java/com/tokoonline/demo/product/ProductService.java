package com.tokoonline.demo.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tokoonline.demo.product.model.Product;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    
    private final ProductRepostitory productRepostitory;

    public List<Product> fetchAll(){
        return productRepostitory.findAll();
    }
}
