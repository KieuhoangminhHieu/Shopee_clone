package com.devteria.identity_service.service;

import com.devteria.identity_service.dto.request.CartItemRequest;
import com.devteria.identity_service.dto.response.CartItemResponse;
import com.devteria.identity_service.entity.Cart;
import com.devteria.identity_service.entity.CartItem;
import com.devteria.identity_service.entity.Product;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.exception.AppException;
import com.devteria.identity_service.exception.ErrorCode;
import com.devteria.identity_service.mapper.CartMapper;
import com.devteria.identity_service.repository.CartItemRepository;
import com.devteria.identity_service.repository.CartRepository;
import com.devteria.identity_service.repository.ProductRepository;
import com.devteria.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Transactional
    public CartItemResponse addOrUpdateCartItemByUser(String userId, CartItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUser_Id(userId).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            c.setTotalPrice(BigDecimal.ZERO);
            return cartRepository.save(c);
        });

        return addOrUpdateCartItem(cart, request);
    }

    @Transactional
    public CartItemResponse addOrUpdateCartItemByCartId(String cartId, CartItemRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        return addOrUpdateCartItem(cart, request);
    }

    private CartItemResponse addOrUpdateCartItem(Cart cart, CartItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // existing item?
        CartItem item = cartItemRepository
                .findByCart_IdAndProduct_Id(cart.getId(), product.getId())
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setCart(cart);
                    ci.setProduct(product);
                    ci.setPrice(product.getPrice()); // BigDecimal
                    return ci;
                });

        item.setQuantity(request.getQuantity());
        item.setSubTotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

        cartItemRepository.save(item);

        // recalc cart total from DB to be safe
        List<CartItem> items = cartItemRepository.findByCart_Id(cart.getId());
        BigDecimal total = items.stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
        cartRepository.save(cart);

        return cartMapper.toCartItemResponse(item);
    }

    @Transactional
    public void removeCartItemByUser(String userId, String productId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        removeCartItemFromCart(cart, productId);
    }

    @Transactional
    public void removeCartItemByCartId(String cartId, String productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        removeCartItemFromCart(cart, productId);
    }

    private void removeCartItemFromCart(Cart cart, String productId) {
        CartItem item = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
                .orElseThrow(() -> new AppException(ErrorCode.CARTITEM_NOT_FOUND));
        cartItemRepository.delete(item);

        List<CartItem> items = cartItemRepository.findByCart_Id(cart.getId());
        BigDecimal total = items.stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
        cartRepository.save(cart);
    }
}
