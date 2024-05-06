package com.tokoonline.demo.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Category is not found")
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(){
        super("Category is not found");
    }
}


