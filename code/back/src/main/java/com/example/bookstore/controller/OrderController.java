package com.example.bookstore.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.Order;
import com.example.bookstore.entity.OrderDetail;
import com.example.bookstore.service.*;
import com.example.bookstore.utils.DateTool;
import com.example.bookstore.utils.JwtUtil;
import com.example.bookstore.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.DelayQueue;


/**
 */

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;

    //添加
    @PostMapping("/add")
    public Result add(Double totalPrice, Integer pay) throws ParseException {

        System.out.println(totalPrice);

        Result result = new Result();
        result.setData(orderService.add(totalPrice, pay));

        result.success("订单生成成功");

        return result;
    }

    @GetMapping( "/getAll")
    public Result getAll(Integer pageNum, Integer pageSize) {
        Result result = new Result();
        //通过id查找
        Page<Order> orderPage = orderService.getAll(pageNum, pageSize);
        for (Order order:orderPage.getRecords()) {
            order.put("username", userService.getById(order.getUserId()).getUsername());
            order.put("address", userService.getById(order.getUserId()).getAddress());
        }
        result.setData(orderPage);
        result.success("查询成功");
        return result;
    }

    //根据用户userId查订单
    @GetMapping( "/getByUserId")
    public Result getByUserId() {
        Result result = new Result();
        //通过id查找
        List<Order> list = orderService.getByUserId();
//        System.out.println(list);
        if (!list.isEmpty()) {
            result.setData(list);
            result.success("查询成功");
        }
        else {
            result.fail("查询失败");
        }
        return result;
    }
    //根据status查订单
    @GetMapping( "/getByStatus")
    public Result getByStatus(Integer status) {
        Result result = new Result();
        //通过id查找
        List<Order> list = orderService.getByStatus(status);
//        System.out.println(list);
        if (!list.isEmpty()) {
            result.setData(list);
            result.success("查询成功");
        }
        else {
            result.fail("查询失败");
        }
        return result;
    }

    //更新
    @PostMapping("/update")
    public Result update(Integer orderId, Integer status){
        Result result = new Result();
        List<OrderDetail> orderDetails = orderDetailService.getByOrderId(orderId);
        Boolean flag = false;
        for (OrderDetail orderDetail: orderDetails) {
            if (orderDetail.getStatus() != status)
                flag = true;
        }
        if (!flag) {//订单详情更新完毕
            result.setData(orderService.updateOrder(orderId, status));
            result.success("订单更新成功");
        }
        else {
            result.fail("有订单详情未更新");
        }
        return result;
    }

    //取消订单
    @PostMapping("/cancel")
    public Result cancel(Integer orderId, String reason){
        Result result = new Result();
        Order order = orderService.cancel(orderId, reason);
        List<OrderDetail> list = orderDetailService.getByOrderId(orderId);
        for (OrderDetail orderDetail: list) {//取消该订单下的所有订单详情
            orderDetail.setStatus(-2);
            orderDetailService.updateById(orderDetail);
        }
        result.setData(order);
        result.success("取消成功");
        return result;
    }

    //删除
    @PostMapping("/deleteByIds")
    public Result deleteByIds(String ids){
        Result result = new Result();

        orderService.deleteByIds(ids);
        result.success("删除成功");
        return result;
    }
}

