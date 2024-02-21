package com.tokoonline.demo.authentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "This token not valid")
public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException(){
        super("Token not valid");
    }
}
