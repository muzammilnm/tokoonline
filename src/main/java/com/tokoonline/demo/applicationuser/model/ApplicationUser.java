package com.tokoonline.demo.applicationuser.model;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.AuditorAware;

import com.tokoonline.demo.applicationuser.model.dto.ApplicationUserResponseDto;
import com.tokoonline.demo.common.AuditModel;
import com.tokoonline.demo.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser extends AuditModel implements AuditorAware<String> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String fullName;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @ManyToMany 
    @JoinTable( 
        name = "users_roles", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Collection<Role> roles;

    public ApplicationUserResponseDto convertToDto(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ApplicationUserResponseDto.class);
    }

    @Override
    public Optional<String> getCurrentAuditor() {
       return Optional.of(this.toString());
    }

}
