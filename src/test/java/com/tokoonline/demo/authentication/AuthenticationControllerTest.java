package com.tokoonline.demo.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokoonline.demo.applicationuser.ApplicationUserRepository;
import com.tokoonline.demo.applicationuser.model.ApplicationUser;
import com.tokoonline.demo.applicationuser.model.dto.ApplicationUserRequestDto;
import com.tokoonline.demo.applicationuser.model.dto.ApplicationUserResponseDto;
import com.tokoonline.demo.authentication.model.dto.JwtResponseDto;
import com.tokoonline.demo.authentication.model.dto.LoginRequestDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @AfterEach
    public void cleanUp(){
        applicationUserRepository.deleteAll();
    }

    @Test 
    void login_shouldReturnValidTokenAndHttpStatusOk_whenLoginIsSuccess() throws Exception{
        ApplicationUser applicationUser = ApplicationUser.builder()
            .firstName("john")
            .lastName("doe")
            .username("johndoe")
            .email("johndoe@gmail.com").password(passwordEncoder.encode("P@ssw0rd"))
            .build();
        applicationUserRepository.save(applicationUser);
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("johndoe@gmail.com").password("P@ssw0rd").build();
        String loginRequestString = objectMapper.writeValueAsString(loginRequestDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/logins").content(loginRequestString)
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void login_shouldReturnHttpStatus401Unauthorized_whenUserNotFoundAndOrPasswordAreInvalid() throws Exception{
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("johndoe@gmail.com").password("P@ssw0rd").build();
        String loginRequestString = objectMapper.writeValueAsString(loginRequestDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/logins")
            .content(loginRequestString)
            .contentType(MediaType.APPLICATION_JSON);

            mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void register_shouldReturnEmailAndHttpStatusOk_whenRegisterIsSuccess() throws Exception{
        ApplicationUserRequestDto requestDto = ApplicationUserRequestDto.builder()
            .firstName("john")
            .lastName("doe")
            .username("john doe")
            .email("johndoe@gmail.com")
            .password("P@ssw0rd")
            .build();
        String requestDtoString = objectMapper.writeValueAsString(requestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registers")
            .content(requestDtoString)
            .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        String responseString = result.getResponse().getContentAsString();
        
        ApplicationUserResponseDto actualResult = objectMapper.readValue(responseString, ApplicationUserResponseDto.class);
        ApplicationUser applicationUser = applicationUserRepository.findByEmail(actualResult.getEmail()).get();
        ApplicationUserResponseDto expectedResult = applicationUser.convertToDto();

        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedResult.getEmail(), actualResult.getEmail());
    }

    @Test
    void register_shouldReturnThrowErrorResponse_whenGivenEmailIsAlreadyExists() throws Exception{
        ApplicationUser applicationUser = ApplicationUser.builder()
            .firstName("john")
            .lastName("doe")
            .username("john doe")
            .email("johndoe@gmail.com")
            .password(passwordEncoder.encode("P@ssw0rd"))
            .build();
        applicationUserRepository.save(applicationUser);
        ApplicationUserRequestDto requestDto = ApplicationUserRequestDto.builder()
            .firstName("john")
            .lastName("doe")
            .username("john doe")
            .email("johndoe@gmail.com")
            .password("P@ssw0rd")
            .build();
        String requestDtoString = objectMapper.writeValueAsString(requestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registers")
            .content(requestDtoString)
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void register_shouldReturnThrowErrorResponse_whenGivenFirstNameOrLastNameOrOrUsernameEmailOrPasswordIsBlank() throws Exception{
        ApplicationUserRequestDto requestDto = ApplicationUserRequestDto.builder()
            .lastName("")
            .username("")
            .email("johndoe@gmail.com")
            .password("P@ssw0rd")
            .build();
        String requestDtoString = objectMapper.writeValueAsString(requestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registers")
            .content(requestDtoString)
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            // .andExpect(MockMvcResultMatchers.jsonPath("$lastName", Is.is("LastName can not be null")))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void refreshToken_shouldReturnAccessTokenAndRefreshToken_whenGivenRefreshTokenIsSuccess() throws Exception{
        String secret = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        SecretKey generateSecretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder().subject("johndoe@gmail.com").issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 600000))
            .signWith(generateSecretKey).compact();
        ApplicationUser applicationUser = ApplicationUser.builder()
            .firstName("john")
            .lastName("doe")
            .username("john doe")
            .email("johndoe@gmail.com")
            .password(passwordEncoder.encode("P@ssw0rd"))
            .build();
        applicationUserRepository.save(applicationUser);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/refresh-tokens")
            .header("Authorization", "Bearer " + token);
        
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String responseString = result.getResponse().getContentAsString();
        JwtResponseDto actualResult = objectMapper.readValue(responseString, JwtResponseDto.class);
    
        Assertions.assertNotNull(actualResult.getAccessToken());
        Assertions.assertNotNull(actualResult.getRefreshToken());
    }

    @Test
    void refreshToken_shouldThrowErrorResponse_whenGivenEmailIsNotFound() throws Exception{
        String secret = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        SecretKey generateSecretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder().subject("jodoh@gmail.com").issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 600000))
            .signWith(generateSecretKey).compact();
        ApplicationUser applicationUser = ApplicationUser.builder()
            .firstName("john")
            .lastName("doe")
            .username("john doe")
            .email("johndoe@gmail.com")
            .password(passwordEncoder.encode("P@ssw0rd"))
            .build();
        applicationUserRepository.save(applicationUser);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/refresh-tokens")
            .header("Authorization", "BearerToken " + token);
        
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
