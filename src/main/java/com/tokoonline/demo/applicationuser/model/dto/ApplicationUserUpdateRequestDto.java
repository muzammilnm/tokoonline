package com.tokoonline.demo.applicationuser.model.dto;

import org.modelmapper.ModelMapper;

import com.tokoonline.demo.applicationuser.model.ApplicationUser;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUserUpdateRequestDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    public ApplicationUser convertToEntity(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ApplicationUser.class);
    }
}
