package com.devteria.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.devteria.identity_service.constant.PredefinedRole;
import com.devteria.identity_service.dto.request.UserCreationRequest;
import com.devteria.identity_service.dto.response.UserResponse;
import com.devteria.identity_service.entity.Role;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.mapper.UserMapper;
import com.devteria.identity_service.repository.RoleRepository;
import com.devteria.identity_service.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@Testcontainers
class UserServiceIntegrationTest {
    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserMapper userMapper;

    private UserCreationRequest request;
    User user;

    @BeforeEach
    void initData() {
        Role role = Role.builder()
                .name(PredefinedRole.USER_ROLE)
                .description("Default role")
                .build();
        roleRepository.save(role);

        request = UserCreationRequest.builder()
                .firstName("Hiếu")
                .lastName("Hoàng")
                .username("hieuhoang2903")
                .password("12345678")
                .dob(LocalDate.of(2003, 3, 3))
                .email("hieuhoang@gmail.com")
                .phoneNumber("0987654321")
                .address("Hà Nội")
                .build();

        user = userMapper.toUser(request);
        user.setRoles(Set.of(role));
        user = userRepository.save(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsers_validRequest_success() {
        List<UserResponse> responses = userService.getUsers();
        Assertions.assertThat(responses.getFirst().getUsername()).isEqualTo("hieuhoang2903");
    }

    @Test
    @WithMockUser(username = "hieuhoang2903")
    void getMyInfo_validRequest_success() {
        UserResponse response = userService.getMyInfo();
        Assertions.assertThat(response.getUsername()).isEqualTo("hieuhoang2903");
        Assertions.assertThat(response.getEmail()).isEqualTo("hieuhoang@gmail.com");
    }

    @Test
    @WithMockUser(username = "notfounduser")
    void getMyInfo_userNotFound_error() {
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
    }

    @Test
    @WithMockUser(username = "hieuhoang2903", roles = "ADMIN")
    void getUserById_validRequest_success() {
        UserResponse response = userService.getUser(user.getId());
        Assertions.assertThat(response.getUsername()).isEqualTo("hieuhoang2903");
        Assertions.assertThat(response.getEmail()).isEqualTo("hieuhoang@gmail.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsers_validRequest_fail() {
        var exception = assertThrows(AppException.class, () -> userService.getUser("1231312312"));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
        Assertions.assertThat(exception.getMessage()).isEqualTo("Không tìm thấy người dùng");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_validRequest_success() {
        userService.deleteUser(user.getId());
        Assertions.assertThat(userRepository.findById(user.getUsername())).isEmpty();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        request.setUsername("hieuhoang2904");
        // WHEN
        UserResponse response = userService.createUser(request);
        // THEN
        Assertions.assertThat(response.getUsername()).isEqualTo("hieuhoang2904");
        Assertions.assertThat(response.getFirstName()).isEqualTo("Hiếu");
    }

    @Test
    void createUser_validRequest_fail() {
        // GIVEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        // WHEN,THEN
        Assertions.assertThat(exception.getMessage()).isEqualTo("Người dùng đã tồn tại");
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }
}
