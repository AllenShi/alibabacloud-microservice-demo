package com.alibabacloud.hipstershop.cartserviceapi.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author wangtao 2019-08-14 16:35
 */
public class CartItem implements Serializable {
    private String productId;
    private int quantity;
    private String productName;
    private String productPicture;
    private int price;

    public CartItem() {
    }

    public CartItem(String productId, int quantity, int price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", productName='" + productName + '\'' +
                ", productPicture='" + productPicture + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem item = (CartItem) o;
        return productId.equals(item.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
