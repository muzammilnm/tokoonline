package com.tokoonline.demo.applicationuser;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tokoonline.demo.applicationuser.model.ApplicationUser;
import com.tokoonline.demo.exceptionhandler.ApplicationUserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserServiceTest {
    @InjectMocks
    private ApplicationUserService applicationUserService;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Test
    void loadUserByUsername_expectReturnUserPrincipal_whenUsernameIsFound(){
        ApplicationUser existingApplicationUser = ApplicationUser.builder().username("john.doe").firstName("john").lastName("doe").email("johndoe@gmail.com").password("secret").build();
        existingApplicationUser.setId(UUID.randomUUID());
        Mockito.when(applicationUserRepository.findByEmail(existingApplicationUser.getEmail())).thenReturn(Optional.of(existingApplicationUser));

        UserDetails userDetails = applicationUserService.loadUserByUsername(existingApplicationUser.getEmail());

        Assertions.assertEquals(existingApplicationUser.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(existingApplicationUser.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_expectThrowException_whenUsernameIsNotFound(){
        Mockito.when(applicationUserRepository.findByEmail("email@gmail.com")).thenReturn(Optional.empty());

        Executable loadByUsernameAction = () -> applicationUserService.loadUserByUsername("email@gmail.com");

        Assertions.assertThrows(UsernameNotFoundException.class, loadByUsernameAction);
    }

    @Test
    void register_shouldReturnApplicationUser_whenGivenNewApplicationUserNotFound() throws Exception{
        ApplicationUser newApplicationUser = ApplicationUser.builder().username("john.doe").firstName("john").lastName("doe").email("johndoe@gmail.com").password("secret").build();
        Mockito.when(applicationUserRepository.findByEmail(newApplicationUser.getEmail())).thenReturn(Optional.empty());
    
        ApplicationUser applicationUser = applicationUserService.register(newApplicationUser);

        Assertions.assertEquals(newApplicationUser.getFirstName(), applicationUser.getFirstName());
        Assertions.assertEquals(newApplicationUser.getLastName(), applicationUser.getLastName());
        Assertions.assertEquals(newApplicationUser.getEmail(), applicationUser.getEmail());
    }

    @Test
    void register_shouldThrowErrorException_whenGivenApplicationUserIsAlreadyExists() throws Exception{
        ApplicationUser newApplicationUser = ApplicationUser.builder().username("john.doe").firstName("john").lastName("doe").email("johndoe@gmail.com").password("secret").build();
        ApplicationUser expectedResult = ApplicationUser.builder().username("john.doe").firstName("john").lastName("doe").email("johndoe@gmail.com").password("secret").build();
        Mockito.when(applicationUserRepository.findByEmail(newApplicationUser.getEmail())).thenReturn(Optional.of(expectedResult));

        Assertions.assertThrows(ApplicationUserAlreadyExistsException.class, () -> applicationUserService.register(newApplicationUser));
    }

    @Test
    void update_shouldReturnNewApplicationUser_whenGivenUpdateIsSuccess() throws Exception{
        ApplicationUser newApplicationUser = ApplicationUser.builder().username("john.doe").firstName("john").lastName("doe").email("johndoe@gmail.com").password("secret").build();
        ApplicationUser expectedResult = ApplicationUser.builder().username("alma zahra").firstName("alma").lastName("zahra").email("johndoe@gmail.com").password("secret").build();
        Mockito.when(applicationUserRepository.findByEmail(newApplicationUser.getEmail())).thenReturn(Optional.of(newApplicationUser));

        ApplicationUser actualResult = applicationUserService.update(expectedResult);

        Assertions.assertEquals(expectedResult.getUsername(), actualResult.getUsername());
        Assertions.assertEquals(expectedResult.getFirstName(), actualResult.getFirstName());
        Assertions.assertEquals(expectedResult.getLastName(), actualResult.getLastName());
    }

    @Test
    void update_shouldThrowErrorException_whenGivenApplicationUserIsANotFound() throws Exception{
        ApplicationUser newApplicationUser = ApplicationUser.builder().username("john.doe").firstName("john").lastName("doe").email("johndoe@gmail.com").password("secret").build();
        Mockito.when(applicationUserRepository.findByEmail(newApplicationUser.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(ApplicationUserNotFoundException.class, () -> applicationUserService.update(newApplicationUser));
    }

    @Test
    void getByEmail_shouldReturnApplicationUser_whenGivenEmailIsFoundInAppliacationUser() throws Exception{
        ApplicationUser applicationUser = ApplicationUser.builder().username("john.doe").firstName("john").lastName("doe").email("johndoe@gmail.com").password("secret").build();
        Mockito.when(applicationUserRepository.findByEmail(applicationUser.getEmail())).thenReturn(Optional.of(applicationUser));

        ApplicationUser actualResult = applicationUserService.getByEmail(applicationUser.getEmail());

        Assertions.assertEquals(applicationUser.getUsername(), actualResult.getUsername());
        Assertions.assertEquals(applicationUser.getFirstName(), actualResult.getFirstName());
        Assertions.assertEquals(applicationUser.getLastName(), actualResult.getLastName());
    }

    @Test
    void getByEmail_shouldReturnNull_whenGivenEmailIsNotFoundInAppliacationUser() throws Exception{
        Mockito.when(applicationUserRepository.findByEmail("example@gmail.com")).thenReturn(Optional.empty());

        ApplicationUser actualResult = applicationUserService.getByEmail("example@gmail.com");

        Assertions.assertEquals(null, actualResult);
    }
}
