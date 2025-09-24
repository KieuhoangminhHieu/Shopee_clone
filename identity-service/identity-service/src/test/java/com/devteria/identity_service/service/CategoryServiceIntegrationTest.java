package com.devteria.identity_service.service;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.devteria.identity_service.dto.request.CategoryRequest;
import com.devteria.identity_service.dto.response.CategoryResponse;
import com.devteria.identity_service.entity.Category;
import com.devteria.identity_service.entity.Role;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.repository.CategoryRepository;
import com.devteria.identity_service.repository.RoleRepository;
import com.devteria.identity_service.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Testcontainers
@Slf4j
class CategoryServiceIntegrationTest {

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    Category category;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = Role.builder().name("ROLE_ADMIN").description("Admin role").build();
        roleRepository.save(role);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setEmail("admin@gmail.com");
        admin.setRoles(Set.of(role));
        userRepository.save(admin);

        category = Category.builder()
                .name("Điện tử")
                .description("Các sản phẩm điện tử")
                .build();
        category = categoryRepository.save(category);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_success() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Thời trang");
        request.setDescription("Các sản phẩm thời trang");

        CategoryResponse response = categoryService.createCategory(request);

        Assertions.assertThat(response.getName()).isEqualTo("Thời trang");
        Assertions.assertThat(response.getDescription()).isEqualTo("Các sản phẩm thời trang");
    }

    @Test
    void getAllCategories_success() {
        List<CategoryResponse> responses = categoryService.getAllCategories();

        Assertions.assertThat(responses).isNotEmpty();
        Assertions.assertThat(responses.getFirst().getName()).isEqualTo("Điện tử");
    }

    @Test
    void getCategoryById_success() {
        CategoryResponse response = categoryService.getCategoryById(category.getId());

        Assertions.assertThat(response.getName()).isEqualTo("Điện tử");
    }

    @Test
    void getCategoryById_invalid_fail() {
        Assertions.assertThatThrownBy(() -> categoryService.getCategoryById("invalid-id"))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.INVALID_KEY.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_success() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Điện tử cập nhật");
        request.setDescription("Mô tả mới");

        CategoryResponse response = categoryService.updateCategory(category.getId(), request);

        Assertions.assertThat(response.getName()).isEqualTo("Điện tử cập nhật");
        Assertions.assertThat(response.getDescription()).isEqualTo("Mô tả mới");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_invalid_fail() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Tên mới");
        request.setDescription("Mô tả mới");

        Assertions.assertThatThrownBy(() -> categoryService.updateCategory("invalid-id", request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.INVALID_KEY.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_success() {
        categoryService.deleteCategory(category.getId());

        Assertions.assertThat(categoryRepository.findById(category.getId())).isEmpty();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_invalid_fail() {
        Assertions.assertThatThrownBy(() -> categoryService.deleteCategory("invalid-id"))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.INVALID_KEY.getMessage());
    }
}
