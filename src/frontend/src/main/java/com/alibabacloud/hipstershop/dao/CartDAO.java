package com.alibabacloud.hipstershop.dao;

import com.alibabacloud.hipstershop.cartserviceapi.domain.CartItem;
import com.alibabacloud.hipstershop.cartserviceapi.service.CartService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangtao 2019-08-14 17:11
 */
@Service
public class CartDAO {

    @Reference(version = "1.0.0")
    private CartService cartService;

    public List<CartItem> viewCart(String userId) {
        return cartService.viewCart(userId);
    }

    public boolean addToCart(String userId, String productId, int quantity, int price) {
        return cartService.addItemToCart(userId, productId, quantity, price);
    }

    public boolean emptyCart(String userId) {
        try {
            cartService.cleanCartItems(userId);
        } catch (Exception e) {
           return false;
        }
        return true;
    }
}
