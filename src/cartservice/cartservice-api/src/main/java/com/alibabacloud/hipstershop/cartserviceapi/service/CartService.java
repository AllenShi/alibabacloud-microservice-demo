package com.alibabacloud.hipstershop.cartserviceapi.service;

import com.alibabacloud.hipstershop.cartserviceapi.domain.CartItem;

import java.util.List;

public interface CartService {

    List<CartItem> viewCart(String userID);

    boolean addItemToCart(String userID, String productID, int quantity, int price);

    List<CartItem> cleanCartItems(String userID);
}