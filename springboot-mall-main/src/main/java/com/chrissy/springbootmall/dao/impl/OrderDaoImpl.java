package com.chrissy.springbootmall.dao.impl;

import com.chrissy.springbootmall.dao.OrderDao;
import com.chrissy.springbootmall.model.Order;
import com.chrissy.springbootmall.model.OrderItem;
import com.chrissy.springbootmall.rowmapper.OrderItemRowMapper;
import com.chrissy.springbootmall.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private NamedParameterJdbcTemplate npjt;

    @Override
    public Order getOrderById(Integer orderId) {

        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                "FROM `order` WHERE order_id=:orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<Order> orderList = npjt.query(sql, map, new OrderRowMapper());
        if (orderList.size() > 0) {
            return orderList.get(0);
        } else {
            return null;
        }


    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        String sql = "SELECT oi.order_item_id,oi.order_id, oi.product_id, " +
                "oi.quantity, oi.amount, p.product_name, p.image_url" +
                " FROM order_item as oi " +
                "LEFT JOIN product as p ON oi.product_id = p.product_id " +
                "WHERE oi.order_id=:orderId";

        Map<String, Object> map = new HashMap();
        map.put("orderId", orderId);

        List<OrderItem> orderItemList = npjt.query(sql, map, new OrderItemRowMapper());

        return orderItemList;

    }

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {

        // 檢查 user 是否存在
        

        // 創建訂單
        String sql = "INSERT INTO `order` " +
                "(user_id, total_amount, created_date, last_modified_date) " +
                "VALUES (:userId, :totalAmount, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        npjt.update(sql, new MapSqlParameterSource(map), keyHolder);

        int orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {

        String sql = "INSERT INTO order_item " +
                "(order_id, product_id, quantity, amount) " +
                "VALUES (:orderId, :productId, :quantity, :amount)";
        MapSqlParameterSource[] parameterSource = new MapSqlParameterSource[orderItemList.size()];
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            parameterSource[i] = new MapSqlParameterSource();
            parameterSource[i].addValue("orderId", orderId);
            parameterSource[i].addValue("productId", orderItem.getProductId());
            parameterSource[i].addValue("quantity", orderItem.getQuantity());
            parameterSource[i].addValue("amount", orderItem.getAmount());

        }

        npjt.batchUpdate(sql, parameterSource);

    }
}
