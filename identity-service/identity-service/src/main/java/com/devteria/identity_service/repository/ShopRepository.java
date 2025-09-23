package com.devteria.identity_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devteria.identity_service.entity.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, String> {

    List<Shop> findByUserId(String userId);

    boolean existsByShopName(String shopName);
}
