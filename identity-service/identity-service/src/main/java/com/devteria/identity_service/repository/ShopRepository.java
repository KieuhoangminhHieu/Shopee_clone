package com.devteria.identity_service.repository;

import com.devteria.identity_service.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, String> {

    List<Shop> findByUserId(String userId);

    boolean existsByShopName(String shopName);
}
