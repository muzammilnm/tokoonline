package com.tokoonline.demo.applicationuser;

import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tokoonline.demo.applicationuser.model.ApplicationUser;
import com.tokoonline.demo.authentication.model.UserPrincipal;
import com.tokoonline.demo.exceptionhandler.ApplicationUserNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationUserService implements UserDetailsService {
    
    private final ApplicationUserRepository applicationUserRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with " + email));
        log.info("user found is name : {} ", applicationUser.getFullname());
        return UserPrincipal.build(applicationUser);
    }

    @Transactional
    public ApplicationUser register(ApplicationUser applicationUser){
        ApplicationUser foundApplicationUser = applicationUserRepository.findByEmail(applicationUser.getEmail()).orElse(null);
        if(Objects.nonNull(foundApplicationUser)){
            throw new ApplicationUserAlreadyExistsException();
        }
        
        String encryptedPassword = new BCryptPasswordEncoder(12).encode(applicationUser.getPassword());
        ApplicationUser newApplicationUser = ApplicationUser.builder()
            .firstName(applicationUser.getFirstName())
            .lastName(applicationUser.getLastName())
            .fullname(applicationUser.getFirstName() + " " + applicationUser.getLastName())
            .username(applicationUser.getUsername())
            .email(applicationUser.getEmail())
            .password(encryptedPassword)
            .build();
        applicationUserRepository.save(newApplicationUser);

        return newApplicationUser;
    }

    public ApplicationUser update(ApplicationUser applicationUser){
        ApplicationUser foundApplicationUser = applicationUserRepository.findByEmail(applicationUser.getEmail()).orElseThrow(() -> new ApplicationUserNotFoundException());
        
        foundApplicationUser.setFirstName(applicationUser.getFirstName());
        foundApplicationUser.setLastName(applicationUser.getLastName());
        foundApplicationUser.setFullname(applicationUser.getFirstName() + " " + applicationUser.getLastName());
        foundApplicationUser.setUsername(applicationUser.getUsername());
        applicationUserRepository.save(foundApplicationUser);

        return foundApplicationUser;
    }

    public ApplicationUser getByEmail(String email){
        ApplicationUser applicationUser = applicationUserRepository.findByEmail(email).orElse(null);
        return applicationUser;
    }
    
}
