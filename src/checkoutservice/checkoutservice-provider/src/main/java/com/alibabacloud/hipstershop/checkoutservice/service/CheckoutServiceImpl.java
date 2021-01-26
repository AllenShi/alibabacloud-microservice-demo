package com.alibabacloud.hipstershop.checkoutservice.service;

import com.alibabacloud.hipstershop.cartserviceapi.domain.CartItem;
import com.alibabacloud.hipstershop.cartserviceapi.service.CartService;

import com.alibabacloud.hipstershop.productserviceapi.domain.ProductItem;
import com.alibabacloud.hipstershop.productserviceapi.service.ProductService;

import com.alibabacloud.hipstershop.checkoutserviceapi.domain.Order;
import com.alibabacloud.hipstershop.checkoutserviceapi.service.CheckoutService;

import com.alibabacloud.hipstershop.checkoutservice.entity.OrderForm;
import com.alibabacloud.hipstershop.checkoutservice.repository.OrderFormRepository;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author xiaofeng.gxf
 * @date 2020/6/24
 */
@DubboComponentScan
@RefreshScope
@Service(version = "0.0.1")
public class CheckoutServiceImpl implements CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);

    @Reference(version = "1.0.0")
    CartService cartService;

    @Reference(version = "1.0.0")
    ProductService productService;

    /**
     * orderForm数据库操作接口
     */
    @Resource
    private OrderFormRepository orderFormRepository;

    @Override
    public String checkout(String email, String streetAddress, String zipCode, String city, String state, String country,
                           String creditCardNumber, int creditCardExpirationMonth, String creditCardCvv, String userId) {
        Order order;
        //生成uuid
        UUID uuid = UUID.randomUUID();
        order = new Order();
        order.setOrderId(uuid.toString());
        order.setUserId(userId);

        //获取购物车商品
        List<CartItem> items = cartService.cleanCartItems(userId);

        List<ProductItem> productItems = new ArrayList<>();
        for (CartItem item : items) {
            int quantity = item.getQuantity();
            double priceInTotal = item.getPrice() * quantity;
            productItems.add(new ProductItem(item.getProductId(), quantity, priceInTotal, order.getOrderId()));
        }

        //校验库存
        List<ProductItem> lockedProductItems = productService.confirmInventory(productItems);
        //保存商品列表

        order.setProductItemList(productItems);

        int lockedProductNum = 0;
        for (ProductItem item : lockedProductItems) {
            if (item.isLock()) {
                lockedProductNum++;
            }
        }

        if (lockedProductNum > 0) {
            //状态为1表示至少有一件商品购买成功。
            order.setStatus(1);

            //计算价格
            double productCost = 0;
            for (ProductItem item : lockedProductItems) {
                productCost += item.getPriceInTotal();
            }
            order.setProductCost(productCost);
            //校验、保存地址

            //生成订单，支付

            //运输商品
            order.setShipCost(0.0);

            order.setTotalCost(productCost);

        } else {
            //状态为2表示所有商品都购买失败。
            order.setStatus(-1);
            //临时模拟
            order.setShipId(null);
            order.setProductCost(0.0);
            order.setShipCost(0.0);
            order.setTotalCost(0.0);
        }
        orderFormRepository.save(new OrderForm(order));
        return order.getOrderId();
    }

    @Override
    public Order getOrder(String orderId, String userId) {
        Optional<OrderForm> orderFormOptional = orderFormRepository.findById(orderId);
        return orderFormOptional.orElse(new OrderForm()).getOrder();
    }
}
