package com.tokoonline.demo.authentication.authentication;

import java.io.IOException;
import java.util.Objects;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokoonline.demo.applicationuser.ApplicationUserService;
import com.tokoonline.demo.applicationuser.model.ApplicationUser;
import com.tokoonline.demo.authentication.exception.TokenNotValidException;
import com.tokoonline.demo.authentication.jwt.JwtProvider;
import com.tokoonline.demo.authentication.model.UserPrincipal;
import com.tokoonline.demo.authentication.model.dto.JwtResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtProvider jwtProvider;
    private final ApplicationUserService applicationUserService;
    
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException{
        String jwt = jwtProvider.getJwt(request);
        if(Objects.isNull(jwt)){
            throw new TokenNotValidException();
        }

        final String email = jwtProvider.getEmailFromJwtToken(jwt);
        if(Objects.isNull(email) || !jwtProvider.isJwtTokenValid(jwt)){  
            throw new TokenNotValidException();
        }

        ApplicationUser applicationUser = applicationUserService.getByEmail(email);
        if(Objects.isNull(applicationUser)){
            throw new TokenNotValidException();
        }

        UserPrincipal userPrincipal = UserPrincipal.build(applicationUser);
        String accessToken = jwtProvider.generateJwtAccessToken(userPrincipal);
        String newRefreshToken = jwtProvider.generateJwtRefreshToken(userPrincipal);

        JwtResponseDto authResponse = new JwtResponseDto(accessToken, newRefreshToken);
        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }
}
