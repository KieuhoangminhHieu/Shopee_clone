package com.devteria.identity_service.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import org.assertj.core.api.Assertions;
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
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.devteria.identity_service.dto.request.ProductRequest;
import com.devteria.identity_service.entity.*;
import com.devteria.identity_service.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Slf4j
class ProductControllerIntegrationTest {

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
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    User owner;
    Shop shop;
    Category category;
    Product product;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        shopRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = new Role();
        role.setName("ROLE_USER");
        role.setDescription("User role");
        roleRepository.save(role);

        owner = new User();
        owner.setUsername("shopowner");
        owner.setPassword("12345678");
        owner.setEmail("owner@gmail.com");
        owner.setDob(LocalDate.of(2000, 1, 1));
        owner.setPhoneNumber("0909090909");
        owner.setAddress("Hà Nội");
        owner.setRoles(Set.of(role));
        owner = userRepository.save(owner);

        shop = new Shop();
        shop.setShopName("Kiều Mart");
        shop.setDescription("Shop của Kiều");
        shop.setUserId(owner.getId());
        shop.setAddress("Hà Nội");
        shop = shopRepository.save(shop);

        category = new Category();
        category.setName("Thời trang");
        category.setDescription("Các sản phẩm thời trang");
        category = categoryRepository.save(category);

        product = new Product();
        product.setName("Áo thun");
        product.setDescription("Áo thun cotton");
        product.setPrice(new BigDecimal("199000"));
        product.setShop(shop);
        product.setCategory(category);
        product = productRepository.save(product);

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void createProduct_success() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Quần jean");
        request.setDescription("Quần jean xanh");
        request.setPrice(299000.0);
        request.setShopId(shop.getId());
        request.setCategoryId(category.getId());

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Quần jean"))
                .andExpect(jsonPath("$.description").value("Quần jean xanh"))
                .andExpect(jsonPath("$.price").value(299000));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllProducts_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Áo thun"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getProductById_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Áo thun"));
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void updateProduct_success() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Áo thun cập nhật");
        request.setDescription("Mô tả mới");
        request.setPrice(249000.0);
        request.setShopId(shop.getId());
        request.setCategoryId(category.getId());

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Áo thun cập nhật"))
                .andExpect(jsonPath("$.price").value(249000));
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void deleteProduct_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + product.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertThat(productRepository.findById(product.getId())).isEmpty();
    }

    @Test
    void getProductById_invalid_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/invalid-id")).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    void createProduct_unauthorized_fail() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Giày thể thao");
        request.setDescription("Giày chạy bộ");
        request.setPrice(499000.0);
        request.setShopId(shop.getId());
        request.setCategoryId(category.getId());

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound());
    }
}
