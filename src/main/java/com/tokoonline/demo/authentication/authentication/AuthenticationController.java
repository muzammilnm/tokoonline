package com.tokoonline.demo.authentication.authentication;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.tokoonline.demo.applicationuser.ApplicationUserService;
import com.tokoonline.demo.applicationuser.model.ApplicationUser;
import com.tokoonline.demo.applicationuser.model.dto.ApplicationUserRequestDto;
import com.tokoonline.demo.applicationuser.model.dto.ApplicationUserResponseDto;
import com.tokoonline.demo.authentication.jwt.JwtProvider;
import com.tokoonline.demo.authentication.model.UserPrincipal;
import com.tokoonline.demo.authentication.model.dto.JwtResponseDto;
import com.tokoonline.demo.authentication.model.dto.LoginRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@SecurityScheme(
    name = "Authorization",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class AuthenticationController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ApplicationUserService applicationUserService;
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/registers")
    public ResponseEntity<ApplicationUserResponseDto> register(@Valid @RequestBody ApplicationUserRequestDto applicationUserRequestDto){
        ApplicationUser convertedApplicationUser = applicationUserRequestDto.convertToEntity();
        ApplicationUser newApplicationUser = applicationUserService.register(convertedApplicationUser);
        logger.info("Attempt register to system: {} ", newApplicationUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(newApplicationUser.convertToDto());
    }
    
    @PostMapping("/logins")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        logger.info("Attempt login to system: {}", loginRequestDto);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtProvider.generateJwtAccessToken(userPrincipal);
        String refreshToken = jwtProvider.generateJwtRefreshToken(userPrincipal);

        return ResponseEntity.ok(new JwtResponseDto(accessToken, refreshToken));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/refresh-tokens")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException{
        logger.info("Attempt to request refresh token: {}", request);
        authenticationService.refreshToken(request, response);
    }
}
