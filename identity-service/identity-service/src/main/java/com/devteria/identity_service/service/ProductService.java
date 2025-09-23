package com.devteria.identity_service.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devteria.identity_service.dto.request.ProductRequest;
import com.devteria.identity_service.dto.response.ProductResponse;
import com.devteria.identity_service.entity.Category;
import com.devteria.identity_service.entity.Product;
import com.devteria.identity_service.entity.Shop;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.ProductMapper;
import com.devteria.identity_service.repository.CategoryRepository;
import com.devteria.identity_service.repository.ProductRepository;
import com.devteria.identity_service.repository.ShopRepository;
import com.devteria.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    ShopRepository shopRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    ProductMapper productMapper;

    private String getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND))
                .getId();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ProductResponse createProduct(ProductRequest request) {
        Shop shop =
                shopRepository.findById(request.getShopId()).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        String currentUserId = getCurrentUserId();
        if (!shop.getUserId().equals(currentUserId)
                && SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        Product product = productMapper.toEntity(request);
        product.setShop(shop);
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        return productMapper.toResponse(product);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        String currentUserId = getCurrentUserId();
        if (!product.getShop().getUserId().equals(currentUserId)
                && SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        productMapper.updateEntityFromDto(request, product);
        product.setCategory(category);

        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        String currentUserId = getCurrentUserId();
        if (!product.getShop().getUserId().equals(currentUserId)
                && SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        productRepository.delete(product);
    }
}
