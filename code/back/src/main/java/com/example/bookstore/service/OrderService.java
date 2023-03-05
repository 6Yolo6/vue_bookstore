package com.example.bookstore.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookstore.entity.Order;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.DelayQueue;


/**

 */
public interface OrderService extends IService<Order> {

    void cancelOrder();

    DelayQueue<Order> pushOrder(Order order);

    Order add(Double totalPrice, Integer pay) throws ParseException;

    List<Order> getByStatus(Integer status);

    Order updateOrder(Integer orderId, Integer status);

    List<Order> getByUserId();

    Page<Order> getAll(Integer pageNum, Integer pageSize);

    void deleteByIds(String ids);

    Order cancel(Integer orderId, String reason);

    Order getByNo(String orderNo);
}
