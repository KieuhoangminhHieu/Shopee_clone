package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.request.UserCreationRequest;
import com.devteria.identity_service.dto.request.UserUpdateRequest;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.entity.Role;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.UserMapper;
import com.devteria.identity_service.repository.RoleRepository;
import com.devteria.identity_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.springframework.security.test.context.support.WithMockUser;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;

    @BeforeEach
    void initData() {
        LocalDate dob = LocalDate.of(2000, 1, 1);

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

        // Mock mapper
        when(userMapper.toUser(any(UserCreationRequest.class))).thenReturn(user);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        // Mock passwordEncoder
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Mock roleRepository (không có role)
        when(roleRepository.findById(anyString())).thenReturn(Optional.empty());

        // Mock save
        when(userRepository.save(any())).thenReturn(user);
    }

    // ---------- CREATE USER ----------
    @Test
    void createUser_validRequest_success() {
        var response = userService.createUser(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("d426bb04a64c");
        assertThat(response.getUsername()).isEqualTo("hieuhoang2903");
    }

    @Test
    void createUser_userExisted_fail() {
        when(userRepository.save(any())).thenThrow(new DataIntegrityViolationException("duplicate"));

        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_EXISTED);
    }

    // ---------- GET USERS ----------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUsers_success() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        var result = userService.getUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("hieuhoang2903");
    }

    // ---------- GET USER BY ID ----------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUser_found_success() {
        when(userRepository.findById("d426bb04a64c")).thenReturn(Optional.of(user));

        var result = userService.getUser("d426bb04a64c");

        assertThat(result.getId()).isEqualTo("d426bb04a64c");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUser_notFound_fail() {
        when(userRepository.findById("not-exist")).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class, () -> userService.getUser("not-exist"));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    // ---------- GET MY INFO ----------
    @Test
    void getMyInfo_success() {
        // Mock SecurityContext
        SecurityContext context = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("hieuhoang2903");
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByUsername("hieuhoang2903")).thenReturn(Optional.of(user));

        var result = userService.getMyInfo();

        assertThat(result.getUsername()).isEqualTo("hieuhoang2903");
    }

    @Test
    void getMyInfo_notFound_fail() {
        SecurityContext context = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("unknown");
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    // ---------- UPDATE USER ----------
    @Test
    @WithMockUser(username = "hieuhoang2903")
    void updateUser_success() {
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .password("newpass123")
                .roles(List.of("ROLE_USER"))
                .build();

        when(userRepository.findById("d426bb04a64c")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleRepository.findAllById(any())).thenReturn(List.of());

        var result = userService.updateUser("d426bb04a64c", updateRequest);

        assertThat(result.getId()).isEqualTo("d426bb04a64c");
        verify(passwordEncoder).encode("newpass123");
    }


    @Test
    void updateUser_notFound_fail() {
        UserUpdateRequest updateRequest = UserUpdateRequest.builder().password("abc").build();

        when(userRepository.findById("not-exist")).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class,
                () -> userService.updateUser("not-exist", updateRequest));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    // ---------- DELETE USER ----------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteUser_success() {
        userService.deleteUser("d426bb04a64c");
        verify(userRepository, times(1)).deleteById("d426bb04a64c");
    }
}
