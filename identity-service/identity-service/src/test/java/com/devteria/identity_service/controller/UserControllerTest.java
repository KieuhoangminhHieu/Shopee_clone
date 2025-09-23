package com.devteria.identity_service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.devteria.identity_service.dto.request.UserCreationRequest;
import com.devteria.identity_service.dto.request.UserUpdateRequest;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
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
        dob = LocalDate.of(2003, 3, 3);
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
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);
        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("d426bb04a64c"));
    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        request.setUsername("hi");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1002"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Tên người dùng phải có ít nhất 3 ký tự"));
    }

    @Test
    void createUser_passwordInvalid_fail() throws Exception {
        request.setPassword("123456");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1003"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Mật khẩu phải có ít nhất 8 ký tự"));
    }

    @Test
    void createUser_emailInvalid_fail() throws Exception {
        request.setEmail("hi");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1012"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Email không hợp lệ"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsers_success() throws Exception {
        Mockito.when(userService.getUsers()).thenReturn(List.of(userResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("result[0].username").value("hieuhoang2903"));
    }

    @Test
    @WithMockUser(username = "hieuhoang2903")
    void getUser_success() throws Exception {
        Mockito.when(userService.getUser("d426bb04a64c")).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/d426bb04a64c"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("hieuhoang2903"));
    }

    @Test
    @WithMockUser(username = "hieuhoang2903")
    void updateUser_success() throws Exception {
        var updateRequest = new UserUpdateRequest();
        updateRequest.setUsername("hieuhoang2903");
        updateRequest.setRoles(List.of("ROLE_USER"));

        String content = objectMapper.writeValueAsString(updateRequest);

        Mockito.when(userService.updateUser((Mockito.eq("d426bb04a64c")), ArgumentMatchers.any()))
                .thenReturn(userResponse);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/d426bb04a64c")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("hieuhoang2903"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_success() throws Exception {
        Mockito.doNothing().when(userService).deleteUser("d426bb04a64c");

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/d426bb04a64c"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"));
    }

    @Test
    @WithMockUser(username = "hieuhoang2903")
    void getMyInfo_success() throws Exception {
        Mockito.when(userService.getMyInfo()).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/myInfo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("hieuhoang2903"));
    }
}
