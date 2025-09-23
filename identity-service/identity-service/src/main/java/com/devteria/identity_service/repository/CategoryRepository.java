package com.devteria.identity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devteria.identity_service.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {}
