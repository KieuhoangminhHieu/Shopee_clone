package com.devteria.identity_service.service;

import java.math.BigDecimal;
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

import com.devteria.identity_service.dto.request.ProductRequest;
import com.devteria.identity_service.dto.response.ProductResponse;
import com.devteria.identity_service.entity.*;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.ProductMapper;
import com.devteria.identity_service.repository.*;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Testcontainers
@Slf4j
class ProductServiceIntegrationTest {

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
    ProductService productService;

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

    @Autowired
    ProductMapper productMapper;

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

        Role role = Role.builder().name("ROLE_USER").description("User role").build();
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

        category = Category.builder()
                .name("Thời trang")
                .description("Các sản phẩm thời trang")
                .build();
        category = categoryRepository.save(category);

        product = new Product();
        product.setName("Áo thun");
        product.setDescription("Áo thun cotton");
        product.setPrice(BigDecimal.valueOf(199000.0));
        product.setShop(shop);
        product.setCategory(category);
        product = productRepository.save(product);
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void createProduct_success() {
        ProductRequest request = new ProductRequest();
        request.setName("Quần jean");
        request.setDescription("Quần jean xanh");
        request.setPrice(299000.0);
        request.setShopId(shop.getId());
        request.setCategoryId(category.getId());

        ProductResponse response = productService.createProduct(request);

        Assertions.assertThat(response.getName()).isEqualTo("Quần jean");
        Assertions.assertThat(response.getPrice()).isEqualTo(299000.0);
    }

    @Test
    void getAllProducts_success() {
        List<ProductResponse> responses = productService.getAllProducts();

        Assertions.assertThat(responses).isNotEmpty();
        Assertions.assertThat(responses.getFirst().getName()).isEqualTo("Áo thun");
    }

    @Test
    void getProductById_success() {
        ProductResponse response = productService.getProductById(product.getId());

        Assertions.assertThat(response.getName()).isEqualTo("Áo thun");
    }

    @Test
    void getProductById_invalid_fail() {
        Assertions.assertThatThrownBy(() -> productService.getProductById("invalid-id"))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.INVALID_KEY.getMessage());
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void updateProduct_success() {
        ProductRequest request = new ProductRequest();
        request.setName("Áo thun cập nhật");
        request.setDescription("Mô tả mới");
        request.setPrice(249000.0);
        request.setShopId(shop.getId());
        request.setCategoryId(category.getId());

        ProductResponse response = productService.updateProduct(product.getId(), request);

        Assertions.assertThat(response.getName()).isEqualTo("Áo thun cập nhật");
        Assertions.assertThat(response.getPrice()).isEqualTo(249000.0);
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void deleteProduct_success() {
        productService.deleteProduct(product.getId());

        Assertions.assertThat(productRepository.findById(product.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void updateProduct_invalid_fail() {
        ProductRequest request = new ProductRequest();
        request.setName("Tên mới");
        request.setDescription("Mô tả mới");
        request.setPrice(123000.0);
        request.setShopId(shop.getId());
        request.setCategoryId(category.getId());

        Assertions.assertThatThrownBy(() -> productService.updateProduct("invalid-id", request))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.INVALID_KEY.getMessage());
    }

    @Test
    @WithMockUser(username = "shopowner", roles = "USER")
    void deleteProduct_invalid_fail() {
        Assertions.assertThatThrownBy(() -> productService.deleteProduct("invalid-id"))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(ErrorCode.INVALID_KEY.getMessage());
    }
}
