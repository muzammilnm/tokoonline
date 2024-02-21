package com.tokoonline.demo.authentication.jwt;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tokoonline.demo.applicationuser.ApplicationUserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ApplicationUserService applicationUserService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = jwtProvider.getJwt(request);
        log.info("jwt : {} ", jwt);
        if(Objects.nonNull(jwt) && jwtProvider.isJwtTokenValid(jwt)){
            String email = jwtProvider.getEmailFromJwtToken(jwt);
            UserDetails userDetails = applicationUserService.loadUserByUsername(email);
            log.info("user details : {} ", userDetails);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            log.info("authentication token : {} ", authenticationToken);
            
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        log.info("authentication tahap 1");

        filterChain.doFilter(request, response);
        log.info("authentication tahap 2");
    }
    
}
