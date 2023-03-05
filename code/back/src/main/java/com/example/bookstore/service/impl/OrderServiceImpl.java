package com.example.bookstore.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookstore.entity.Order;
import com.example.bookstore.entity.OrderDetail;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.service.OrderDetailService;
import com.example.bookstore.service.OrderService;
import com.example.bookstore.utils.DateTool;
import com.example.bookstore.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.DelayQueue;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OrderDetailService orderDetailService;
    //    private StringRedisTemplate redisTemplate;
    //是否自动取消订单
    private int isStarted = 1;
    private DelayQueue<Order> queue = new DelayQueue<>();
    @Resource
    private ThreadPoolTaskExecutor executorService;

    @Override
    public void cancelOrder() {
        //新建一个线程，用来模拟定时取消订单
        executorService.submit(()->{
            try {
                System.out.println("开启自动取消订单,当前时间：" + DateTool.getCurrTime());
                while (isStarted == 1 && !queue.isEmpty()) {
                    try {
                        Order order = queue.take();
                        this.cancel(order.getId(), "超时自动取消");
                        List<OrderDetail> list = orderDetailService.getByOrderId(order.getId());
                        for (OrderDetail orderDetail: list) {//更新该订单下的所有订单详情状态
                            orderDetail.setStatus(-2);
                            orderDetailService.updateById(orderDetail);
                        }
//                        queue.remove(order);
                        System.out.println("订单：" + order.getNo() + "付款超时，自动取消，当前时间：" + DateTool.getCurrTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public DelayQueue<Order> pushOrder(Order order){
        executorService.submit(()->{
            try {
                queue.add(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return queue;
    }

    @Override
    public Order add(Double totalPrice, Integer pay) throws ParseException {
        Order order = new Order();
        String token = request.getHeader("Authorization");
//        System.out.println("token" + token);
        String userId = JwtUtil.validateToken(token);

        order.setTotalPrice(totalPrice);
        order.setCreateTime(DateTool.getCurrTime());
        order.setUserId(Integer.valueOf(userId));
        order.setNo(String.valueOf(UUID.randomUUID()));
        //订单状态
        if (pay == 1){//支付
            order.setStatus(2);
        }
        else if (pay == 0) {//未支付
            order.setStatus(1);
            Date date = DateUtil.parse(order.getCreateTime());
//            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
            //自动取消时间5分钟
            order.setCancelTime(DateUtil.offset(date, DateField.MINUTE, 5));
            this.pushOrder(order);
            this.cancelOrder();
//            // 获取缓存 key
//            String cacheKey = order.getNo();
//            System.out.println("OrderService.receipt cache key is "+ cacheKey);
//
//            // 设置订单过期 cache
//            redisTemplate.opsForValue().set(cacheKey, cacheKey, 20, TimeUnit.SECONDS);
        }
        this.save(order);
        return order;
    }

    @Override
    public List<Order> getByStatus(Integer status) {
        String token = request.getHeader("Authorization");
//        System.out.println("token" + token);
        String userId = JwtUtil.validateToken(token);
        System.out.println(userId);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();

        if(status == 0){//0-全部订单
            queryWrapper.eq("user_id", Integer.valueOf(userId));
        }
        else {//其他状态：1-待支付，2-待发货，3-待收货，4-待评价，5-退款,-1-待追评;-3-追评完-2-订单取消
            queryWrapper.eq("user_id", Integer.valueOf(userId)).eq("status", status);
        }
        return this.list(queryWrapper);
    }

    @Override
    public Order updateOrder(Integer orderId, Integer status) {
//        String token = request.getHeader("Authorization");
////        System.out.println("token" + token);
//        String userId = JwtUtil.validateToken(token);
//        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();

        Order order = this.getById(orderId);
        if (status == 4) {
            order.setStatus(-1);
        }
        else if (status == -1)
            order.setStatus(-3);
        else {
            if (order.getStatus() == 1) {//继续付款，从超时队列里删除
                order.setCancelTime(null);
                for (Order q:queue)
                    if (q.getId().equals(orderId))
                        queue.remove(q);
            }
            order.setStatus(status+1);
        }
        this.updateById(order);
        return order;
    }

    @Override
    public List<Order> getByUserId() {
        String token = request.getHeader("Authorization");
//        System.out.println("token" + token);
        String userId = JwtUtil.validateToken(token);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Integer.valueOf(userId));
        return this.list(queryWrapper);
    }

    @Override
    public Page<Order> getAll(Integer pageNum, Integer pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        return this.page(page, queryWrapper);
    }

    @Override
    public void deleteByIds(String ids) {//根据Id删除
        //        System.out.println(ids);
        List<String> list = new ArrayList<>();
        String[] array = ids.split(",");
        for (String i:array) {
            list.add(i);
        }
        this.removeByIds(list);
    }

    @Override
    public Order cancel(Integer orderId, String reason) {
//        String token = request.getHeader("Authorization");
////        System.out.println("token" + token);
//        String userId = JwtUtil.validateToken(token);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);
        Order order = this.getOne(queryWrapper);
        if (order != null) {
            order.setStatus(-2);//取消订单
            order.setCancelTime(new Date());
            order.setCancelReason(reason);
            this.updateById(order);
        }
        return order;
    }

    @Override
    public Order getByNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("no", orderNo);
        return this.getOne(queryWrapper);
    }
}
