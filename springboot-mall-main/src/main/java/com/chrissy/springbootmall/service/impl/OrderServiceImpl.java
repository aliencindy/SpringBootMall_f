package com.chrissy.springbootmall.service.impl;

import com.chrissy.springbootmall.dao.OrderDao;
import com.chrissy.springbootmall.dao.ProductDao;
import com.chrissy.springbootmall.dto.BuyItem;
import com.chrissy.springbootmall.dto.CreateOrderRequest;
import com.chrissy.springbootmall.model.Order;
import com.chrissy.springbootmall.model.OrderItem;
import com.chrissy.springbootmall.model.Product;
import com.chrissy.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public Order getOrderById(Integer orderId) {

        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);
        order.setOrderItemList(orderItemList);

        return order;
    }

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());
        // 計算總價錢
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

        // 轉換 BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setAmount(amount);
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItemList.add(orderItem);
        }
        // 創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);
        orderDao.createOrderItems(orderId,orderItemList);

        return orderId;
    }
}
