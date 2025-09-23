package com.devteria.identity_service.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devteria.identity_service.dto.request.ShopCreationRequest;
import com.devteria.identity_service.dto.request.ShopUpdateRequest;
import com.devteria.identity_service.dto.response.ShopResponse;
import com.devteria.identity_service.entity.Shop;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.ShopMapper;
import com.devteria.identity_service.repository.ShopRepository;
import com.devteria.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ShopService {

    ShopRepository shopRepository;
    UserRepository userRepository;
    ShopMapper shopMapper;

    private String getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND))
                .getId();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ShopResponse createShop(ShopCreationRequest request) {
        String currentUserId = getCurrentUserId();

        Shop shop = shopMapper.toEntity(request);
        shop.setUserId(currentUserId);

        Shop saved = shopRepository.save(shop);
        log.info("Created shop {} for userId {}", saved.getShopName(), currentUserId);

        return shopMapper.toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ShopResponse> getShops() {
        return shopRepository.findAll().stream().map(shopMapper::toResponse).toList();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ShopResponse getShopById(String id) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        String currentUserId = getCurrentUserId();
        if (!shop.getUserId().equals(currentUserId)
                && SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return shopMapper.toResponse(shop);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ShopResponse updateShop(String id, ShopUpdateRequest request) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        String currentUserId = getCurrentUserId();
        if (!shop.getUserId().equals(currentUserId)
                && SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        shopMapper.updateEntityFromDto(request, shop);

        Shop updated = shopRepository.save(shop);
        log.info("Updated shop {} by user {}", updated.getId(), currentUserId);

        return shopMapper.toResponse(updated);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteShop(String id) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        String currentUserId = getCurrentUserId();
        if (!shop.getUserId().equals(currentUserId)
                && SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        shopRepository.delete(shop);
        log.warn("Deleted shop {} by user {}", id, currentUserId);
    }
}
