package com.devteria.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
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

import com.devteria.identity_service.dto.request.ShopCreationRequest;
import com.devteria.identity_service.dto.request.ShopUpdateRequest;
import com.devteria.identity_service.dto.response.ShopResponse;
import com.devteria.identity_service.entity.Role;
import com.devteria.identity_service.entity.Shop;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.mapper.ShopMapper;
import com.devteria.identity_service.repository.RoleRepository;
import com.devteria.identity_service.repository.ShopRepository;
import com.devteria.identity_service.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Testcontainers
@Slf4j
class ShopServiceIntegrationTest {

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
    ShopService shopService;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ShopMapper shopMapper;

    User owner;
    Shop shop;

    @BeforeEach
    void setup() {
        shopRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = Role.builder().name("ROLE_USER").description("Default role").build();
        roleRepository.save(role);

        owner = User.builder()
                .username("shopowner")
                .password("12345678")
                .email("owner@gmail.com")
                .dob(LocalDate.of(2000, 1, 1))
                .phoneNumber("0909090909")
                .address("Hà Nội")
                .roles(Set.of(role))
                .build();
        owner = userRepository.save(owner);

        shop = new Shop();
        shop.setShopName("Kiều Mart");
        shop.setDescription("Shop của Kiều");
        shop.setUserId(owner.getId());
        shop.setAddress("Hà Nội");
        shop = shopRepository.save(shop);
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void createShop_success() {
        ShopCreationRequest request = ShopCreationRequest.builder()
                .shopName("Shop mới")
                .description("Mô tả shop mới")
                .address("Đà Nẵng")
                .build();

        ShopResponse response = shopService.createShop(request);
        Assertions.assertThat(response.getShopName()).isEqualTo("Shop mới");
        Assertions.assertThat(response.getAddress()).isEqualTo("Đà Nẵng");
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void getShopById_success() {
        ShopResponse response = shopService.getShopById(shop.getId());
        Assertions.assertThat(response.getShopName()).isEqualTo("Kiều Mart");
        Assertions.assertThat(response.getDescription()).contains("Kiều");
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    void getShopById_unauthorized_error() {
        var exception = assertThrows(AppException.class, () -> shopService.getShopById(shop.getId()));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void updateShop_success() {
        ShopUpdateRequest request = ShopUpdateRequest.builder()
                .shopName("Kiều Mart Updated")
                .description("Đã cập nhật mô tả")
                .address("Hồ Chí Minh")
                .build();

        ShopResponse response = shopService.updateShop(shop.getId(), request);
        Assertions.assertThat(response.getShopName()).isEqualTo("Kiều Mart Updated");
        Assertions.assertThat(response.getAddress()).isEqualTo("Hồ Chí Minh");
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    void updateShop_unauthorized_error() {
        ShopUpdateRequest request = ShopUpdateRequest.builder()
                .shopName("Hack Shop")
                .description("Không phải chủ")
                .address("Fake City")
                .build();

        var exception = assertThrows(AppException.class, () -> shopService.updateShop(shop.getId(), request));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getShops_success() {
        List<ShopResponse> shops = shopService.getShops();
        Assertions.assertThat(shops).isNotEmpty();
        Assertions.assertThat(shops.getFirst().getShopName()).isEqualTo("Kiều Mart");
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void deleteShop_success() {
        shopService.deleteShop(shop.getId());
        Assertions.assertThat(shopRepository.findById(shop.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    void deleteShop_unauthorized_error() {
        var exception = assertThrows(AppException.class, () -> shopService.deleteShop(shop.getId()));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
    }
}
