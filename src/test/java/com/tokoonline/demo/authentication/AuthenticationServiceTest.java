package com.tokoonline.demo.authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tokoonline.demo.applicationuser.ApplicationUserRepository;
import com.tokoonline.demo.applicationuser.ApplicationUserService;
import com.tokoonline.demo.applicationuser.model.ApplicationUser;
import com.tokoonline.demo.authentication.authentication.AuthenticationService;
import com.tokoonline.demo.authentication.exception.TokenNotValidException;
import com.tokoonline.demo.authentication.jwt.JwtProvider;
import com.tokoonline.demo.authentication.model.UserPrincipal;

import jakarta.servlet.ServletOutputStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private ApplicationUserService applicationUserService;
    
    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Test 
    void refreshToken_shouldReturnJwtResponseDto() throws Exception {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        // Mock ApplicationUser
        ApplicationUser applicationUser = ApplicationUser.builder()
            .username("john.doe")
            .firstName("John")
            .lastName("Doe")
            .email("johndoe@gmail.com")
            .password("secret")
            .roles(Collections.emptyList())
            .build();
        // Stub behavior for jwtProvider
        String jwtToken = "your_jwt_token_here";
        when(jwtProvider.getJwt(request)).thenReturn(jwtToken);
        when(jwtProvider.getEmailFromJwtToken(jwtToken)).thenReturn("johndoe@gmail.com");
        when(jwtProvider.isJwtTokenValid(jwtToken)).thenReturn(true);
        when(jwtProvider.generateJwtAccessToken(any(UserPrincipal.class))).thenReturn("access_token");
        when(jwtProvider.generateJwtRefreshToken(any(UserPrincipal.class))).thenReturn("refresh_token");
        // Stub behavior for applicationUserService
        when(applicationUserService.getByEmail("johndoe@gmail.com")).thenReturn(applicationUser);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        // Call the method under test
        AuthenticationService authenticationService = new AuthenticationService(jwtProvider, applicationUserService);
        authenticationService.refreshToken(request, response);

        // Verify response content
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(response).getOutputStream(); // Ensure getOutputStream is called
    }

    @Test 
    void refreshToken_shouldThrowErrorException_whenGivenJwtIsNull() throws Exception {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(jwtProvider.getJwt(request)).thenReturn(null);
       
        Assertions.assertThrows(TokenNotValidException.class, () -> authenticationService.refreshToken(request, response));
    }

    @Test 
    void refreshToken_shouldReturnJwtResponseDto_whenGivenEmailIsNotFound() throws Exception {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        // Stub behavior for jwtProvider
        String jwtToken = "your_jwt_token_here";
        when(jwtProvider.getJwt(request)).thenReturn(jwtToken);
        when(jwtProvider.getEmailFromJwtToken(jwtToken)).thenReturn(null);

        Assertions.assertThrows(TokenNotValidException.class, () -> authenticationService.refreshToken(request, response));
    }

    @Test 
    void refreshToken_shouldReturnJwtResponseDto_whenGivenApplicationUserIsNotFound() throws Exception {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        // Stub behavior for jwtProvider
        String jwtToken = "your_jwt_token_here";
        when(jwtProvider.getJwt(request)).thenReturn(jwtToken);
        when(jwtProvider.getEmailFromJwtToken(jwtToken)).thenReturn("johndoe@gmail.com");
        when(jwtProvider.isJwtTokenValid(jwtToken)).thenReturn(true);
        // Stub behavior for applicationUserService
        when(applicationUserService.getByEmail("johndoe@gmail.com")).thenReturn(null);

        Assertions.assertThrows(TokenNotValidException.class, () -> authenticationService.refreshToken(request, response));
    }

    @Test 
    void refreshToken_shouldReturnJwtResponseDto_whenGivenTokenIstokenIsExpired() throws Exception {
        // Mock HttpServletRequest and HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ApplicationUser applicationUser = ApplicationUser.builder()
            .username("john.doe")
            .firstName("John")
            .lastName("Doe")
            .email("johndoe@gmail.com")
            .password("secret")
            .build();
        // Stub behavior for jwtProvider
        String jwtToken = "your_jwt_token_here";
        when(jwtProvider.getJwt(request)).thenReturn(jwtToken);
        when(jwtProvider.getEmailFromJwtToken(jwtToken)).thenReturn("johndoe@gmail.com");
        when(jwtProvider.isJwtTokenValid(jwtToken)).thenReturn(false);

        Assertions.assertThrows(TokenNotValidException.class, () -> authenticationService.refreshToken(request, response));
    }
    
}
