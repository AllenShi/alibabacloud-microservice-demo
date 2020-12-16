package com.alibabacloud.hipstershop.productservice.service;

import com.alibabacloud.hipstershop.productserviceapi.domain.ProductItem;
import com.alibabacloud.hipstershop.productserviceapi.service.ProductService;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.*;


/**
 * 对外提供的Dubbo service实现类
 *
 * @author xiaofeng.gxf
 * @date 2020/6/29
 */

@DubboComponentScan
@RefreshScope
@Service(version = "1.0.0")
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public List<ProductItem> confirmInventory(List<ProductItem> checkoutProductItems) {

        for (ProductItem item : checkoutProductItems) {
            //@TODO check inventory
            item.setLock(true);
        }
        return checkoutProductItems;
    }
}
