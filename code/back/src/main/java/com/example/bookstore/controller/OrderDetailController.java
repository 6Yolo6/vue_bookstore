package com.example.bookstore.controller;


import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.Order;
import com.example.bookstore.entity.OrderDetail;
import com.example.bookstore.service.*;
import com.example.bookstore.utils.JwtUtil;
import com.example.bookstore.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 */

@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private HttpServletRequest request;
    //添加
    @PostMapping("/add")
    public Result add(Integer orderId, Integer status, String ids, String nums){
        Result result = new Result();

        List<OrderDetail> list = orderDetailService.add(orderId, status, ids, nums);
        System.out.println(list);

        result.setData(list);
        result.success("添加成功");

        return result;
    }

    @GetMapping( "/getAll")
    public Result getAll() {
        Result result = new Result();
        //通过id查找
        List<OrderDetail> list = orderDetailService.list();
//        System.out.println(list);
        for (OrderDetail orderDetail : list) {
            Order order = orderService.getById(orderDetail.getOrderId());
            orderDetail.put("createTime", order.getCreateTime());
            orderDetail.put("no", order.getNo());
//            orderDetail.put("status", order.getStatus());
//            orderDetail.put("address", userService.getById(Integer.valueOf(userId)).getAddress());
        }
        if (!list.isEmpty()) {
            result.setData(list);
            result.success("查询成功");
        }
        else {
            result.fail("查询失败");
        }
        return result;
    }

    //评价
    @GetMapping( "/getDetailByStatus")
    public Result getDetailByStatus(Integer status) {
        Result result = new Result();
        List<OrderDetail> list = orderDetailService.getDetailByStatus(status);
        for (OrderDetail orderDetail : list) {
            Order order = orderService.getById(orderDetail.getOrderId());
            orderDetail.put("createTime", order.getCreateTime());
            orderDetail.put("no", order.getNo());
        }
        if (!list.isEmpty()) {
            result.setData(list);
            result.success("查询成功");
        }
        else {
            result.fail("查询失败");
        }
        return result;
    }

    @GetMapping( "/getByOrderId")
    public Result getByOrderId(Integer orderId) {
        Result result = new Result();
        //通过id查找
        List<OrderDetail> list = orderDetailService.getByOrderId(orderId);
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

    //查订单详情
    @GetMapping( "/getDetail")
    public Result getDetail(String orderIds, Integer status) {
        Result result = new Result();
        //通过id查找
        List<OrderDetail> list = orderDetailService.getDetail(orderIds, status);
        System.out.println(list);
        String token = request.getHeader("Authorization");
//        System.out.println("token" + token);
        String userId = JwtUtil.validateToken(token);
        for (OrderDetail orderDetail : list) {
            Order order = orderService.getById(orderDetail.getOrderId());
//            if (order.getStatus() == -1) //追评
//                orderDetail.put("commentId", commentService.getByBookId(orderDetail.getBookId()));
            orderDetail.put("createTime", order.getCreateTime());
            orderDetail.put("no", order.getNo());
            if (order.getCancelTime() != null)
                orderDetail.put("cancelTime", order.getCancelTime().getTime());
            else
                orderDetail.put("cancelTime", 0);
            orderDetail.put("cancelReason", order.getCancelReason());
//            orderDetail.put("status", order.getStatus());
            orderDetail.put("address", userService.getById(Integer.valueOf(userId)).getAddress());
        }
        if (!list.isEmpty()) {
            result.setData(list);
            result.success("查询成功");
        }
        else {
            result.fail("查询失败");
        }
        return result;
    }

    //更新详情
    @PostMapping("/update")
    public Result update(Integer orderId, Integer bookId, Integer status){
        Result result = new Result();
        if (status == 1) {
            List<OrderDetail> list = orderDetailService.getByOrderId(orderId);
            for (OrderDetail orderDetail: list) {//继续付款
                orderDetail.setStatus(2);
                orderDetailService.updateById(orderDetail);
            }
            result.setData("付款成功");
        }
        else
            result.setData(orderDetailService.update(orderId, bookId, status));
        orderService.updateOrder(orderId, status);
        result.success("更新成功");
        return result;
    }

    //删除
    @PostMapping("/deleteByIds")
    public Result deleteByIds(String orderIds, String bookIds){
        Result result = new Result();
        System.out.println(orderIds);
        System.out.println(bookIds);
        orderDetailService.deleteByIds(orderIds, bookIds);
        String[] arrayOrder = orderIds.split(",");

        for (String i:arrayOrder) {
            List<OrderDetail> orderDetails= orderDetailService.getByOrderId(Integer.valueOf(i));
            if (orderDetails.isEmpty())
                orderService.deleteByIds(orderIds);
        }
        result.success("删除成功");
        return result;
    }
}

