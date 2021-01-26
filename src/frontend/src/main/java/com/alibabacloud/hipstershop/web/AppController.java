package com.alibabacloud.hipstershop.web;

import com.alibabacloud.hipstershop.cartserviceapi.domain.CartItem;
import com.alibabacloud.hipstershop.checkoutserviceapi.domain.Order;
import com.alibabacloud.hipstershop.dao.CartDAO;
import com.alibabacloud.hipstershop.dao.OrderDAO;
import com.alibabacloud.hipstershop.dao.ProductDAO;
import com.alibabacloud.hipstershop.productserviceapi.domain.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

/**
 * @author wangtao 2019-08-12 15:41
 */
@Api(value = "/", tags = {"首页操作接口"})
@Controller
public class AppController {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private OrderDAO orderDAO;

    private String userID = "Test User";

    @ApiOperation(value = "首页", tags = {"首页操作页面"})
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productDAO.getProductList());

        return "index.html";
    }

    @PostMapping("/checkout")
    public RedirectView checkout(@RequestParam(name = "email") String email,
                                 @RequestParam(name = "street_address") String streetAddress,
                                 @RequestParam(name = "zip_code") String zipCode,
                                 @RequestParam(name = "city") String city,
                                 @RequestParam(name = "state") String state,
                                 @RequestParam(name = "country") String country,
                                 @RequestParam(name = "credit_card_number") String creditCardNumber,
                                 @RequestParam(name = "credit_card_expiration_month") int creditCardExpirationMonth,
                                 @RequestParam(name = "credit_card_cvv") String creditCardCvv) {
        String orderId = orderDAO.checkout(email, streetAddress, zipCode, city, state, country, creditCardNumber,
                creditCardExpirationMonth, creditCardCvv, userID);
        return new RedirectView("/checkout/" + orderId);
    }

    @GetMapping("/checkout/{orderId}")
    public String checkout(@PathVariable(name = "orderId") String orderId, Model model) {
        Order order = orderDAO.getOrder(orderId, userID);
        model.addAttribute("order", order);
        return "checkout.html";
    }

    @ApiOperation(value = "产品详情", tags = {"用户操作页面"})
    @GetMapping("/product/{id}")
    public String product(@PathVariable(name = "id") @ApiParam(name = "id", value = "产品id", required = true) String id, Model model) {
        Product p = productDAO.getProductById(id);
        model.addAttribute("product", p);
        return "product.html";
    }

    @ApiOperation(value = "查看购物车")
    @GetMapping("/cart")
    public String viewCart(Model model) {
        List<CartItem> items = cartDAO.viewCart(userID);
        double totalCost = 0;
        for (CartItem item : items) {
            Product p = productDAO.getProductById(item.getProductId());
            item.setProductName(p.getName() + item.getProductName());
            item.setProductPicture(p.getPicture());
            totalCost += item.getPrice() * item.getQuantity();
        }
        model.addAttribute("items", items);
        model.addAttribute("totalCost", totalCost);
        return "cart.html";
    }

    @ApiOperation(value = "新增购物车商品")
    @PostMapping("/cart")
    public RedirectView addToCart(@RequestParam(name = "product_id") @ApiParam(name = "productID", value = "产品id", required = true) String productID,
                                  @RequestParam(name = "quantity") @ApiParam(name = "quantity", value = "数量", required = true) int quantity,
                                  @RequestParam(name = "price") @ApiParam(name = "price", value = "价格", required = true) int price,
                                  @RequestParam(name = "channel_select") @ApiParam(name = "channelSelect", value = "下单通信方式", required = true) String channelSelect) {
        cartDAO.addToCart(userID, productID, quantity, price, channelSelect);
        return new RedirectView("/cart");
    }

    @ApiOperation(value = "清空购物车")
    @PostMapping("/cart/empty")
    public RedirectView emptyCart() {
        cartDAO.emptyCart(userID);
        return new RedirectView("/cart");
    }

    @ModelAttribute("cartSize")
    public int getCartSize() {
        return cartDAO.viewCart(userID).size();
    }

}
