package com.tokoonline.demo.product;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tokoonline.demo.product.model.Product;

public interface ProductRepostitory extends JpaRepository<Product, UUID> {

}
