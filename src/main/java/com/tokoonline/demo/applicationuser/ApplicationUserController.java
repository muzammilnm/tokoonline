package com.tokoonline.demo.applicationuser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tokoonline.demo.applicationuser.model.ApplicationUser;
import com.tokoonline.demo.applicationuser.model.dto.ApplicationUserResponseDto;
import com.tokoonline.demo.applicationuser.model.dto.ApplicationUserUpdateRequestDto;
import com.tokoonline.demo.authentication.authentication.AuthenticationController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    
    @PatchMapping("/application-users/{email}")
    public ResponseEntity<ApplicationUserResponseDto> update(@Valid @RequestBody ApplicationUserUpdateRequestDto applicationUserUpdateRequestDto, @PathVariable String email){
        logger.info("Attemp update application user: {}", applicationUserUpdateRequestDto);

        ApplicationUser convertApplicationUser = applicationUserUpdateRequestDto.convertToEntity();
        ApplicationUser applicationUser = ApplicationUser.builder()
            .firstName(convertApplicationUser.getFirstName())
            .lastName(convertApplicationUser.getLastName())
            .username(convertApplicationUser.getUsername())
            .email(email)
            .username(convertApplicationUser.getUsername())
            .build();
        ApplicationUser applicationUserSaved = applicationUserService.update(applicationUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(applicationUserSaved.convertToDto());
    }
}
