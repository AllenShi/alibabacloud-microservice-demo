package com.alibabacloud.hipstershop.cartserviceprovider.service;

import com.alibabacloud.hipstershop.cartserviceapi.domain.CartItem;
import com.alibabacloud.hipstershop.cartserviceprovider.repository.RedisRepository;
import org.apache.dubbo.config.annotation.Service;
import com.alibabacloud.hipstershop.cartserviceapi.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.annotation.Resource;
import java.util.*;

@RefreshScope
@Service(version = "1.0.0")
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Resource
    private RedisRepository redisRepository;

    @Override
    public List<CartItem> viewCart(String userId) {
        return redisRepository.getUserCartItems(userId);
    }

    @Override
    public boolean addItemToCart(String userId, String productId, int quantity, int price) {
        return redisRepository.save(new CartItem(productId, quantity, price), userId);
    }

    @Override
    public List<CartItem> cleanCartItems(String userId) {
        List<CartItem> items = viewCart(userId);
        redisRepository.removeUserCartItems(userId);
        return items;
    }
}
