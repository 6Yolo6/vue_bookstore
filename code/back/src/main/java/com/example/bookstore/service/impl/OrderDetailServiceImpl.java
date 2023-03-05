package com.example.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.Order;
import com.example.bookstore.entity.OrderDetail;
import com.example.bookstore.mapper.OrderDetailMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.service.OrderDetailService;
import com.example.bookstore.service.OrderService;
import com.example.bookstore.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Autowired
    private HttpServletRequest request;


    @Override
    public List<OrderDetail> add(Integer orderId, Integer status, String ids, String nums) {

        String token = request.getHeader("Authorization");
        String userId = JwtUtil.validateToken(token);
//        System.out.println(userId);
        List<OrderDetail> orderDetails = new ArrayList<>();

        String[] arrayId = ids.split(",");
        String[] arrayNum = nums.split(",");

        for (Integer i = 0; i < arrayId.length; i++) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setStatus(status);
            orderDetail.setBookId(Integer.valueOf(arrayId[i]));
            orderDetail.setNum(Integer.valueOf(arrayNum[i]));
            this.save(orderDetail);
            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }

    @Override
    public List<OrderDetail> getDetail(String orderIds, Integer status) {
//        String token = request.getHeader("Authorization");
////        System.out.println("token" + token);
//        String userId = JwtUtil.validateToken(token);
        List<String> list = new ArrayList<>();
        String[] array = orderIds.split(",");
        for (String i:array) {
            list.add(i);
        }
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        if (status == 0)
            queryWrapper.in("order_id", list);
        else
            queryWrapper.in("order_id", list).eq("status", status);
        return this.list(queryWrapper);
    }

    @Override
    public OrderDetail update(Integer orderId, Integer bookId, Integer status) {
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("order_id", orderId).eq("book_id", bookId).eq("status", status);

        OrderDetail orderDetail = this.getOne(queryWrapper);
        if (status == 4) {//评价
            orderDetail.setStatus(-1);
        }
        else if (status == -1) {//追评
            orderDetail.setStatus(-3);
        }
        else {
            orderDetail.setStatus(status+1);
        }
        this.updateById(orderDetail);

        System.out.println(orderDetail);
        return orderDetail;
    }

    @Override
    public List<OrderDetail> getDetailByStatus(Integer status) {
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);

        return this.list(queryWrapper);
    }

    @Override
    public List<OrderDetail> getByOrderId(Integer orderId) {
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);

        return this.list(queryWrapper);
    }

    @Override
    public void deleteByIds(String orderIds, String bookIds) {//根据Id删除
//        System.out.println(ids);
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        List<String> listBook = new ArrayList<>();
        List<String> listOrder= new ArrayList<>();
        String[] arrayOrder = orderIds.split(",");
        String[] arrayBook = bookIds.split(",");
        for (String i:arrayOrder)
            listOrder.add(i);
        for (String i:arrayBook)
            listBook.add(i);
        queryWrapper.in("book_id", listBook).in("order_id", listOrder);
        this.remove(queryWrapper);
    }
}
