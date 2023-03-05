package com.example.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.mapper.CartMapper;
import com.example.bookstore.service.CartService;
import com.example.bookstore.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    @Autowired
    private HttpServletRequest request;

    @Override
    public Cart add(Integer bookId) {

        String token = request.getHeader("Authorization");
        String userId = JwtUtil.validateToken(token);
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Integer.valueOf(userId)).eq("book_id", bookId);
        Cart cart = this.getOne(queryWrapper);
        if (cart != null) {
            cart.setNum(cart.getNum()+1);
            this.update(cart);
        }
        else {
            cart = new Cart();
            cart.setUserId(Integer.valueOf(userId));
            cart.setBookId(bookId);
            cart.setNum(1);
            cart.setId(cart.getId());
            //默认不选中
            cart.setChecked(0);
            this.save(cart);
        }
        return cart;
    }
    @Override
    public Cart sub(Integer bookId) {
        String token = request.getHeader("Authorization");
//        System.out.println("token" + token);
        String userId = JwtUtil.validateToken(token);
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Integer.valueOf(userId)).eq("book_id", bookId);
        Cart cart = this.getOne(queryWrapper);
        cart.setNum(cart.getNum()-1);
        this.update(cart);
        return cart;
    }

    @Override
    public Cart getCartByBookId(Integer bookId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookId);
        return this.getOne(queryWrapper);

    }

    @Override
    public void deleteByIds(String ids) {//根据Id删除书籍
//        System.out.println(ids);
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        List<String> list = new ArrayList<>();
        String[] array = ids.split(",");
        for (String i:array) {
            list.add(i);
        }
        if (list.size() == 1) {
            queryWrapper.eq("book_id",list.get(0));
            this.remove(queryWrapper);
        }
        else {
            queryWrapper.in("book_id", list);
            this.remove(queryWrapper);
        }
    }

    @Override
    public void update(Cart cart) {
        this.updateById(cart);
    }

    @Override
    public List<Cart> getCartByUserId() {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        String token = request.getHeader("Authorization");
//        System.out.println("token" +token);
        String userId = JwtUtil.validateToken(token);
        queryWrapper.eq("user_id", Integer.valueOf(userId));

        return this.list(queryWrapper);
    }

    @Override
    public void updateCartByIds(String ids) {
        System.out.println("ids: "+ ids);
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        String token = request.getHeader("Authorization");
        System.out.println("token" + token);
        String userId = JwtUtil.validateToken(token);
        System.out.println(userId);
        List<String> list = new ArrayList<>();
        String[] array = ids.split(",");

        for (String id:array) {
            list.add(id);
        }
        queryWrapper.in("book_id", list).eq("user_id", Integer.valueOf(userId)).eq("checked", 0);
        List<Cart> cartList = this.list(queryWrapper);
        Boolean allCheck = true;
        if (cartList.size() != 0)
            allCheck = false;
        if (allCheck) {
            List<Cart> list1 = this.getCartByUserId();
            for (Cart cart: list1) {
                cart.setChecked(0);
                this.update(cart);
            }
        }
        else {
            for (Cart cart: cartList) {
                cart.setChecked(1);
                this.update(cart);
            }
        }
    }
}
