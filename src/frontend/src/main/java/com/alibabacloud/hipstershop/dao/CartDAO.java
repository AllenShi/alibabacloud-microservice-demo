package com.alibabacloud.hipstershop.dao;

import com.alibabacloud.hipstershop.cartserviceapi.domain.CartDTO;
import com.alibabacloud.hipstershop.cartserviceapi.domain.CartItem;
import com.alibabacloud.hipstershop.cartserviceapi.service.CartService;
import com.alibabacloud.hipstershop.producer.RocketMQProducer;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author wangtao 2019-08-14 17:11
 */
@Service
public class CartDAO {

    @Reference(version = "1.0.0")
    private CartService cartService;

    @Resource
    private RocketMQProducer rocketMQProducer;

    public List<CartItem> viewCart(String userId) {
        return cartService.viewCart(userId);
    }

    public void addToCart(String userId, String productId, int quantity, int price, String channelSelect) {
        if (Objects.equals(channelSelect, "rpc")) {
            cartService.addItemToCart(userId, productId, quantity, price);
        }
        if (Objects.equals(channelSelect, "rocketmq")) {
            CartDTO cartDTO = new CartDTO(userId, productId, quantity, price);
            rocketMQProducer.send(cartDTO);
        }
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
