package com.devteria.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import com.devteria.identity_service.dto.request.UserCreationRequest;
import com.devteria.identity_service.dto.request.UserUpdateRequest;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.repository.UserRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

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

        user = User.builder()
                .id("d426bb04a64c")
                .firstName("Hiếu")
                .lastName("Hoàng")
                .username("hieuhoang2903")
                .dob(dob)
                .email("hieuhoang@gmail.com")
                .phoneNumber("0987654321")
                .address("Hà Nội")
                .build();
    }

    @Transactional
    @Test
    void createUser_validRequest_success() {
        // GIVEN
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        // WHEN
        var response = userService.createUser(request);
        // THEN
        Assertions.assertThat(response.getId()).isEqualTo("d426bb04a64c");
        Assertions.assertThat(response.getUsername()).isEqualTo("hieuhoang2903");
    }

    @Transactional
    @Test
    void createUser_validRequest_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    @Test
    @WithMockUser(username = "hieuhoang2903")
    void getMyInfo_valid_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var response = userService.getMyInfo();

        Assertions.assertThat(response.getUsername()).isEqualTo("hieuhoang2903");
        Assertions.assertThat(response.getId()).isEqualTo("d426bb04a64c");
    }

    @Test
    @WithMockUser(username = "hieuhoang2903")
    void getMyInfo_userNotFound_error() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsers_valid_success() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        var responses = userService.getUsers();

        Assertions.assertThat(responses).hasSize(1);
        Assertions.assertThat(responses.get(0).getUsername()).isEqualTo("hieuhoang2903");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUser_valid_success() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        var response = userService.getUser("d426bb04a64c");

        Assertions.assertThat(response.getUsername()).isEqualTo("hieuhoang2903");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUser_notFound_error() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class, () -> userService.getUser("invalid-id"));

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
    }

    @Test
    @WithMockUser(username = "hieuhoang2903")
    void updateUser_userNotFound_error() {
        var updateRequest = new UserUpdateRequest();
        updateRequest.setPassword("newpass123");

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class, () -> userService.updateUser("invalid-id", updateRequest));

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_valid_success() {
        doNothing().when(userRepository).deleteById(anyString());

        userService.deleteUser("d426bb04a64c");

        verify(userRepository, times(1)).deleteById("d426bb04a64c");
    }
}
