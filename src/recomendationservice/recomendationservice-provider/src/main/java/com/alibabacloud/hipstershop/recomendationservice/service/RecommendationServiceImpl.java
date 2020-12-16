package com.alibabacloud.hipstershop.recomendationservice.service;

import com.alibabacloud.hipstershop.productserviceapi.domain.Product;
import com.alibabacloud.hipstershop.recommendationserviceapi.service.RecommendationService;
import org.apache.dubbo.config.annotation.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jicong.ljc
 * @date 2020/9/9
 * @email jicong.ljc@alibaba-inc.com
 */
@Service(version = "1.0.0")
public class RecommendationServiceImpl implements RecommendationService {
    @Override
    public List<Product> sortProduct(List<Product> products) {
        List<Product> result = new ArrayList<>(products);
        Collections.shuffle(result);
        return result;
    }
}
