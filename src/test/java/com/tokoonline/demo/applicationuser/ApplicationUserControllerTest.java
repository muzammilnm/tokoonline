package com.tokoonline.demo.applicationuser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokoonline.demo.applicationuser.model.ApplicationUser;
import com.tokoonline.demo.applicationuser.model.dto.ApplicationUserResponseDto;
import com.tokoonline.demo.applicationuser.model.dto.ApplicationUserUpdateRequestDto;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @BeforeEach
    void setup(){
        ApplicationUser applicationUser = ApplicationUser.builder()
            .firstName("john")
            .lastName("doe")
            .username("johndoe")
            .email("johndoe@gmail.com")
            .build();
        applicationUserRepository.save(applicationUser);
    }

    @AfterEach
    public void cleanup() {
        this.applicationUserRepository.deleteAll();
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void update_shouldReturnStatusIsCreated_whenGivenUpdateApplicationUserIsSuccess() throws Exception{
        ApplicationUserUpdateRequestDto requestDto = ApplicationUserUpdateRequestDto.builder()
            .firstName("alma")
            .lastName("zahra")
            .username("alma zahra 123")
            .build();
        String requestDtoString = objectMapper.writeValueAsString(requestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/application-users/{email}", "johndoe@gmail.com")
        .contentType(MediaType.APPLICATION_JSON)    
        .content(requestDtoString)
        .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        ApplicationUserResponseDto responseResult = objectMapper.readValue(responseString, ApplicationUserResponseDto.class);
        ApplicationUser actualResult = applicationUserRepository.findByEmail(responseResult.getEmail()).get();

        Assertions.assertEquals(requestDto.getFirstName(), actualResult.getFirstName());
        Assertions.assertEquals(requestDto.getLastName(), actualResult.getLastName());
        Assertions.assertEquals(requestDto.getUsername(), actualResult.getUsername());
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void update_shouldReturnThrowErrorResponse_whenGiven() throws Exception{
        ApplicationUserUpdateRequestDto requestDto = ApplicationUserUpdateRequestDto.builder()
            .firstName("")
            .lastName("zahra")
            .username("alma zahra 123")
            .build();
        String requestDtoString = objectMapper.writeValueAsString(requestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/application-users/{email}", "johndoe@gmail.com")
        .contentType(MediaType.APPLICATION_JSON)    
        .content(requestDtoString)
        .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
