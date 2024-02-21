package com.tokoonline.demo.applicationuser;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This application user is already")
public class ApplicationUserAlreadyExistsException extends RuntimeException {
    public ApplicationUserAlreadyExistsException(){
        super("Application user is already");
    }
}
