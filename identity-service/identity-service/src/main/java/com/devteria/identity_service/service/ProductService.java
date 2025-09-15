package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.ProductCreationRequest;
import com.devteria.identity_service.entity.Category;
import com.devteria.identity_service.entity.Product;
import com.devteria.identity_service.entity.Shop;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.repository.CategoryRepository;
import com.devteria.identity_service.repository.ProductRepository;
import com.devteria.identity_service.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Product createProduct(ProductCreationRequest request) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductDescription(request.getProductDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImage(request.getImage());

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_FOUND));
        product.setShop(shop);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        return productRepository.save(product);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXISTED));
    }

    public Product updateProduct(String productId, ProductCreationRequest request) {
        Product product = getProductById(productId);

        product.setProductName(request.getProductName());
        product.setProductDescription(request.getProductDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImage(request.getImage());

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_FOUND));
        product.setShop(shop);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        return productRepository.save(product);
    }

    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXISTED);
        }
        productRepository.deleteById(productId);
    }
}
