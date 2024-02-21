package com.tokoonline.demo.authentication.model;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
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

    private UserPrincipal(UUID id, String firstName, String lastName, String username, String email, String password){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = Collections.emptyList();
    }

    public static UserPrincipal build(ApplicationUser applicationUser){
        return new UserPrincipal(applicationUser.getId(), applicationUser.getFirstName(), applicationUser.getLastName(), applicationUser.getUsername(), applicationUser.getEmail(), applicationUser.getPassword());
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
