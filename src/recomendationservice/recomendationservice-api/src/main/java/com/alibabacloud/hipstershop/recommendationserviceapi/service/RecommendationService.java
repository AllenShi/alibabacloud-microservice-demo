package com.alibabacloud.hipstershop.recommendationserviceapi.service;

import com.alibabacloud.hipstershop.productserviceapi.domain.Product;

import java.util.List;

/**
 * @author jicong.ljc
 * @date 2020/9/9
 * @email jicong.ljc@alibaba-inc.com
 */
public interface RecommendationService {
    List<Product> sortProduct(List<Product> products);
}
