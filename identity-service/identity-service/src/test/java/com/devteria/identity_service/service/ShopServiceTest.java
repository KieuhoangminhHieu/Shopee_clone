package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.request.ShopCreationRequest;
import com.devteria.identity_service.dto.request.ShopUpdateRequest;
import com.devteria.identity_service.dto.response.ShopResponse;
import com.devteria.identity_service.entity.Shop;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.ShopMapper;
import com.devteria.identity_service.repository.ShopRepository;
import com.devteria.identity_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ShopServiceTest {

    @Autowired
    private ShopService shopService;

    @MockitoBean
    private ShopRepository shopRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ShopMapper shopMapper;

    private ShopCreationRequest creationRequest;
    private ShopUpdateRequest updateRequest;
    private Shop shop;
    private ShopResponse shopResponse;
    private User user;

    @BeforeEach
    void setup() {
        creationRequest = new ShopCreationRequest();
        creationRequest.setShopName("Kiều Store");
        creationRequest.setDescription("Chuyên bán đồ handmade");
        creationRequest.setAddress("123 Đường Láng, Hà Nội");

        updateRequest = new ShopUpdateRequest();
        updateRequest.setShopName("Kiều Store Updated");
        updateRequest.setDescription("Cập nhật mô tả");
        updateRequest.setAddress("456 Trần Duy Hưng, Hà Nội");

        shop = new Shop();
        shop.setId("shop123");
        shop.setShopName("Kiều Store");
        shop.setDescription("Chuyên bán đồ handmade");
        shop.setAddress("123 Đường Láng, Hà Nội");
        shop.setUserId("user123");

        shopResponse = new ShopResponse();
        shopResponse.setId("shop123");
        shopResponse.setShopName("Kiều Store");
        shopResponse.setDescription("Chuyên bán đồ handmade");
        shopResponse.setAddress("123 Đường Láng, Hà Nội");

        user = new User();
        user.setId("user123");
        user.setUsername("kieudev");

        when(shopMapper.toEntity(any())).thenReturn(shop);
        when(shopMapper.toResponse(any())).thenReturn(shopResponse);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    }

    @Test
    @WithMockUser(username = "kieudev", roles = {"USER"})
    void createShop_success() {
        when(shopRepository.save(any())).thenReturn(shop);

        var result = shopService.createShop(creationRequest);

        assertThat(result).isNotNull();
        assertThat(result.getShopName()).isEqualTo("Kiều Store");
        assertThat(result.getAddress()).isEqualTo("123 Đường Láng, Hà Nội");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getShops_success() {
        when(shopRepository.findAll()).thenReturn(List.of(shop));

        var result = shopService.getShops();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getShopName()).isEqualTo("Kiều Store");
        assertThat(result.get(0).getAddress()).isEqualTo("123 Đường Láng, Hà Nội");
    }

    @Test
    @WithMockUser(username = "kieudev", roles = {"USER"})
    void getShopById_authorized_success() {
        when(shopRepository.findById("shop123")).thenReturn(Optional.of(shop));

        var result = shopService.getShopById("shop123");

        assertThat(result.getShopName()).isEqualTo("Kiều Store");
        assertThat(result.getAddress()).isEqualTo("123 Đường Láng, Hà Nội");
    }

    @Test
    @WithMockUser(username = "otheruser", roles = {"USER"})
    void getShopById_unauthorized_fail() {
        Shop otherShop = new Shop();
        otherShop.setId("shop123");
        otherShop.setUserId("user123");

        when(shopRepository.findById("shop123")).thenReturn(Optional.of(otherShop));

        User otherUser = new User();
        otherUser.setId("user999");
        otherUser.setUsername("otheruser");

        when(userRepository.findByUsername("otheruser")).thenReturn(Optional.of(otherUser));

        var exception = assertThrows(AppException.class, () -> shopService.getShopById("shop123"));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "kieudev", roles = {"USER"})
    void updateShop_success() {
        when(shopRepository.findById("shop123")).thenReturn(Optional.of(shop));
        when(shopRepository.save(any())).thenReturn(shop);

        var result = shopService.updateShop("shop123", updateRequest);

        assertThat(result.getShopName()).isEqualTo("Kiều Store");
        assertThat(result.getAddress()).isEqualTo("123 Đường Láng, Hà Nội");
        verify(shopMapper).updateEntityFromDto(updateRequest, shop);
    }

    @Test
    @WithMockUser(username = "kieudev", roles = {"USER"})
    void deleteShop_success() {
        when(shopRepository.findById("shop123")).thenReturn(Optional.of(shop));

        shopService.deleteShop("shop123");

        verify(shopRepository).delete(shop);
    }

    @Test
    @WithMockUser(username = "kieudev", roles = {"USER"})
    void getShopById_notFound_fail() {
        when(shopRepository.findById("notfound")).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class, () -> shopService.getShopById("notfound"));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_KEY);
    }
}