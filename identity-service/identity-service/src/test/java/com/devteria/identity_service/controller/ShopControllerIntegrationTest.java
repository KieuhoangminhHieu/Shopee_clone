package com.devteria.identity_service.controller;

import java.time.LocalDate;
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

import com.devteria.identity_service.dto.request.ShopCreationRequest;
import com.devteria.identity_service.dto.request.ShopUpdateRequest;
import com.devteria.identity_service.entity.Role;
import com.devteria.identity_service.entity.Shop;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.repository.RoleRepository;
import com.devteria.identity_service.repository.ShopRepository;
import com.devteria.identity_service.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@Testcontainers
class ShopControllerIntegrationTest {

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
    private ShopRepository shopRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private ObjectMapper objectMapper;

    private User owner;
    private Shop shop;

    @BeforeEach
    void initData() {
        shopRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = Role.builder().name("ROLE_USER").description("Default role").build();
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

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void createShop_success() throws Exception {
        ShopCreationRequest request = ShopCreationRequest.builder()
                .shopName("Shop mới")
                .description("Mô tả shop mới")
                .address("Đà Nẵng")
                .build();

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/shops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.shopName").value("Shop mới"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.address").value("Đà Nẵng"));
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void getShopById_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/shops/" + shop.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.shopName").value("Kiều Mart"));
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    void getShopById_unauthorized_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/shops/" + shop.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1004"));
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void updateShop_success() throws Exception {
        ShopUpdateRequest request = ShopUpdateRequest.builder()
                .shopName("Kiều Mart Updated")
                .description("Đã cập nhật mô tả")
                .address("Hồ Chí Minh")
                .build();

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.put("/shops/" + shop.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.shopName").value("Kiều Mart Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.address").value("Hồ Chí Minh"));
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    void updateShop_unauthorized_fail() throws Exception {
        ShopUpdateRequest request = ShopUpdateRequest.builder()
                .shopName("Hack Shop")
                .description("Không phải chủ")
                .address("Fake City")
                .build();

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.put("/shops/" + shop.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1004"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getShops_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/shops"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("result[0].shopName").value("Kiều Mart"));
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void deleteShop_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/shops/" + shop.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"));
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    void deleteShop_unauthorized_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/shops/" + shop.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1004"));
    }
}
