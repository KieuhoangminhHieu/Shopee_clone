package com.devteria.identity_service.service;

import com.devteria.identity_service.entity.Shop;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.repository.ShopRepository;
import com.devteria.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShopService {

    ShopRepository shopRepository;


    UserRepository userRepository;

    public Shop createShop(Shop shop) {

        User user = userRepository.findById(shop.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return shopRepository.save(shop);
    }

    public List<Shop> getShops() {
        return shopRepository.findAll();
    }

    public Shop getShopById(String id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
    }

    public Shop updateShop(String id, Shop updatedShop) {
        Shop shop = getShopById(id);


        userRepository.findById(updatedShop.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        shop.setShopName(updatedShop.getShopName());
        shop.setDescription(updatedShop.getDescription());
        shop.setUserId(updatedShop.getUserId());
        return shopRepository.save(shop);
    }

    public void deleteShop(String id) {
        if (!shopRepository.existsById(id)) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
        shopRepository.deleteById(id);
    }
}
