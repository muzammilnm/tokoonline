package com.tokoonline.demo.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Customer is not found")
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(){
        super("Customer is not found");
    }
}
