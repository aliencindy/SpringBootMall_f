package com.chrissy.springbootmall.service;

import com.chrissy.springbootmall.dto.CreateOrderRequest;
import com.chrissy.springbootmall.model.Order;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
    Order getOrderById(Integer orderId);
}
