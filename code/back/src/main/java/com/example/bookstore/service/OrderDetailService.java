package com.example.bookstore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookstore.entity.OrderDetail;

import java.util.List;


/**

 */
public interface OrderDetailService extends IService<OrderDetail> {

    List<OrderDetail> add(Integer orderId, Integer status, String ids, String nums);

    List<OrderDetail> getDetail(String orderIds, Integer status);

    OrderDetail update(Integer orderId, Integer bookId, Integer status);

    List<OrderDetail> getDetailByStatus(Integer status);

    List<OrderDetail> getByOrderId(Integer orderId);

    void deleteByIds(String orderIds, String bookIds);
}
