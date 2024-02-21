package com.tokoonline.demo.common;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tokoonline.demo.applicationuser.model.ApplicationUser;

@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class config implements AuditorAware<String>{
    
    @Bean
    AuditorAware<String> auditorProvider() {
        return new ApplicationUser();
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
            !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        ApplicationUser userPrincipal = (ApplicationUser) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.toString());
    }
}
