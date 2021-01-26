package com.alibabacloud.hipstershop.productservice.controller;

import com.alibabacloud.hipstershop.productservice.entity.ProductInfo;
import com.alibabacloud.hipstershop.productservice.service.ProductInfoService;
import com.alibabacloud.hipstershop.productserviceapi.domain.Product;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class ProductController {

    @Resource
    ProductInfoService productInfoService;

    @GetMapping("/product/{id}")
    public Product getProductById(@PathVariable(name = "id") String id) {
        return productInfoService.getProduct(id).getProduct();
    }

    @GetMapping("/products")
    public List<Product> getProductList() {
        return productInfoService.getAllProduct().stream().map(ProductInfo::getProduct).collect(Collectors.toList());
    }
}
