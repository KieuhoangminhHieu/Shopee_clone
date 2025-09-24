package com.devteria.identity_service.controller;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.devteria.identity_service.dto.request.CategoryRequest;
import com.devteria.identity_service.entity.Category;
import com.devteria.identity_service.entity.Role;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.repository.CategoryRepository;
import com.devteria.identity_service.repository.RoleRepository;
import com.devteria.identity_service.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@Testcontainers
class CategoryControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper;

    private Category category;

    @BeforeEach
    void initData() {
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

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_success() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Thời trang");
        request.setDescription("Các sản phẩm thời trang");

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Thời trang"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Các sản phẩm thời trang"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCategories_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Điện tử"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCategoryById_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/" + category.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Điện tử"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_success() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Điện tử cập nhật");
        request.setDescription("Mô tả mới");

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.put("/categories/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Điện tử cập nhật"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Mô tả mới"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/" + category.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getCategoryById_invalid_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/invalid-id"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "USER")
    void createCategory_forbidden_fail() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Thể thao");
        request.setDescription("Các sản phẩm thể thao");

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
