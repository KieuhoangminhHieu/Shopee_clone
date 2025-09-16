package com.devteria.identity_service.controller;

import com.devteria.identity_service.dto.response.ApiResponse;
import com.devteria.identity_service.entity.Category;
import com.devteria.identity_service.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    public ApiResponse<Category> createCategory(@RequestBody Category category) {
        ApiResponse<Category> response = new ApiResponse<>();
        response.setResult(categoryService.createCategory(category));
        return response;
    }

    @GetMapping
    public ApiResponse<List<Category>> getCategories() {
        ApiResponse<List<Category>> response = new ApiResponse<>();
        response.setResult(categoryService.getCategories());
        return response;
    }

    @GetMapping("/{categoryId}")
    public ApiResponse<Category> getCategory(@PathVariable String categoryId) {
        ApiResponse<Category> response = new ApiResponse<>();
        response.setResult(categoryService.getCategoryById(categoryId));
        return response;
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<Category> updateCategory(@PathVariable String categoryId, @RequestBody Category category) {
        ApiResponse<Category> response = new ApiResponse<>();
        response.setResult(categoryService.updateCategory(categoryId, category));
        return response;
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<String> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        ApiResponse<String> response = new ApiResponse<>();
        response.setResult("Category deleted successfully");
        return response;
    }
}
