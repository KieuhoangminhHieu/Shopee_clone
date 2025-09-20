package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.request.UserCreationRequest;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate dob;

    private ObjectMapper objectMapper;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2000, 1, 1); // đủ 18 tuổi
        request = UserCreationRequest.builder()
                .firstName("Hiếu")
                .lastName("Hoàng")
                .username("hieuhoang2903")
                .password("12345678")
                .dob(dob)
                .email("hieuhoang@gmail.com")
                .phoneNumber("0987654321")
                .address("Hà Nội")
                .build();

        userResponse = UserResponse.builder()
                .id("d426bb04a64c")
                .firstName("Hiếu")
                .lastName("Hoàng")
                .username("hieuhoang2903")
                .dob(dob)
                .email("hieuhoang@gmail.com")
                .phoneNumber("0987654321")
                .address("Hà Nội")
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(200))
                .andExpect(jsonPath("result.username").value("hieuhoang2903"));
    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        request.setUsername("hi"); // < 3 ký tự

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(1002))
                .andExpect(jsonPath("message").value("Username must be at least 3 characters"));
    }

    @Test
    void createUser_passwordInvalid_fail() throws Exception {
        request.setPassword("123456"); // < 8 ký tự

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(1003))
                .andExpect(jsonPath("message").value("Password must be at least 8 characters"));
    }

    @Test
    void createUser_dobInvalid_fail() throws Exception {
        request.setDob(LocalDate.now().minusYears(10)); // < 18 tuổi

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(1011))
                .andExpect(jsonPath("message").value("Your age must be at least 18"));
    }

    @Test
    void createUser_emailInvalid_fail() throws Exception {
        request.setEmail("invalid-email"); // sai format

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(1012))
                .andExpect(jsonPath("message").value("INVALID_EMAIL"));
    }

    @Test
    void createUser_phoneNumberInvalid_fail() throws Exception {
        request.setPhoneNumber("123"); // < 10 ký tự

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(1013))
                .andExpect(jsonPath("message").value("INVALID_PHONE_NUMBER"));
    }
}
