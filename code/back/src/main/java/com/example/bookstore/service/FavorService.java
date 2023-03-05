package com.example.bookstore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.Favor;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2022-10-18
 */
public interface FavorService extends IService<Favor> {
    Favor add(Integer bookId);
    Favor getFavor(Integer bookId);
    void deleteByIds(String ids);

    void update(Favor favor);

    List<Favor> getFavorByUserId();

}
