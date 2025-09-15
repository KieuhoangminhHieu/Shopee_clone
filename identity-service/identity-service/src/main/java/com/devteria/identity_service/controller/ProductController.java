package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.ApiResponse;
import com.devteria.identity_service.dto.ProductCreationRequest;
import com.devteria.identity_service.entity.Product;
import com.devteria.identity_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ApiResponse<Product> createProduct(@RequestBody ProductCreationRequest request) {
        ApiResponse<Product> response = new ApiResponse<>();
        response.setResult(productService.createProduct(request));
        return response;
    }

    @GetMapping
    public ApiResponse<List<Product>> getProducts() {
        ApiResponse<List<Product>> response = new ApiResponse<>();
        response.setResult(productService.getProducts());
        return response;
    }

    @GetMapping("/{productId}")
    public ApiResponse<Product> getProduct(@PathVariable String productId) {
        ApiResponse<Product> response = new ApiResponse<>();
        response.setResult(productService.getProductById(productId));
        return response;
    }

    @PutMapping("/{productId}")
    public ApiResponse<Product> updateProduct(@PathVariable String productId,
                                              @RequestBody ProductCreationRequest request) {
        ApiResponse<Product> response = new ApiResponse<>();
        response.setResult(productService.updateProduct(productId, request));
        return response;
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        ApiResponse<String> response = new ApiResponse<>();
        response.setResult("Product deleted successfully!");
        return response;
    }
}
