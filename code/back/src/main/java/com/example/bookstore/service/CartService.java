package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookstore.entity.Cart;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
public interface CartService extends IService<Cart> {
    Cart add(Integer bookId);
    Cart getCartByBookId(Integer bookId);
    void deleteByIds(String ids);

    void update(Cart cart);

    List<Cart> getCartByUserId();

    void updateCartByIds(String ids);

    Cart sub(Integer bookId);
}
