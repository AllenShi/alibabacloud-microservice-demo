package com.alibabacloud.hipstershop.dao;

import com.alibabacloud.hipstershop.productserviceapi.domain.Product;
import com.alibabacloud.hipstershop.recommendationserviceapi.service.RecommendationService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ProductDAO {

    @Autowired
    private ProductService productService;

    @Reference(version = "1.0.0")
    private RecommendationService recommendationService;

    public Product getProductById(String id) {
        return productService.getProductById(id);
    }

    public List<Product> getProductList() {

        ResponseEntity<List<Product>> responseEntity = productService.getProductList();

        List<Product> result =  responseEntity.getBody();
        try {
            result = recommendationService.sortProduct(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @FeignClient(name = "productservice")
    public interface ProductService {

        @GetMapping("/products/")
        ResponseEntity<List<Product>> getProductList();

        @GetMapping("/product/{id}")
        Product getProductById(@PathVariable(name = "id") String id);

    }
}
