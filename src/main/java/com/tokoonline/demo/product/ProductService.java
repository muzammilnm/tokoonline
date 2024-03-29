package com.tokoonline.demo.product;

import java.util.List;
import java.util.UUID;

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

    public Product fetchById(UUID id){
        return productRepostitory.findById(id).orElseThrow(() -> new ProductNotFoundException());
    }

    public Product add(Product product){
        Product newProduct = Product.builder() 
            .name(product.getName())
            .desctription(product.getDesctription())
            .price(product.getPrice())
            .stock(product.getStock())
            .build();

        return productRepostitory.save(newProduct);
    }

    public Product update(Product product){
        Product foundProduct = productRepostitory.findById(product.getId()).orElseThrow(() -> new ProductNotFoundException());

        foundProduct.setName(product.getName());
        foundProduct.setDesctription(product.getDesctription());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setStock(product.getStock());
        return productRepostitory.save(foundProduct);
    }

    public Boolean deleteById(UUID id){
        productRepostitory.findById(id).orElseThrow(() -> new ProductNotFoundException());
        productRepostitory.deleteById(id);
        return true;
    }
}
