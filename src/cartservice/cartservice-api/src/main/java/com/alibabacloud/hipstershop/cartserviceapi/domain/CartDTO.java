package com.alibabacloud.hipstershop.cartserviceapi.domain;

/**
 * @author shenyi
 * @date 2020/12/30
 * @email shenyi.yt@alibaba-inc.com
 */
public class CartDTO {
    private String userId;
    private String productId;
    private Integer quantity;
    private Integer price;

    public CartDTO() {
    }

    public CartDTO(String userId, String productId, Integer quantity, Integer price) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CartDTO{" +
                "userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
