package com.tokoonline.demo.authentication.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tokoonline.demo.applicationuser.model.ApplicationUser;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class UserPrincipal implements UserDetails {

    @Getter
    private UUID id;

    @Getter
    private String firstName;

    @Getter
    private String lastName;
    
    private String username;

    @Getter
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(UUID id, String firstName, String lastName, String username, String email, String password, Collection<? extends GrantedAuthority> authorities){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal build(ApplicationUser applicationUser){
        List<GrantedAuthority> authorities = applicationUser.getRoles().isEmpty() ? Collections.emptyList() : 
            applicationUser.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        return new UserPrincipal(applicationUser.getId(), applicationUser.getFirstName(), applicationUser.getLastName(), applicationUser.getUsername(), applicationUser.getEmail(), applicationUser.getPassword(), authorities);
    }
     
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
