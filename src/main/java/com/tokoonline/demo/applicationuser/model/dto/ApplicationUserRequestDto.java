package com.tokoonline.demo.applicationuser.model.dto;

import org.modelmapper.ModelMapper;

import com.tokoonline.demo.applicationuser.model.ApplicationUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUserRequestDto {
    @NotBlank(message = "FirstName can not be null")
    private String firstName;

    @NotBlank(message = "LastName can not be null")
    private String lastName;

    @NotBlank(message = "Email can not be null")
    @Email
    private String email;

    @NotBlank(message = "Username can not be null")
    private String username;

    @NotBlank(message = "Password can not be null")
    private String password;

    public ApplicationUser convertToEntity(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ApplicationUser.class);
    }
}
