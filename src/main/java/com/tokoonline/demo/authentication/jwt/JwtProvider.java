package com.tokoonline.demo.authentication.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.tokoonline.demo.authentication.model.UserPrincipal;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${simple.app.jwt.secret}")
    private String jwtSecret;

    @Value("${simple.app.jwt.access.expiration}")
    private Long jwtAccessExpiration;

    @Value("${simple.app.jwt.refresh.expiration}")
    private Long jwtRefreshExpiration;

    public String generateJwtAccessToken(UserPrincipal userPrincipal){
        Map<String, Object> userInformation = new HashMap<>();
        userInformation.put("firstName", userPrincipal.getFirstName());
        userInformation.put("lastName", userPrincipal.getLastName());
        userInformation.put("username", userPrincipal.getUsername());

        return Jwts.builder().claims(userInformation).subject(userPrincipal.getEmail()).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + jwtAccessExpiration))
            .signWith(this.generateKey()).compact();
    }

    public String generateJwtRefreshToken(UserPrincipal userPrincipal){
        return Jwts.builder().subject(userPrincipal.getEmail()).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
            .signWith(this.generateKey()).compact();
    }

    public String getEmailFromJwtToken(String token){
        return Jwts.parser().verifyWith(this.generateSecretKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public Boolean isJwtTokenValid(String token){
        try {
            Jwts.parser().verifyWith(this.generateSecretKey()).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException exception) {
            logger.error("Invalid JWT signature -> Message: {0} ", exception);
        } catch (MalformedJwtException exception) {
            logger.error("Invalid JWT token -> Message: {0} ", exception);
        } catch (ExpiredJwtException exception) {
            logger.info("Expired JWT token -> Message: {0} ", exception);
        } catch (UnsupportedJwtException exception) {
            logger.error("Unsupported JWT token -> Message: {0} ", exception);
        } catch (IllegalArgumentException exception) {
            logger.error("JWT claims string is empty -> Message: {0} ", exception);
        }

        return false;
    }

    private Key generateKey(){
        byte[] bytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    private SecretKey generateSecretKey(){
        return Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String getJwt(HttpServletRequest request){
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
            logger.info("token : {}", authorizationHeader.replace("Bearer ", ""));
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
